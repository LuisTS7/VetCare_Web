package com.vetcare.backend.repository;

import com.vetcare.backend.entity.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropietarioRepository extends JpaRepository<Propietario, Long> {

    Optional<Propietario> findByDni(String dni);

    boolean existsByDni(String dni);
}