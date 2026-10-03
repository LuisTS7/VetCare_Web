package com.vetcare.backend.service;

import com.vetcare.backend.dto.UsuarioRequest;
import com.vetcare.backend.dto.UsuarioResponse;
import com.vetcare.backend.entity.Usuario;
import com.vetcare.backend.exception.BusinessException;
import com.vetcare.backend.exception.ResourceNotFoundException;
import com.vetcare.backend.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UsuarioService {

    private static final Set<String> ROLES_VALIDOS = Set.of(
            "Administrador",
            "Recepcionista",
            "Médico veterinario"
    );

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            "Activo",
            "Inactivo"
    );

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertir)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Long id) {
        return convertir(buscarEntidad(id));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarVeterinariosActivos() {

        return usuarioRepository
                .findByRolIgnoreCaseAndEstadoIgnoreCase(
                        "Médico veterinario",
                        "Activo"
                )
                .stream()
                .map(this::convertir)
                .toList();
    }

    public UsuarioResponse crear(UsuarioRequest request) {

        String correo = request.correo()
                .trim()
                .toLowerCase();

        if (usuarioRepository.existsByCorreoIgnoreCase(correo)) {
            throw new BusinessException(
                    HttpStatus.CONFLICT,
                    "Ya existe un usuario registrado con ese correo"
            );
        }

        validarRol(request.rol());
        validarEstado(request.estado());

        if (request.password() == null ||
                request.password().isBlank()) {

            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "La contraseña es obligatoria"
            );
        }

        Usuario usuario = new Usuario();

        usuario.setNombres(request.nombres().trim());
        usuario.setCorreo(correo);

        usuario.setPassword(
                passwordEncoder.encode(request.password())
        );

        usuario.setRol(normalizarRol(request.rol()));
        usuario.setEstado(normalizarEstado(request.estado()));

        return convertir(usuarioRepository.save(usuario));
    }

    public UsuarioResponse actualizar(
            Long id,
            UsuarioRequest request) {

        Usuario usuario = buscarEntidad(id);

        String correo = request.correo()
                .trim()
                .toLowerCase();

        usuarioRepository.findByCorreoIgnoreCase(correo)
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new BusinessException(
                            HttpStatus.CONFLICT,
                            "Ya existe otro usuario registrado con ese correo"
                    );
                });

        validarRol(request.rol());
        validarEstado(request.estado());

        usuario.setNombres(request.nombres().trim());
        usuario.setCorreo(correo);

        /*
         * La contraseña solo se modifica cuando
         * el usuario ingresa una nueva contraseña.
         * Si llega vacía o nula, se conserva la actual.
         */
        if (request.password() != null &&
                !request.password().isBlank()) {

            usuario.setPassword(
                    passwordEncoder.encode(request.password())
            );
        }

        usuario.setRol(normalizarRol(request.rol()));
        usuario.setEstado(normalizarEstado(request.estado()));

        return convertir(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public Usuario buscarEntidad(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el usuario con ID " + id
                        )
                );
    }

    private void validarRol(String rol) {

        boolean valido = ROLES_VALIDOS.stream()
                .anyMatch(r -> r.equalsIgnoreCase(rol.trim()));

        if (!valido) {
            throw new BusinessException(
                    "El rol indicado no es válido"
            );
        }
    }

    private void validarEstado(String estado) {

        boolean valido = ESTADOS_VALIDOS.stream()
                .anyMatch(e -> e.equalsIgnoreCase(estado.trim()));

        if (!valido) {
            throw new BusinessException(
                    "El estado indicado no es válido"
            );
        }
    }

    private String normalizarRol(String rol) {
        return ROLES_VALIDOS.stream()
                .filter(r -> r.equalsIgnoreCase(rol.trim()))
                .findFirst()
                .orElseThrow();
    }

    private String normalizarEstado(String estado) {
        return ESTADOS_VALIDOS.stream()
                .filter(e -> e.equalsIgnoreCase(estado.trim()))
                .findFirst()
                .orElseThrow();
    }

    private UsuarioResponse convertir(Usuario usuario) {

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombres(),
                usuario.getCorreo(),
                usuario.getRol(),
                usuario.getEstado()
        );
    }
}