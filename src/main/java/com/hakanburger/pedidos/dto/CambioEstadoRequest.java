package com.hakanburger.pedidos.dto;

import jakarta.validation.constraints.NotNull;

import com.hakanburger.pedidos.model.EstadoPedido;

/** PATCH /api/pedidos/{id}/estado */
public record CambioEstadoRequest(
        @NotNull(message = "Indica el nuevo estado") EstadoPedido estado
) {
}
