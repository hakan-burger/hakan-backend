package com.hakanburger.delivery.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

/** Igual al tipo Zona del frontend. */
public record ZonaDto(
        @NotBlank(message = "Falta la zona de reparto") String codigo,
        String nombre,
        BigDecimal tarifa,
        String tiempo
) {
}
