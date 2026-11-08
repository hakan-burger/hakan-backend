package com.hakanburger.pedidos.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/** Lo que envía el frontend al confirmar el pedido (POST /api/pedidos). */
public record NuevoPedidoRequest(
        @NotEmpty(message = "El carrito está vacío") List<@Valid ItemCarritoDto> items,
        @NotNull(message = "Faltan los datos de entrega") @Valid EntregaDto entrega,
        @NotNull(message = "Faltan los datos del comprobante") @Valid ComprobanteDto comprobante,
        @NotNull(message = "Faltan los datos del pago") @Valid PagoDto pago
) {
}
