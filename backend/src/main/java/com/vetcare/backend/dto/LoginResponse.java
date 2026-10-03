package com.vetcare.backend.dto;

public record LoginResponse(
        Long id,
        String nombres,
        String correo,
        String rol,
        String estado,
        String token
) {
}