package com.vetcare.backend.controller;

import com.vetcare.backend.dto.MascotaRequest;
import com.vetcare.backend.dto.MascotaResponse;
import com.vetcare.backend.service.MascotaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(
            MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @GetMapping
    public List<MascotaResponse> listar() {
        return mascotaService.listar();
    }

    @GetMapping("/{id}")
    public MascotaResponse obtenerPorId(
            @PathVariable Long id) {
        return mascotaService.obtenerPorId(id);
    }

    @GetMapping("/propietario/{propietarioId}")
    public List<MascotaResponse> listarPorPropietario(
            @PathVariable Long propietarioId) {

        return mascotaService
                .listarPorPropietario(propietarioId);
    }

    @PostMapping
    public ResponseEntity<MascotaResponse> crear(
            @Valid @RequestBody MascotaRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mascotaService.crear(request));
    }

    @PutMapping("/{id}")
    public MascotaResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MascotaRequest request) {

        return mascotaService.actualizar(id, request);
    }
}