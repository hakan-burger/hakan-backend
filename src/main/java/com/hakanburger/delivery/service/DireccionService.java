package com.hakanburger.delivery.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hakanburger.common.ReglaNegocioException;
import com.hakanburger.delivery.dto.DeliveryMapper;
import com.hakanburger.delivery.dto.DireccionDto;
import com.hakanburger.delivery.dto.ZonaDto;
import com.hakanburger.delivery.model.Direccion;
import com.hakanburger.delivery.model.Zona;
import com.hakanburger.delivery.repository.DireccionRepository;
import com.hakanburger.delivery.repository.ZonaRepository;
import com.hakanburger.pedidos.model.Usuario;
import com.hakanburger.pedidos.service.SesionService;

/** Direcciones del cliente y zonas de reparto (pantallas 09, 10 y 21). */
@Service
@Transactional(readOnly = true)
public class DireccionService {

    private final DireccionRepository direcciones;
    private final ZonaRepository zonas;
    private final SesionService sesion;

    public DireccionService(DireccionRepository direcciones, ZonaRepository zonas, SesionService sesion) {
        this.direcciones = direcciones;
        this.zonas = zonas;
        this.sesion = sesion;
    }

    public List<ZonaDto> listarZonas() {
        return zonas.findAllByOrderByCodigoAsc().stream().map(DeliveryMapper::zona).toList();
    }

    /** "Mis direcciones" del cliente que inició sesión. */
    public List<DireccionDto> listar() {
        return direcciones.findByUsuarioIdAndGuardadaTrueOrderById(sesion.clienteActual().getId()).stream()
                .map(DeliveryMapper::direccion).toList();
    }

    @Transactional
    public DireccionDto registrar(DireccionDto datos) {
        return DeliveryMapper.direccion(guardar(sesion.clienteActual(), datos, true));
    }

    /**
     * Dirección para un pedido: si es una de "Mis direcciones" se usa esa;
     * si es nueva y el cliente no quiso guardarla, se registra solo para el pedido.
     */
    @Transactional
    public Direccion paraPedido(Usuario cliente, DireccionDto datos) {
        if (datos.id() != null) {
            var guardada = direcciones.findByIdAndUsuarioId(datos.id(), cliente.getId());
            if (guardada.isPresent()) {
                return guardada.get();
            }
        }
        return guardar(cliente, datos, false);
    }

    @Transactional
    public Direccion guardar(Usuario cliente, DireccionDto datos, boolean enMisDirecciones) {
        Zona zona = zonas.findByCodigo(datos.zona().codigo())
                .orElseThrow(() -> new ReglaNegocioException("Por ahora no llegamos a esa zona"));
        Direccion d = new Direccion();
        d.setUsuario(cliente);
        d.setZona(zona);
        d.setNombre(datos.nombre().trim());
        d.setDireccion(datos.direccion().trim());
        d.setDistrito(datos.distrito().trim());
        d.setReferencia(datos.referencia() == null || datos.referencia().isBlank() ? null : datos.referencia().trim());
        d.setGuardada(enMisDirecciones);
        d.setPrincipal(enMisDirecciones && !direcciones.existsByUsuarioIdAndGuardadaTrue(cliente.getId()));
        return direcciones.save(d);
    }
}
