package com.vetcare.backend.service;

import com.vetcare.backend.dto.CitaRequest;
import com.vetcare.backend.dto.CitaResponse;
import com.vetcare.backend.entity.Cita;
import com.vetcare.backend.entity.Mascota;
import com.vetcare.backend.entity.Usuario;
import com.vetcare.backend.exception.BusinessException;
import com.vetcare.backend.exception.ResourceNotFoundException;
import com.vetcare.backend.repository.CitaRepository;
import com.vetcare.backend.repository.MascotaRepository;
import com.vetcare.backend.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CitaService {

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            "Pendiente",
            "Confirmada",
            "Atendida",
            "Cancelada"
    );

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;

    public CitaService(
            CitaRepository citaRepository,
            MascotaRepository mascotaRepository,
            UsuarioRepository usuarioRepository) {

        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> listar() {
        return citaRepository.findAll()
                .stream()
                .map(this::convertir)
                .toList();
    }

    @Transactional(readOnly = true)
    public CitaResponse obtenerPorId(Long id) {
        return convertir(buscarEntidad(id));
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> listarPorFecha(LocalDate fecha) {
        return citaRepository.findByFechaOrderByHoraAsc(fecha)
                .stream()
                .map(this::convertir)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> listarPorVeterinarioYFecha(
            Long veterinarioId,
            LocalDate fecha) {

        buscarVeterinario(veterinarioId);

        return citaRepository
                .findByVeterinarioIdAndFechaOrderByHoraAsc(
                        veterinarioId,
                        fecha
                )
                .stream()
                .map(this::convertir)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> listarPorMascota(Long mascotaId) {

        if (!mascotaRepository.existsById(mascotaId)) {
            throw new ResourceNotFoundException(
                    "No se encontró la mascota con ID " + mascotaId
            );
        }

        return citaRepository
                .findByMascotaIdOrderByFechaDescHoraDesc(mascotaId)
                .stream()
                .map(this::convertir)
                .toList();
    }

    public CitaResponse crear(CitaRequest request) {

        Mascota mascota = buscarMascota(request.mascotaId());
        Usuario veterinario = buscarVeterinario(request.veterinarioId());

        String estado = normalizarEstado(request.estado());

        validarDisponibilidad(
                null,
                veterinario.getId(),
                request.fecha(),
                request.hora(),
                estado
        );

        Cita cita = new Cita();

        aplicarDatos(
                cita,
                request,
                mascota,
                veterinario,
                estado
        );

        return convertir(citaRepository.save(cita));
    }

    public CitaResponse actualizar(
            Long id,
            CitaRequest request) {

        Cita cita = buscarEntidad(id);

        Mascota mascota = buscarMascota(request.mascotaId());
        Usuario veterinario = buscarVeterinario(request.veterinarioId());

        String estado = normalizarEstado(request.estado());

        validarDisponibilidad(
                id,
                veterinario.getId(),
                request.fecha(),
                request.hora(),
                estado
        );

        aplicarDatos(
                cita,
                request,
                mascota,
                veterinario,
                estado
        );

        return convertir(citaRepository.save(cita));
    }

    public CitaResponse cambiarEstado(
            Long id,
            String estado) {

        Cita cita = buscarEntidad(id);

        String estadoNormalizado = normalizarEstado(estado);

        if (!estadoNormalizado.equalsIgnoreCase("Cancelada")) {
            validarDisponibilidad(
                    id,
                    cita.getVeterinario().getId(),
                    cita.getFecha(),
                    cita.getHora(),
                    estadoNormalizado
            );
        }

        cita.setEstado(estadoNormalizado);

        return convertir(citaRepository.save(cita));
    }

    public Cita buscarEntidad(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la cita con ID " + id
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

    private void validarDisponibilidad(
            Long citaId,
            Long veterinarioId,
            java.time.LocalDate fecha,
            java.time.LocalTime hora,
            String estado) {

        if (estado.equalsIgnoreCase("Cancelada")) {
            return;
        }

        boolean ocupado = citaRepository
                .findByVeterinarioIdAndFechaOrderByHoraAsc(
                        veterinarioId,
                        fecha
                )
                .stream()
                .filter(cita ->
                        citaId == null ||
                        !cita.getId().equals(citaId)
                )
                .filter(cita ->
                        !cita.getEstado().equalsIgnoreCase("Cancelada")
                )
                .anyMatch(cita ->
                        cita.getHora().equals(hora)
                );

        if (ocupado) {
            throw new BusinessException(
                    HttpStatus.CONFLICT,
                    "El veterinario ya tiene una cita programada en esa fecha y hora"
            );
        }
    }

    private void aplicarDatos(
            Cita cita,
            CitaRequest request,
            Mascota mascota,
            Usuario veterinario,
            String estado) {

        cita.setFecha(request.fecha());
        cita.setHora(request.hora());
        cita.setMascota(mascota);
        cita.setMotivo(request.motivo().trim());
        cita.setVeterinario(veterinario);
        cita.setEstado(estado);
    }

    private String normalizarEstado(String estado) {

        if (estado == null || estado.isBlank()) {
            throw new BusinessException(
                    "El estado de la cita es obligatorio"
            );
        }

        return ESTADOS_VALIDOS.stream()
                .filter(e -> e.equalsIgnoreCase(estado.trim()))
                .findFirst()
                .orElseThrow(() ->
                        new BusinessException(
                                "El estado de la cita no es válido"
                        )
                );
    }

    private CitaResponse convertir(Cita cita) {

        return new CitaResponse(
                cita.getId(),
                cita.getFecha(),
                cita.getHora(),
                cita.getMascota().getId(),
                cita.getMascota().getNombre(),
                cita.getMascota().getPropietario().getId(),
                cita.getMascota().getPropietario().getNombres(),
                cita.getMotivo(),
                cita.getVeterinario().getId(),
                cita.getVeterinario().getNombres(),
                cita.getEstado()
        );
    }
}