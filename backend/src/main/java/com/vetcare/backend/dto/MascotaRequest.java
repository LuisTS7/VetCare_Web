package com.vetcare.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MascotaRequest(

        @NotBlank(message = "El nombre de la mascota es obligatorio")
        @Size(max = 80, message = "El nombre no puede superar los 80 caracteres")
        String nombre,

        @NotBlank(message = "La especie es obligatoria")
        @Size(max = 50, message = "La especie no puede superar los 50 caracteres")
        String especie,

        @Size(max = 80, message = "La raza no puede superar los 80 caracteres")
        String raza,

        @NotBlank(message = "El sexo es obligatorio")
        @Size(max = 20, message = "El sexo no puede superar los 20 caracteres")
        String sexo,

        @NotNull(message = "La edad es obligatoria")
        @Min(value = 0, message = "La edad no puede ser negativa")
        Integer edad,

        @NotNull(message = "El propietario es obligatorio")
        Long propietarioId

) {
}