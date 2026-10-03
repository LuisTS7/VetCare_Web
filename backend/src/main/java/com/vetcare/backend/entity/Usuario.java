package com.vetcare.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(
    name = "usuarios",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_correo", columnNames = "correo")
    }
)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 120, message = "Los nombres no pueden superar los 120 caracteres")
    @Column(nullable = false, length = 120)
    private String nombres;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo electrónico no tiene un formato válido")
    @Size(max = 120, message = "El correo no puede superar los 120 caracteres")
    @Column(nullable = false, length = 120)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false, length = 100)
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Size(max = 40, message = "El rol no puede superar los 40 caracteres")
    @Column(nullable = false, length = 40)
    private String rol;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede superar los 20 caracteres")
    @Column(nullable = false, length = 20)
    private String estado;

    public Usuario() {
    }

    public Usuario(
            Long id,
            String nombres,
            String correo,
            String password,
            String rol,
            String estado) {
        this.id = id;
        this.nombres = nombres;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}