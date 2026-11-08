package com.hakanburger.pedidos.dto;

/** Respuesta de la consulta de RUC (pantalla 12). */
public record DatosRucDto(
        String ruc,
        String razonSocial,
        String direccionFiscal,
        String estado,
        String condicion
) {
}
