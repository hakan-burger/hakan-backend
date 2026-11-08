package com.hakanburger.pedidos.dto;

import java.math.BigDecimal;
import java.util.List;

/** Igual al tipo Producto del frontend (shared/types). */
public record ProductoDto(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Long categoriaId,
        String imagenUrl,
        Boolean disponible,
        String etiqueta,
        List<OpcionTamanoDto> tamanos,
        List<String> ingredientesQuitables,
        List<AdicionalDto> adicionales) {

    /** Al registrar un pedido solo se usa el id; el precio se toma de la base de datos. */
    public static ProductoDto soloId(Long id) {
        return new ProductoDto(id, null, null, null, null, null, true, null, null, null, null);
    }
}
