package com.vetcare.backend.service;

import com.vetcare.backend.dto.AtencionRequest;
import com.vetcare.backend.dto.AtencionResponse;
import com.vetcare.backend.entity.Atencion;
import com.vetcare.backend.entity.Cita;
import com.vetcare.backend.entity.Mascota;
import com.vetcare.backend.entity.Usuario;
import com.vetcare.backend.exception.BusinessException;
import com.vetcare.backend.exception.ResourceNotFoundException;
import com.vetcare.backend.repository.AtencionRepository;
import com.vetcare.backend.repository.CitaRepository;
import com.vetcare.backend.repository.MascotaRepository;
import com.vetcare.backend.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AtencionService {

    private final AtencionRepository atencionRepository;
    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CitaRepository citaRepository;

    public AtencionService(
            AtencionRepository atencionRepository,
            MascotaRepository mascotaRepository,
            UsuarioRepository usuarioRepository,
            CitaRepository citaRepository) {

        this.atencionRepository = atencionRepository;
        this.mascotaRepository = mascotaRepository;
        this.usuarioRepository = usuarioRepository;
        this.citaRepository = citaRepository;
    }

    @Transactional(readOnly = true)
    public List<AtencionResponse> listar() {
        return atencionRepository.findAll()
                .stream()
                .map(this::convertir)
                .toList();
    }

    @Transactional(readOnly = true)
    public AtencionResponse obtenerPorId(Long id) {
        return convertir(buscarEntidad(id));
    }

    @Transactional(readOnly = true)
    public List<AtencionResponse> historialPorMascota(
            Long mascotaId) {

        if (!mascotaRepository.existsById(mascotaId)) {
            throw new ResourceNotFoundException(
                    "No se encontró la mascota con ID " + mascotaId
            );
        }

        return atencionRepository
                .findByMascotaIdOrderByFechaDescHoraDesc(mascotaId)
                .stream()
                .map(this::convertir)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AtencionResponse> listarPorVeterinario(
            Long veterinarioId) {

        buscarVeterinario(veterinarioId);

        return atencionRepository
                .findByVeterinarioIdOrderByFechaDescHoraDesc(
                        veterinarioId
                )
                .stream()
                .map(this::convertir)
                .toList();
    }

    public AtencionResponse crear(AtencionRequest request) {

        Mascota mascota = buscarMascota(request.mascotaId());
        Usuario veterinario =
                buscarVeterinario(request.veterinarioId());

        Atencion atencion = new Atencion();

        aplicarDatos(
                atencion,
                request,
                mascota,
                veterinario
        );

        Atencion guardada = atencionRepository.save(atencion);

        marcarCitaComoAtendida(
                request,
                mascota,
                veterinario
        );

        return convertir(guardada);
    }

    public AtencionResponse actualizar(
            Long id,
            AtencionRequest request) {

        Atencion atencion = buscarEntidad(id);

        Mascota mascota = buscarMascota(request.mascotaId());
        Usuario veterinario =
                buscarVeterinario(request.veterinarioId());

        aplicarDatos(
                atencion,
                request,
                mascota,
                veterinario
        );

        return convertir(atencionRepository.save(atencion));
    }

    private Atencion buscarEntidad(Long id) {
        return atencionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la atención con ID " + id
                        )
                );
    }

    private Mascota buscarMascota(Long id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la mascota con ID " + id
                        )
                );
    }

    private Usuario buscarVeterinario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el veterinario con ID " + id
                        )
                );

        if (!usuario.getRol().equalsIgnoreCase("Médico veterinario")) {
            throw new BusinessException(
                    "El usuario seleccionado no tiene el rol de Médico veterinario"
            );
        }

        if (!usuario.getEstado().equalsIgnoreCase("Activo")) {
            throw new BusinessException(
                    "El médico veterinario seleccionado se encuentra inactivo"
            );
        }

        return usuario;
    }

    private void aplicarDatos(
            Atencion atencion,
            AtencionRequest request,
            Mascota mascota,
            Usuario veterinario) {

        atencion.setFecha(request.fecha());
        atencion.setHora(request.hora());
        atencion.setMascota(mascota);
        atencion.setVeterinario(veterinario);
        atencion.setMotivo(request.motivo().trim());

        atencion.setObservaciones(
                limpiarOpcional(request.observaciones())
        );

        atencion.setIndicaciones(
                limpiarOpcional(request.indicaciones())
        );
    }

    private String limpiarOpcional(String valor) {

        if (valor == null) {
            return null;
        }

        String limpio = valor.trim();

        return limpio.isBlank() ? null : limpio;
    }

    private void marcarCitaComoAtendida(
            AtencionRequest request,
            Mascota mascota,
            Usuario veterinario) {

        List<Cita> citas =
                citaRepository
                        .findByVeterinarioIdAndFechaOrderByHoraAsc(
                                veterinario.getId(),
                                request.fecha()
                        );

        citas.stream()
                .filter(c ->
                        c.getMascota().getId().equals(mascota.getId())
                )
                .filter(c ->
                        c.getHora().equals(request.hora())
                )
                .filter(c ->
                        !c.getEstado().equalsIgnoreCase("Cancelada")
                )
                .findFirst()
                .ifPresent(c -> {
                    c.setEstado("Atendida");
                    citaRepository.save(c);
                });
    }

    private AtencionResponse convertir(Atencion atencion) {

        return new AtencionResponse(
                atencion.getId(),
                atencion.getFecha(),
                atencion.getHora(),
                atencion.getMascota().getId(),
                atencion.getMascota().getNombre(),
                atencion.getMascota().getPropietario().getId(),
                atencion.getMascota().getPropietario().getNombres(),
                atencion.getVeterinario().getId(),
                atencion.getVeterinario().getNombres(),
                atencion.getMotivo(),
                atencion.getObservaciones(),
                atencion.getIndicaciones()
        );
    }
}