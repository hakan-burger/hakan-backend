package com.hakanburger.pedidos.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.hakanburger.pedidos.model.EstadoPedido;

/** Igual al tipo Pedido del frontend. */
public record PedidoDto(
        Long id,
        LocalDateTime fecha,
        ClienteDto cliente,
        List<ItemCarritoDto> items,
        EntregaDto entrega,
        ComprobanteDto comprobante,
        PagoDto pago,
        boolean pagoVerificado,
        TotalesDto totales,
        EstadoPedido estado,
        LocalDateTime fechaEstado // cuándo pasó al estado actual (pantalla 27: hora de salida a reparto)
) {
}
