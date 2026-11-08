package com.hakanburger.pedidos.dto;

import jakarta.validation.constraints.NotNull;

/** PATCH /api/productos/{id}/disponibilidad */
public record CambioDisponibilidadRequest(
        @NotNull(message = "Indica si está disponible") Boolean disponible
) {
}
