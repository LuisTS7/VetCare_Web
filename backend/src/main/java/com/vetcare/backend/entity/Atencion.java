package com.vetcare.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
    name = "atenciones",
    indexes = {
        @Index(name = "idx_atencion_fecha", columnList = "fecha"),
        @Index(name = "idx_atencion_mascota", columnList = "mascota_id"),
        @Index(name = "idx_atencion_veterinario", columnList = "veterinario_id")
    }
)
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de la atención es obligatoria")
    @Column(nullable = false, columnDefinition = "date")
    private LocalDate fecha;

    @NotNull(message = "La hora de la atención es obligatoria")
    @Column(nullable = false, columnDefinition = "time")
    private LocalTime hora;

    @NotNull(message = "La mascota es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "mascota_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_atencion_mascota")
    )
    private Mascota mascota;

    @NotNull(message = "El veterinario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "veterinario_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_atencion_veterinario")
    )
    private Usuario veterinario;

    @NotBlank(message = "El motivo de la atención es obligatorio")
    @Size(max = 250, message = "El motivo no puede superar los 250 caracteres")
    @Column(nullable = false, length = 250)
    private String motivo;

    @Size(max = 2000, message = "Las observaciones no pueden superar los 2000 caracteres")
    @Column(length = 2000)
    private String observaciones;

    @Size(max = 2000, message = "Las indicaciones no pueden superar los 2000 caracteres")
    @Column(length = 2000)
    private String indicaciones;

    public Atencion() {
    }

    public Atencion(
            Long id,
            LocalDate fecha,
            LocalTime hora,
            Mascota mascota,
            Usuario veterinario,
            String motivo,
            String observaciones,
            String indicaciones) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.motivo = motivo;
        this.observaciones = observaciones;
        this.indicaciones = indicaciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Usuario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Usuario veterinario) {
        this.veterinario = veterinario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }
}