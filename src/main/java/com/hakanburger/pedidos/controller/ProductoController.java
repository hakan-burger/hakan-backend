package com.hakanburger.pedidos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.hakanburger.pedidos.dto.CambioDisponibilidadRequest;
import com.hakanburger.pedidos.dto.ProductoDto;
import com.hakanburger.pedidos.service.CatalogoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final CatalogoService catalogo;

    public ProductoController(CatalogoService catalogo) {
        this.catalogo = catalogo;
    }

    /** GET /api/productos?categoria=1&buscar=royal  (pantalla 01: carta) */
    @GetMapping
    public List<ProductoDto> listar(@RequestParam(required = false) Long categoria,
            @RequestParam(required = false) String buscar) {
        return catalogo.listarProductos(categoria, buscar);
    }

    /** GET /api/productos/{id}  (pantalla 02: ficha del producto) */
    @GetMapping("/{id}")
    public ProductoDto obtener(@PathVariable Long id) {
        return catalogo.obtenerProducto(id);
    }

    /** PATCH /api/productos/{id}/disponibilidad  body: { "disponible": false } */
    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<Void> cambiarDisponibilidad(@PathVariable Long id,
            @Valid @RequestBody CambioDisponibilidadRequest datos) {
        catalogo.cambiarDisponibilidad(id, datos.disponible());
        return ResponseEntity.noContent().build();
    }

    /** POST /api/productos/verificar-disponibilidad  body: [1, 5, 9]  (carrito) */
    @PostMapping("/verificar-disponibilidad")
    public List<ProductoDto> verificarDisponibilidad(@RequestBody List<Long> ids) {
        return catalogo.verificarDisponibilidad(ids);
    }
}
