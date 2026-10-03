package com.vetcare.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo electrónico no tiene un formato válido")
        String correo,

        @NotBlank(message = "La contraseña es obligatoria")
        String password

) {
}