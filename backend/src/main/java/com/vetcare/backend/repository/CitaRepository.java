package com.vetcare.backend.repository;

import com.vetcare.backend.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByFechaOrderByHoraAsc(LocalDate fecha);

    List<Cita> findByMascotaIdOrderByFechaDescHoraDesc(Long mascotaId);

    List<Cita> findByVeterinarioIdAndFechaOrderByHoraAsc(
            Long veterinarioId,
            LocalDate fecha
    );
}