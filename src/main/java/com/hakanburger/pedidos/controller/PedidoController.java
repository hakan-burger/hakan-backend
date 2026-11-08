package com.hakanburger.pedidos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.hakanburger.pedidos.dto.CambioEstadoRequest;
import com.hakanburger.pedidos.dto.NuevoPedidoRequest;
import com.hakanburger.pedidos.dto.PedidoDto;
import com.hakanburger.pedidos.model.EstadoPedido;
import com.hakanburger.pedidos.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService servicio;

    public PedidoController(PedidoService servicio) {
        this.servicio = servicio;
    }

    /** POST /api/pedidos  (pantalla 16: confirmar pedido) */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoDto registrar(@Valid @RequestBody NuevoPedidoRequest datos) {
        return servicio.registrar(datos);
    }

    /** GET /api/pedidos?estado=REGISTRADO  (pantallas 24 y 28) */
    @GetMapping
    public List<PedidoDto> listar(@RequestParam(required = false) EstadoPedido estado) {
        return servicio.listar(estado);
    }

    /** GET /api/pedidos/mios  (pantalla 21) */
    @GetMapping("/mios")
    public List<PedidoDto> misPedidos() {
        return servicio.misPedidos();
    }

    /** GET /api/pedidos/{id} */
    @GetMapping("/{id}")
    public PedidoDto obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    /** PATCH /api/pedidos/{id}/estado  body: { "estado": "EN_PREPARACION" } */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambioEstadoRequest datos) {
        servicio.cambiarEstado(id, datos.estado());
        return ResponseEntity.noContent().build();
    }

    /** PATCH /api/pedidos/{id}/verificar-pago  (pantalla 25) */
    @PatchMapping("/{id}/verificar-pago")
    public ResponseEntity<Void> verificarPago(@PathVariable Long id) {
        servicio.verificarPago(id);
        return ResponseEntity.noContent().build();
    }
}
