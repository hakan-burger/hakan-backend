package com.hakanburger.delivery.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/** Igual al tipo Direccion del frontend. */
public record DireccionDto(
        Long id,
        @NotBlank(message = "Ponle un nombre a la dirección") @Size(max = 40) String nombre,
        @NotBlank(message = "Ingresa la dirección") @Size(max = 150) String direccion,
        @NotBlank(message = "Elige el distrito") @Size(max = 60) String distrito,
        @Size(max = 150) String referencia,
        @NotNull(message = "Falta la zona de reparto") @Valid ZonaDto zona
) {
}
