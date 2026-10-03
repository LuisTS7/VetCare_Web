package com.vetcare.backend.service;

import com.vetcare.backend.dto.MascotaRequest;
import com.vetcare.backend.dto.MascotaResponse;
import com.vetcare.backend.entity.Mascota;
import com.vetcare.backend.entity.Propietario;
import com.vetcare.backend.exception.ResourceNotFoundException;
import com.vetcare.backend.repository.MascotaRepository;
import com.vetcare.backend.repository.PropietarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final PropietarioRepository propietarioRepository;

    public MascotaService(
            MascotaRepository mascotaRepository,
            PropietarioRepository propietarioRepository) {

        this.mascotaRepository = mascotaRepository;
        this.propietarioRepository = propietarioRepository;
    }

    @Transactional(readOnly = true)
    public List<MascotaResponse> listar() {
        return mascotaRepository.findAll()
                .stream()
                .map(this::convertir)
                .toList();
    }

    @Transactional(readOnly = true)
    public MascotaResponse obtenerPorId(Long id) {
        return convertir(buscarEntidad(id));
    }

    @Transactional(readOnly = true)
    public List<MascotaResponse> listarPorPropietario(
            Long propietarioId) {

        if (!propietarioRepository.existsById(propietarioId)) {
            throw new ResourceNotFoundException(
                    "No se encontró el propietario con ID "
                            + propietarioId
            );
        }

        return mascotaRepository
                .findByPropietarioId(propietarioId)
                .stream()
                .map(this::convertir)
                .toList();
    }

    public MascotaResponse crear(MascotaRequest request) {

        Propietario propietario =
                buscarPropietario(request.propietarioId());

        Mascota mascota = new Mascota();

        aplicarDatos(mascota, request, propietario);

        return convertir(mascotaRepository.save(mascota));
    }

    public MascotaResponse actualizar(
            Long id,
            MascotaRequest request) {

        Mascota mascota = buscarEntidad(id);

        Propietario propietario =
                buscarPropietario(request.propietarioId());

        aplicarDatos(mascota, request, propietario);

        return convertir(mascotaRepository.save(mascota));
    }

    private Mascota buscarEntidad(Long id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la mascota con ID " + id
                        )
                );
    }

    private Propietario buscarPropietario(Long id) {
        return propietarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el propietario con ID " + id
                        )
                );
    }

    private void aplicarDatos(
            Mascota mascota,
            MascotaRequest request,
            Propietario propietario) {

        mascota.setNombre(request.nombre().trim());
        mascota.setEspecie(request.especie().trim());

        mascota.setRaza(
                request.raza() == null
                        ? null
                        : request.raza().trim()
        );

        mascota.setSexo(request.sexo().trim());
        mascota.setEdad(request.edad());
        mascota.setPropietario(propietario);
    }

    private MascotaResponse convertir(Mascota mascota) {

        return new MascotaResponse(
                mascota.getId(),
                mascota.getNombre(),
                mascota.getEspecie(),
                mascota.getRaza(),
                mascota.getSexo(),
                mascota.getEdad(),
                mascota.getPropietario().getId(),
                mascota.getPropietario().getNombres()
        );
    }
}