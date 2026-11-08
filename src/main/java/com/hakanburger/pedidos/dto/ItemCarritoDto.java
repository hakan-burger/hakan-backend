package com.hakanburger.pedidos.dto;

import java.util.List;

import jakarta.validation.constraints.*;

/** Una línea del carrito. Igual al tipo ItemCarrito del frontend. */
public record ItemCarritoDto(
        String idLinea,
        @NotNull(message = "Falta el producto") ProductoDto producto,
        @Min(value = 1, message = "La cantidad mínima es 1")
        @Max(value = 20, message = "La cantidad máxima por producto es 20")
        @NotNull(message = "Falta la cantidad") Integer cantidad,
        OpcionTamanoDto tamano,
        List<String> sinIngredientes,
        List<AdicionalDto> adicionales,
        @Size(max = 150, message = "Las indicaciones tienen como máximo 150 caracteres") String indicaciones) {
}
