package com.hakanburger.pedidos.service;

import org.springframework.stereotype.Service;

import com.hakanburger.common.NoEncontradoException;
import com.hakanburger.pedidos.model.Usuario;
import com.hakanburger.pedidos.repository.UsuarioRepository;

/**
 * Usuario que está usando el sistema.
 * Mientras no exista el inicio de sesión (historia de usuario de registro e ingreso),
 * se usan los usuarios de prueba cargados en config/DatosIniciales.
 */
@Service
public class SesionService {

    public static final String CORREO_CLIENTE = "juan.perez@gmail.com";
    public static final String CORREO_ATENCION = "atencion@hakanburger.pe";

    private final UsuarioRepository usuarios;

    public SesionService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    /** Cliente que hace pedidos desde la web. */
    public Usuario clienteActual() {
        return buscar(CORREO_CLIENTE);
    }

    /** Personal de atención que usa el panel del local. */
    public Usuario atencionActual() {
        return buscar(CORREO_ATENCION);
    }

    private Usuario buscar(String correo) {
        return usuarios.findByCorreo(correo)
                .orElseThrow(() -> new NoEncontradoException("No existe el usuario de prueba " + correo));
    }
}
