package com.hakanburger.pedidos.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.hakanburger.common.ReglaNegocioException;
import com.hakanburger.pedidos.dto.DatosRucDto;

/**
 * Consulta de RUC para la factura (pantalla 12).
 * Por ahora es una consulta simulada con RUC de prueba; más adelante se reemplaza
 * por la llamada al servicio de consulta RUC de SUNAT sin cambiar el controlador.
 */
@Service
public class SunatService {

    private static final Map<String, DatosRucDto> RUC_PRUEBA = Map.of(
            "20601234567", new DatosRucDto("20601234567", "INVERSIONES ANDINAS S.A.C.",
                    "Av. Separadora Industrial 320, Ate", "ACTIVO", "HABIDO"),
            "10481391143", new DatosRucDto("10481391143", "ADAUTO CARBAJAL MIGUEL GUSTAVO",
                    "Av. Julio C. Tello 203, Santa Clara, Ate", "ACTIVO", "HABIDO"));

    public DatosRucDto consultar(String ruc) {
        if (ruc == null || !ruc.matches("(10|15|17|20)\\d{9}")) {
            throw new ReglaNegocioException("El RUC tiene 11 dígitos y empieza con 10, 15, 17 o 20");
        }
        return RUC_PRUEBA.getOrDefault(ruc,
                new DatosRucDto(ruc, "EMPRESA " + ruc.substring(7) + " S.A.C.", "Lima", "ACTIVO", "HABIDO"));
    }
}
