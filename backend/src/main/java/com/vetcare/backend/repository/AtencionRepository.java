package com.vetcare.backend.repository;

import com.vetcare.backend.entity.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AtencionRepository extends JpaRepository<Atencion, Long> {

    List<Atencion> findByMascotaIdOrderByFechaDescHoraDesc(Long mascotaId);

    List<Atencion> findByVeterinarioIdOrderByFechaDescHoraDesc(
            Long veterinarioId
    );

    List<Atencion> findByVeterinarioIdAndFechaOrderByHoraAsc(
            Long veterinarioId,
            LocalDate fecha
    );
}