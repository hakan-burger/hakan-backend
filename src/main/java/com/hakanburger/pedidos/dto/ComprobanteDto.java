package com.hakanburger.pedidos.dto;

import jakarta.validation.constraints.*;

import com.hakanburger.pedidos.model.TipoComprobante;

/** Igual al tipo Comprobante del frontend. */
public record ComprobanteDto(
        @NotNull(message = "Elige boleta o factura") TipoComprobante tipo,
        @NotBlank(message = "Ingresa el DNI o RUC") String documento,
        @NotBlank(message = "Ingresa el nombre o la razón social") String nombreORazonSocial,
        String direccionFiscal,
        @NotBlank(message = "Ingresa el correo")
        @Email(message = "Ingresa un correo válido") String correo
) {
}
