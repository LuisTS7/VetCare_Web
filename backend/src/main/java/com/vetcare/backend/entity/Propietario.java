package com.vetcare.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(
    name = "propietarios",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_propietario_dni", columnNames = "dni")
    }
)
public class Propietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 12, message = "El documento debe tener entre 8 y 12 caracteres")
    @Column(nullable = false, length = 12)
    private String dni;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 120, message = "Los nombres no pueden superar los 120 caracteres")
    @Column(nullable = false, length = 120)
    private String nombres;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    @Column(nullable = false, length = 20)
    private String telefono;

    @Email(message = "El correo electrónico no tiene un formato válido")
    @Size(max = 120, message = "El correo no puede superar los 120 caracteres")
    @Column(length = 120)
    private String correo;

    public Propietario() {
    }

    public Propietario(
            Long id,
            String dni,
            String nombres,
            String telefono,
            String correo) {
        this.id = id;
        this.dni = dni;
        this.nombres = nombres;
        this.telefono = telefono;
        this.correo = correo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}