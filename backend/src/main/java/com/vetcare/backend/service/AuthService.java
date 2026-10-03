package com.vetcare.backend.service;

import com.vetcare.backend.dto.LoginRequest;
import com.vetcare.backend.dto.LoginResponse;
import com.vetcare.backend.entity.Usuario;
import com.vetcare.backend.exception.BusinessException;
import com.vetcare.backend.repository.UsuarioRepository;
import com.vetcare.backend.security.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        String correo = request.correo()
                .trim()
                .toLowerCase();

        Usuario usuario = usuarioRepository
                .findByCorreoIgnoreCase(correo)
                .orElseThrow(() ->
                        new BusinessException(
                                HttpStatus.UNAUTHORIZED,
                                "Correo o contraseña incorrectos"
                        )
                );

        if (!"Activo".equalsIgnoreCase(usuario.getEstado())) {
            throw new BusinessException(
                    HttpStatus.FORBIDDEN,
                    "El usuario se encuentra inactivo"
            );
        }

        if (!passwordEncoder.matches(
                request.password(),
                usuario.getPassword())) {

            throw new BusinessException(
                    HttpStatus.UNAUTHORIZED,
                    "Correo o contraseña incorrectos"
            );
        }

        String token = jwtService.generarToken(
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getRol()
        );

        return new LoginResponse(
                usuario.getId(),
                usuario.getNombres(),
                usuario.getCorreo(),
                usuario.getRol(),
                usuario.getEstado(),
                token
        );
    }
}