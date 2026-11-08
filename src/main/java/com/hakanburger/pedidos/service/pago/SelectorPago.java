package com.hakanburger.pedidos.service.pago;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hakanburger.common.ReglaNegocioException;
import com.hakanburger.pedidos.model.MetodoPago;

/** Elige la estrategia según el método de pago. Spring le entrega todas las estrategias. */
@Component
public class SelectorPago {

    private final Map<MetodoPago, EstrategiaPago> estrategias = new EnumMap<>(MetodoPago.class);

    public SelectorPago(List<EstrategiaPago> lista) {
        lista.forEach(e -> estrategias.put(e.metodo(), e));
    }

    public EstrategiaPago para(MetodoPago metodo) {
        EstrategiaPago e = estrategias.get(metodo);
        if (e == null) {
            throw new ReglaNegocioException("Método de pago no disponible: " + metodo);
        }
        return e;
    }
}
