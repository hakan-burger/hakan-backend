package com.hakanburger.pedidos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hakanburger.pedidos.dto.CategoriaDto;
import com.hakanburger.pedidos.service.CatalogoService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CatalogoService catalogo;

    public CategoriaController(CatalogoService catalogo) {
        this.catalogo = catalogo;
    }

    /** GET /api/categorias */
    @GetMapping
    public List<CategoriaDto> listar() {
        return catalogo.listarCategorias();
    }
}
