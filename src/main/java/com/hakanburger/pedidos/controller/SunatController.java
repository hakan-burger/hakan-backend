package com.hakanburger.pedidos.controller;

import org.springframework.web.bind.annotation.*;

import com.hakanburger.pedidos.dto.DatosRucDto;
import com.hakanburger.pedidos.service.SunatService;

@RestController
@RequestMapping("/api/sunat")
public class SunatController {

    private final SunatService sunat;

    public SunatController(SunatService sunat) {
        this.sunat = sunat;
    }

    /** GET /api/sunat/ruc/20601234567  (pantalla 12: factura) */
    @GetMapping("/ruc/{ruc}")
    public DatosRucDto consultar(@PathVariable String ruc) {
        return sunat.consultar(ruc);
    }
}
