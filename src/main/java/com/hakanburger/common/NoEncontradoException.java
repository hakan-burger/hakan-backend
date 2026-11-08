package com.hakanburger.common;

/** Se lanza cuando no existe el registro buscado. Responde 404. */
public class NoEncontradoException extends RuntimeException {

    public NoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
