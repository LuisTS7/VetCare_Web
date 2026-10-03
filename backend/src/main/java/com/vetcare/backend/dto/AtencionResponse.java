package com.vetcare.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AtencionResponse(
        Long id,
        LocalDate fecha,
        LocalTime hora,
        Long mascotaId,
        String mascota,
        Long propietarioId,
        String propietario,
        Long veterinarioId,
        String veterinario,
        String motivo,
        String observaciones,
        String indicaciones
) {
}