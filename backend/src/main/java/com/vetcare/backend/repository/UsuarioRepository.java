package com.vetcare.backend.repository;

import com.vetcare.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    boolean existsByCorreoIgnoreCase(String correo);

    List<Usuario> findByRolIgnoreCase(String rol);

    List<Usuario> findByEstadoIgnoreCase(String estado);

    List<Usuario> findByRolIgnoreCaseAndEstadoIgnoreCase(
            String rol,
            String estado
    );
}