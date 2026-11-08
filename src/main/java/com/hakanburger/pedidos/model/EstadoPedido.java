package com.hakanburger.pedidos.model;

/** Estados del pedido, en el orden en que avanza. */
public enum EstadoPedido {
    REGISTRADO, EN_PREPARACION, LISTO, EN_CAMINO, ENTREGADO, CANCELADO
}
