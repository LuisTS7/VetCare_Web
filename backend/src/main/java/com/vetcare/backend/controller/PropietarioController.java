package com.vetcare.backend.controller;

import com.vetcare.backend.entity.Propietario;
import com.vetcare.backend.service.PropietarioService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/propietarios")
public class PropietarioController {

    private final PropietarioService propietarioService;

    public PropietarioController(
            PropietarioService propietarioService) {
        this.propietarioService = propietarioService;
    }

    @GetMapping
    public List<Propietario> listar() {
        return propietarioService.listar();
    }

    @GetMapping("/{id}")
    public Propietario obtenerPorId(
            @PathVariable Long id) {
        return propietarioService.obtenerPorId(id);
    }

    @GetMapping("/dni/{dni}")
    public Propietario obtenerPorDni(
            @PathVariable String dni) {
        return propietarioService.obtenerPorDni(dni);
    }

    @PostMapping
    public ResponseEntity<Propietario> crear(
            @Valid @RequestBody Propietario propietario) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(propietarioService.crear(propietario));
    }

    @PutMapping("/{id}")
    public Propietario actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Propietario propietario) {

        return propietarioService.actualizar(
                id,
                propietario
        );
    }
}