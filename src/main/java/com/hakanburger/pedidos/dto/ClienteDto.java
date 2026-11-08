package com.hakanburger.pedidos.dto;

/** Datos del cliente que se muestran en el pedido. */
public record ClienteDto(
        Long id,
        String nombre,
        String celular
) {
}
