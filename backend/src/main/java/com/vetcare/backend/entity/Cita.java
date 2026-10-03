package com.vetcare.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
    name = "citas",
    indexes = {
        @Index(name = "idx_cita_fecha", columnList = "fecha"),
        @Index(name = "idx_cita_veterinario", columnList = "veterinario_id")
    }
)
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de la cita es obligatoria")
    @Column(nullable = false, columnDefinition = "date")
    private LocalDate fecha;

    @NotNull(message = "La hora de la cita es obligatoria")
    @Column(nullable = false, columnDefinition = "time")
    private LocalTime hora;

    @NotNull(message = "La mascota es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "mascota_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_cita_mascota")
    )
    private Mascota mascota;

    @NotBlank(message = "El motivo de la cita es obligatorio")
    @Size(max = 250, message = "El motivo no puede superar los 250 caracteres")
    @Column(nullable = false, length = 250)
    private String motivo;

    @NotNull(message = "El veterinario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "veterinario_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_cita_veterinario")
    )
    private Usuario veterinario;

    @NotBlank(message = "El estado de la cita es obligatorio")
    @Size(max = 20, message = "El estado no puede superar los 20 caracteres")
    @Column(nullable = false, length = 20)
    private String estado;

    public Cita() {
    }

    public Cita(
            Long id,
            LocalDate fecha,
            LocalTime hora,
            Mascota mascota,
            String motivo,
            Usuario veterinario,
            String estado) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.mascota = mascota;
        this.motivo = motivo;
        this.veterinario = veterinario;
        this.estado = estado;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Usuario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Usuario veterinario) {
        this.veterinario = veterinario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}