package com.vetcare.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record AtencionRequest(

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @NotNull(message = "La hora es obligatoria")
        LocalTime hora,

        @NotNull(message = "La mascota es obligatoria")
        Long mascotaId,

        @NotNull(message = "El veterinario es obligatorio")
        Long veterinarioId,

        @NotBlank(message = "El motivo es obligatorio")
        @Size(max = 250, message = "El motivo no puede superar los 250 caracteres")
        String motivo,

        @Size(max = 2000, message = "Las observaciones no pueden superar los 2000 caracteres")
        String observaciones,

        @Size(max = 2000, message = "Las indicaciones no pueden superar los 2000 caracteres")
        String indicaciones

) {
}