package com.hakanburger.pedidos.dto;

import java.math.BigDecimal;

/** Igual al tipo Totales del frontend. */
public record TotalesDto(
        BigDecimal subtotal,
        BigDecimal descuento,
        BigDecimal delivery,
        BigDecimal total
) {
}
