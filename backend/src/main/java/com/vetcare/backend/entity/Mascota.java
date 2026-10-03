package com.vetcare.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar los 80 caracteres")
    @Column(nullable = false, length = 80)
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    @Size(max = 50, message = "La especie no puede superar los 50 caracteres")
    @Column(nullable = false, length = 50)
    private String especie;

    @Size(max = 80, message = "La raza no puede superar los 80 caracteres")
    @Column(length = 80)
    private String raza;

    @NotBlank(message = "El sexo es obligatorio")
    @Size(max = 20, message = "El sexo no puede superar los 20 caracteres")
    @Column(nullable = false, length = 20)
    private String sexo;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    @Column(nullable = false)
    private Integer edad;

    @NotNull(message = "El propietario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "propietario_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_mascota_propietario")
    )
    private Propietario propietario;

    public Mascota() {
    }

    public Mascota(
            Long id,
            String nombre,
            String especie,
            String raza,
            String sexo,
            Integer edad,
            Propietario propietario) {

        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.edad = edad;
        this.propietario = propietario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }
}