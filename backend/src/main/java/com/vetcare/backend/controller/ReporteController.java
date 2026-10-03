package com.vetcare.backend.controller;

import com.vetcare.backend.service.ReporteService;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(
            ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/indicadores")
    public Map<String, Object> indicadores() {
        return reporteService.obtenerIndicadores();
    }
}