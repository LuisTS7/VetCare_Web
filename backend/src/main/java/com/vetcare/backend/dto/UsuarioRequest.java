package com.vetcare.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 120, message = "Los nombres no pueden superar los 120 caracteres")
        String nombres,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo electrónico no tiene un formato válido")
        @Size(max = 120, message = "El correo no puede superar los 120 caracteres")
        String correo,

        @Size(min = 6, max = 72, message = "La contraseña debe tener entre 6 y 72 caracteres")
        String password,

        @NotBlank(message = "El rol es obligatorio")
        String rol,

        @NotBlank(message = "El estado es obligatorio")
        String estado

) {
}