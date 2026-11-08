package com.hakanburger.pedidos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import com.hakanburger.delivery.dto.DireccionDto;
import com.hakanburger.pedidos.model.Modalidad;

/** Igual al tipo Entrega del frontend. */
public record EntregaDto(
        @NotNull(message = "Elige delivery o recojo en tienda") Modalidad modalidad,
        @Valid DireccionDto direccion,
        @NotNull(message = "Ingresa el celular de contacto")
        @Pattern(regexp = "^9\\d{8}$", message = "El celular tiene 9 dígitos y empieza con 9") String celularContacto
) {
}
