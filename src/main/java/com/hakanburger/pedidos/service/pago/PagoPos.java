package com.hakanburger.pedidos.service.pago;

import org.springframework.stereotype.Component;

import com.hakanburger.common.ReglaNegocioException;
import com.hakanburger.pedidos.dto.PagoDto;
import com.hakanburger.pedidos.model.EstadoPago;
import com.hakanburger.pedidos.model.MetodoPago;
import com.hakanburger.pedidos.model.Pago;

/** POS: el repartidor lleva el equipo; se registra si la tarjeta es de débito o crédito. */
@Component
public class PagoPos implements EstrategiaPago {

    @Override
    public MetodoPago metodo() {
        return MetodoPago.POS;
    }

    @Override
    public void aplicar(Pago pago, PagoDto datos) {
        if (datos.tipoTarjeta() == null) {
            throw new ReglaNegocioException("Indica si pagarás con tarjeta de débito o crédito");
        }
        pago.setTipoTarjeta(datos.tipoTarjeta());
        pago.setEstado(EstadoPago.PENDIENTE);
    }
}
