package com.hakanburger.pedidos.dto;

import java.math.BigDecimal;

/** Igual al tipo OpcionTamano del frontend. */
public record OpcionTamanoDto(
        Long id,
        String nombre,
        BigDecimal precioExtra
) {
}
