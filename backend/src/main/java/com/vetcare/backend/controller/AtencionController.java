package com.vetcare.backend.controller;

import com.vetcare.backend.dto.AtencionRequest;
import com.vetcare.backend.dto.AtencionResponse;
import com.vetcare.backend.service.AtencionService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atenciones")
public class AtencionController {

    private final AtencionService atencionService;

    public AtencionController(
            AtencionService atencionService) {
        this.atencionService = atencionService;
    }

    @GetMapping
    public List<AtencionResponse> listar() {
        return atencionService.listar();
    }

    @GetMapping("/{id}")
    public AtencionResponse obtenerPorId(
            @PathVariable Long id) {
        return atencionService.obtenerPorId(id);
    }

    @GetMapping("/mascota/{mascotaId}")
    public List<AtencionResponse> historialPorMascota(
            @PathVariable Long mascotaId) {

        return atencionService
                .historialPorMascota(mascotaId);
    }

    @GetMapping("/veterinario/{veterinarioId}")
    public List<AtencionResponse> listarPorVeterinario(
            @PathVariable Long veterinarioId) {

        return atencionService
                .listarPorVeterinario(veterinarioId);
    }

    @PostMapping
    public ResponseEntity<AtencionResponse> crear(
            @Valid @RequestBody AtencionRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(atencionService.crear(request));
    }

    @PutMapping("/{id}")
    public AtencionResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtencionRequest request) {

        return atencionService.actualizar(id, request);
    }
}