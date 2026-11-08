package com.hakanburger.pedidos.service.pago;

import org.springframework.stereotype.Component;

import com.hakanburger.common.ReglaNegocioException;
import com.hakanburger.pedidos.dto.PagoDto;
import com.hakanburger.pedidos.model.EstadoPago;
import com.hakanburger.pedidos.model.MetodoPago;
import com.hakanburger.pedidos.model.Pago;

/** Yape/Plin: el cliente envía el número de operación y la atención lo verifica después. */
@Component
public class PagoYapePlin implements EstrategiaPago {

    @Override
    public MetodoPago metodo() {
        return MetodoPago.YAPE_PLIN;
    }

    @Override
    public void aplicar(Pago pago, PagoDto datos) {
        String operacion = datos.numeroOperacion() == null ? "" : datos.numeroOperacion().trim();
        if (!operacion.matches("\\d{6,20}")) {
            throw new ReglaNegocioException("Ingresa el número de operación de Yape/Plin (solo números)");
        }
        pago.setNumeroOperacion(operacion);
        pago.setEstado(EstadoPago.PENDIENTE);
    }
}
