package com.vetcare.backend.controller;

import com.vetcare.backend.dto.UsuarioRequest;
import com.vetcare.backend.dto.UsuarioResponse;
import com.vetcare.backend.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(
            UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtenerPorId(
            @PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @GetMapping("/veterinarios")
    public List<UsuarioResponse> listarVeterinarios() {
        return usuarioService.listarVeterinariosActivos();
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(
            @Valid @RequestBody UsuarioRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioService.crear(request));
    }

    @PutMapping("/{id}")
    public UsuarioResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request) {

        return usuarioService.actualizar(id, request);
    }
}