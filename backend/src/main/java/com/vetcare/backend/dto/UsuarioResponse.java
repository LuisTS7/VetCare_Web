package com.vetcare.backend.dto;

public record UsuarioResponse(
        Long id,
        String nombres,
        String correo,
        String rol,
        String estado
) {
}