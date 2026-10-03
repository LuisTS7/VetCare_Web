package com.vetcare.backend.dto;

public record MascotaResponse(
        Long id,
        String nombre,
        String especie,
        String raza,
        String sexo,
        Integer edad,
        Long propietarioId,
        String propietario
) {
}