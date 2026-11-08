package com.hakanburger.pedidos.dto;

import java.math.BigDecimal;

/** Igual al tipo Adicional del frontend. */
public record AdicionalDto(
        Long id,
        String nombre,
        BigDecimal precio
) {
}
