package com.hakanburger.pedidos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.hakanburger.common.ReglaNegocioException;
import com.hakanburger.delivery.dto.DireccionDto;
import com.hakanburger.delivery.dto.ZonaDto;
import com.hakanburger.pedidos.dto.*;
import com.hakanburger.pedidos.model.*;
import com.hakanburger.pedidos.service.CatalogoService;
import com.hakanburger.pedidos.service.PedidoService;

@SpringBootTest
@Transactional
class PedidoServiceTests {

    @Autowired
    PedidoService pedidos;

    @Autowired
    CatalogoService catalogo;

    private static final ComprobanteDto BOLETA =
            new ComprobanteDto(TipoComprobante.BOLETA, "45678912", "Juan Pérez Quispe", null, "juan.perez@gmail.com");

    private static ItemCarritoDto item(long productoId, int cantidad, OpcionTamanoDto tamano, List<AdicionalDto> extras) {
        return new ItemCarritoDto("x", ProductoDto.soloId(productoId), cantidad, tamano, List.of(), extras, null);
    }

    @Test
    void calculaTotalesConTamanoAdicionalPromoYDelivery() {
        ProductoDto royal = catalogo.obtenerProducto(1L);           // S/ 27.90
        OpcionTamanoDto doble = royal.tamanos().get(1);              // + S/ 6.00
        AdicionalDto cheddar = royal.adicionales().get(0);           // + S/ 3.00
        DireccionDto casa = new DireccionDto(999999L, "Casa", "Jr. Prueba 123", "Santa Clara, Ate", null,
                new ZonaDto("A", null, null, null));                 // delivery S/ 5.00

        PedidoDto p = pedidos.registrar(new NuevoPedidoRequest(
                List.of(item(1, 1, doble, List.of(cheddar)), item(9, 2, null, List.of())),  // limonada 2x1: 2 x 8.00
                new EntregaDto(Modalidad.DELIVERY, casa, "987654321"),
                BOLETA,
                new PagoDto(MetodoPago.EFECTIVO, null, new BigDecimal("100"), null)));

        assertEquals(0, new BigDecimal("52.90").compareTo(p.totales().subtotal()));  // 36.90 + 16.00
        assertEquals(0, new BigDecimal("8.00").compareTo(p.totales().descuento()));
        assertEquals(0, new BigDecimal("5.00").compareTo(p.totales().delivery()));
        assertEquals(0, new BigDecimal("49.90").compareTo(p.totales().total()));
        assertEquals(EstadoPedido.REGISTRADO, p.estado());
    }

    @Test
    void rechazaEfectivoQueNoCubreElTotal() {
        NuevoPedidoRequest datos = new NuevoPedidoRequest(
                List.of(item(2, 1, null, List.of())),
                new EntregaDto(Modalidad.RECOJO, null, "987654321"),
                BOLETA,
                new PagoDto(MetodoPago.EFECTIVO, null, new BigDecimal("10"), null));
        assertThrows(ReglaNegocioException.class, () -> pedidos.registrar(datos));
    }

    @Test
    void rechazaProductoAgotado() {
        NuevoPedidoRequest datos = new NuevoPedidoRequest(
                List.of(item(5, 1, null, List.of())),                    // Pollo Crispy está agotado
                new EntregaDto(Modalidad.RECOJO, null, "987654321"),
                BOLETA,
                new PagoDto(MetodoPago.POS, null, null, TipoTarjeta.CREDITO));
        assertThrows(ReglaNegocioException.class, () -> pedidos.registrar(datos));
    }
}
