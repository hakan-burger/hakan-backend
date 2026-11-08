package com.hakanburger.common;

/** Se lanza cuando los datos no cumplen una regla del negocio. Responde 400. */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
