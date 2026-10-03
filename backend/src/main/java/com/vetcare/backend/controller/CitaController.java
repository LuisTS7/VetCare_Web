package com.vetcare.backend.controller;

import com.vetcare.backend.dto.CitaRequest;
import com.vetcare.backend.dto.CitaResponse;
import com.vetcare.backend.service.CitaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public List<CitaResponse> listar() {
        return citaService.listar();
    }

    @GetMapping("/{id}")
    public CitaResponse obtenerPorId(
            @PathVariable Long id) {
        return citaService.obtenerPorId(id);
    }

    @GetMapping("/fecha/{fecha}")
    public List<CitaResponse> listarPorFecha(
            @PathVariable LocalDate fecha) {

        return citaService.listarPorFecha(fecha);
    }

    @GetMapping("/mascota/{mascotaId}")
    public List<CitaResponse> listarPorMascota(
            @PathVariable Long mascotaId) {

        return citaService.listarPorMascota(mascotaId);
    }

    @GetMapping("/veterinario/{veterinarioId}/fecha/{fecha}")
    public List<CitaResponse> listarPorVeterinarioYFecha(
            @PathVariable Long veterinarioId,
            @PathVariable LocalDate fecha) {

        return citaService.listarPorVeterinarioYFecha(
                veterinarioId,
                fecha
        );
    }

    @PostMapping
    public ResponseEntity<CitaResponse> crear(
            @Valid @RequestBody CitaRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(citaService.crear(request));
    }

    @PutMapping("/{id}")
    public CitaResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CitaRequest request) {

        return citaService.actualizar(id, request);
    }

    @PatchMapping("/{id}/estado")
    public CitaResponse cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        return citaService.cambiarEstado(
                id,
                body.get("estado")
        );
    }
}