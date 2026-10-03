package com.vetcare.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CitaResponse(
        Long id,
        LocalDate fecha,
        LocalTime hora,
        Long mascotaId,
        String mascota,
        Long propietarioId,
        String propietario,
        String motivo,
        Long veterinarioId,
        String veterinario,
        String estado
) {
}