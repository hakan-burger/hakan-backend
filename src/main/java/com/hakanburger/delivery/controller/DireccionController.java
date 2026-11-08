package com.hakanburger.delivery.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.hakanburger.delivery.dto.DireccionDto;
import com.hakanburger.delivery.dto.ZonaDto;
import com.hakanburger.delivery.service.DireccionService;

@RestController
@RequestMapping("/api")
public class DireccionController {

    private final DireccionService servicio;

    public DireccionController(DireccionService servicio) {
        this.servicio = servicio;
    }

    /** GET /api/direcciones  (pantalla 09: elegir dirección) */
    @GetMapping("/direcciones")
    public List<DireccionDto> listar() {
        return servicio.listar();
    }

    /** POST /api/direcciones  (pantalla 10: nueva dirección) */
    @PostMapping("/direcciones")
    @ResponseStatus(HttpStatus.CREATED)
    public DireccionDto registrar(@Valid @RequestBody DireccionDto datos) {
        return servicio.registrar(datos);
    }

    /** GET /api/zonas  (zonas de reparto con su tarifa) */
    @GetMapping("/zonas")
    public List<ZonaDto> zonas() {
        return servicio.listarZonas();
    }
}
