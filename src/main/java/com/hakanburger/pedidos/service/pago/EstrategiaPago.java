package com.hakanburger.pedidos.service.pago;

import com.hakanburger.pedidos.dto.PagoDto;
import com.hakanburger.pedidos.model.MetodoPago;
import com.hakanburger.pedidos.model.Pago;

/**
 * Patrón Strategy: cada método de pago valida y completa el pago a su manera.
 * Para agregar un método nuevo (por ejemplo, pago con tarjeta en línea) se crea otra
 * clase que implemente esta interfaz, sin tocar PedidoService.
 */
public interface EstrategiaPago {

    /** Método de pago que atiende esta estrategia. */
    MetodoPago metodo();

    /**
     * Valida los datos que envió el cliente y los copia en el pago.
     * El monto del pago (total del pedido) ya viene calculado.
     */
    void aplicar(Pago pago, PagoDto datos);
}
