package com.hakanburger.pedidos.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

import com.hakanburger.pedidos.model.MetodoPago;
import com.hakanburger.pedidos.model.TipoTarjeta;

/** Igual al tipo Pago del frontend. Cada método usa solo sus campos. */
public record PagoDto(
        @NotNull(message = "Elige el método de pago") MetodoPago metodo,
        String numeroOperacion,
        BigDecimal montoConQuePaga,
        TipoTarjeta tipoTarjeta
) {
}
