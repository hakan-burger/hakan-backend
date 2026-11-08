package com.hakanburger.delivery.dto;

import com.hakanburger.delivery.model.Direccion;
import com.hakanburger.delivery.model.Zona;

/** Convierte las entidades de delivery en los DTO que recibe el frontend. */
public final class DeliveryMapper {

    private DeliveryMapper() {
    }

    public static ZonaDto zona(Zona z) {
        return new ZonaDto(z.getCodigo(), z.getNombre(), z.getTarifa(), z.getTiempoEstimado());
    }

    public static DireccionDto direccion(Direccion d) {
        return new DireccionDto(d.getId(), d.getNombre(), d.getDireccion(), d.getDistrito(), d.getReferencia(), zona(d.getZona()));
    }
}
