package com.vetcare.backend.service;

import com.vetcare.backend.entity.Propietario;
import com.vetcare.backend.exception.BusinessException;
import com.vetcare.backend.exception.ResourceNotFoundException;
import com.vetcare.backend.repository.PropietarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PropietarioService {

    private final PropietarioRepository propietarioRepository;

    public PropietarioService(
            PropietarioRepository propietarioRepository) {
        this.propietarioRepository = propietarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Propietario> listar() {
        return propietarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Propietario obtenerPorId(Long id) {
        return propietarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el propietario con ID " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public Propietario obtenerPorDni(String dni) {
        return propietarioRepository.findByDni(dni)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró un propietario con DNI " + dni
                        )
                );
    }

    public Propietario crear(Propietario propietario) {

        limpiar(propietario);

        if (propietarioRepository.existsByDni(propietario.getDni())) {
            throw new BusinessException(
                    HttpStatus.CONFLICT,
                    "Ya existe un propietario registrado con el DNI "
                            + propietario.getDni()
            );
        }

        propietario.setId(null);

        return propietarioRepository.save(propietario);
    }

    public Propietario actualizar(
            Long id,
            Propietario datos) {

        Propietario actual = obtenerPorId(id);

        limpiar(datos);

        propietarioRepository.findByDni(datos.getDni())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> {
                    throw new BusinessException(
                            HttpStatus.CONFLICT,
                            "Ya existe otro propietario registrado con el DNI "
                                    + datos.getDni()
                    );
                });

        actual.setDni(datos.getDni());
        actual.setNombres(datos.getNombres());
        actual.setTelefono(datos.getTelefono());
        actual.setCorreo(datos.getCorreo());

        return propietarioRepository.save(actual);
    }

    private void limpiar(Propietario propietario) {

        if (propietario.getDni() != null) {
            propietario.setDni(propietario.getDni().trim());
        }

        if (propietario.getNombres() != null) {
            propietario.setNombres(propietario.getNombres().trim());
        }

        if (propietario.getTelefono() != null) {
            propietario.setTelefono(propietario.getTelefono().trim());
        }

        if (propietario.getCorreo() != null) {
            String correo = propietario.getCorreo().trim();

            propietario.setCorreo(
                    correo.isBlank() ? null : correo.toLowerCase()
            );
        }
    }
}