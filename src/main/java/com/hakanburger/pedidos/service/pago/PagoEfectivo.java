package com.hakanburger.pedidos.service.pago;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.hakanburger.common.ReglaNegocioException;
import com.hakanburger.pedidos.dto.PagoDto;
import com.hakanburger.pedidos.model.EstadoPago;
import com.hakanburger.pedidos.model.MetodoPago;
import com.hakanburger.pedidos.model.Pago;

/** Efectivo: se paga al recibir; se calcula el vuelto que debe llevar el repartidor. */
@Component
public class PagoEfectivo implements EstrategiaPago {

    @Override
    public MetodoPago metodo() {
        return MetodoPago.EFECTIVO;
    }

    @Override
    public void aplicar(Pago pago, PagoDto datos) {
        BigDecimal entrega = datos.montoConQuePaga();
        if (entrega == null || entrega.compareTo(pago.getMonto()) < 0) {
            throw new ReglaNegocioException("El monto con que pagas debe cubrir el total de S/ " + pago.getMonto());
        }
        pago.setMontoEntregado(entrega);
        pago.setVuelto(entrega.subtract(pago.getMonto()));
        pago.setEstado(EstadoPago.PENDIENTE);
    }
}
