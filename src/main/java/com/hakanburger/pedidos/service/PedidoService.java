package com.hakanburger.pedidos.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hakanburger.common.NoEncontradoException;
import com.hakanburger.common.ReglaNegocioException;
import com.hakanburger.delivery.model.Direccion;
import com.hakanburger.delivery.service.DireccionService;
import com.hakanburger.pedidos.dto.*;
import com.hakanburger.pedidos.model.*;
import com.hakanburger.pedidos.repository.PedidoRepository;
import com.hakanburger.pedidos.service.pago.SelectorPago;

/** Registro y seguimiento de pedidos. Los totales siempre se calculan aquí, con los precios de la base de datos. */
@Service
@Transactional(readOnly = true)
public class PedidoService {

    private final PedidoRepository pedidos;
    private final CatalogoService catalogo;
    private final DireccionService direcciones;
    private final SelectorPago selectorPago;
    private final SesionService sesion;

    public PedidoService(PedidoRepository pedidos, CatalogoService catalogo, DireccionService direcciones,
            SelectorPago selectorPago, SesionService sesion) {
        this.pedidos = pedidos;
        this.catalogo = catalogo;
        this.direcciones = direcciones;
        this.selectorPago = selectorPago;
        this.sesion = sesion;
    }

    /** Pantalla 16: el cliente confirma y el pedido queda REGISTRADO. */
    @Transactional
    public PedidoDto registrar(NuevoPedidoRequest datos) {
        return PedidoMapper.pedido(crear(datos, sesion.clienteActual(), LocalDateTime.now()));
    }

    /** Arma el pedido completo. También lo usa config/DatosIniciales para los pedidos de prueba. */
    @Transactional
    public Pedido crear(NuevoPedidoRequest datos, Usuario cliente, LocalDateTime fecha) {
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFechaRegistro(fecha.truncatedTo(ChronoUnit.SECONDS));

        // 1. Entrega
        EntregaDto entrega = datos.entrega();
        pedido.setModalidad(entrega.modalidad());
        pedido.setCelularContacto(entrega.celularContacto());
        BigDecimal delivery = BigDecimal.ZERO;
        if (entrega.modalidad() == Modalidad.DELIVERY) {
            if (entrega.direccion() == null) {
                throw new ReglaNegocioException("Elige la dirección de entrega");
            }
            Direccion direccion = direcciones.paraPedido(cliente, entrega.direccion());
            pedido.setDireccion(direccion);
            delivery = direccion.getZona().getTarifa();
        }

        // 2. Productos
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal descuento = BigDecimal.ZERO;
        for (ItemCarritoDto item : datos.items()) {
            DetallePedido detalle = armarDetalle(item);
            pedido.agregarDetalle(detalle);
            subtotal = subtotal.add(detalle.getSubtotal());
            Producto producto = detalle.getProducto();
            if (producto.esPromo2x1()) {
                // por cada 2 unidades, una sale gratis
                descuento = descuento.add(producto.getPrecio().multiply(BigDecimal.valueOf(item.cantidad() / 2)));
            }
        }

        // 3. Totales
        pedido.setSubtotal(subtotal);
        pedido.setDescuento(descuento);
        pedido.setCostoDelivery(delivery);
        pedido.setTotal(subtotal.subtract(descuento).add(delivery));

        // 4. Comprobante
        pedido.asignarComprobante(armarComprobante(datos.comprobante()));

        // 5. Pago (patrón Strategy: cada método valida sus propios datos)
        Pago pago = new Pago();
        pago.setMetodo(datos.pago().metodo());
        pago.setMonto(pedido.getTotal());
        selectorPago.para(pago.getMetodo()).aplicar(pago, datos.pago());
        pedido.asignarPago(pago);

        pedido.cambiarEstado(EstadoPedido.REGISTRADO, cliente);
        pedidos.save(pedido);

        String serie = pedido.getComprobante().getTipo() == TipoComprobante.FACTURA ? "F001-" : "B001-";
        pedido.getComprobante().setSerieNumero(serie + String.format("%08d", pedido.getId()));
        return pedido;
    }

    private DetallePedido armarDetalle(ItemCarritoDto item) {
        Producto producto = catalogo.buscarProducto(item.producto().id());
        if (!producto.isDisponible()) {
            throw new ReglaNegocioException("\"" + producto.getNombre() + "\" ya no está disponible. Quítalo del carrito para continuar");
        }
        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(producto);
        detalle.setCantidad(item.cantidad().shortValue());

        BigDecimal unitario = producto.getPrecio();
        if (item.tamano() != null) {
            OpcionProducto opcion = producto.getOpciones().stream()
                    .filter(o -> o.getId().equals(item.tamano().id()))
                    .findFirst()
                    .orElseThrow(() -> new ReglaNegocioException("El tamaño elegido no corresponde a " + producto.getNombre()));
            detalle.setOpcion(opcion);
            unitario = unitario.add(opcion.getPrecioExtra());
        }
        detalle.setPrecioUnitario(unitario);

        BigDecimal extras = BigDecimal.ZERO;
        if (item.adicionales() != null) {
            for (AdicionalDto a : item.adicionales()) {
                Adicional adicional = producto.getAdicionales().stream()
                        .filter(x -> x.getId().equals(a.id()))
                        .findFirst()
                        .orElseThrow(() -> new ReglaNegocioException("El adicional elegido no corresponde a " + producto.getNombre()));
                detalle.getAdicionales().add(new DetallePedidoAdicional(detalle, adicional));
                extras = extras.add(adicional.getPrecio());
            }
        }
        if (item.sinIngredientes() != null) {
            for (String ingrediente : item.sinIngredientes()) {
                if (!producto.getIngredientesQuitables().contains(ingrediente)) {
                    throw new ReglaNegocioException("No se puede pedir \"" + ingrediente + "\" en " + producto.getNombre());
                }
                detalle.getSinIngredientes().add(ingrediente);
            }
        }
        String indicaciones = item.indicaciones() == null ? "" : item.indicaciones().trim();
        detalle.setIndicaciones(indicaciones.isEmpty() ? null : indicaciones);
        detalle.setSubtotal(unitario.add(extras).multiply(BigDecimal.valueOf(item.cantidad())));
        return detalle;
    }

    private Comprobante armarComprobante(ComprobanteDto datos) {
        String documento = datos.documento().trim();
        if (datos.tipo() == TipoComprobante.BOLETA && !documento.matches("\\d{8}")) {
            throw new ReglaNegocioException("El DNI tiene 8 dígitos");
        }
        if (datos.tipo() == TipoComprobante.FACTURA) {
            if (!documento.matches("(10|15|17|20)\\d{9}")) {
                throw new ReglaNegocioException("El RUC tiene 11 dígitos y empieza con 10, 15, 17 o 20");
            }
            if (datos.direccionFiscal() == null || datos.direccionFiscal().isBlank()) {
                throw new ReglaNegocioException("Falta la dirección fiscal");
            }
        }
        Comprobante c = new Comprobante();
        c.setTipo(datos.tipo());
        c.setNumeroDocumento(documento);
        c.setNombreRazonSocial(datos.nombreORazonSocial().trim());
        c.setDireccionFiscal(datos.tipo() == TipoComprobante.FACTURA ? datos.direccionFiscal().trim() : null);
        c.setCorreo(datos.correo().trim());
        return c;
    }

    /** Pantallas 24 y 28: lista de pedidos, filtrada por estado si se indica. */
    public List<PedidoDto> listar(EstadoPedido estado) {
        List<Pedido> lista = estado == null ? pedidos.findAllByOrderByIdDesc() : pedidos.findByEstadoOrderByIdDesc(estado);
        return lista.stream().map(PedidoMapper::pedido).toList();
    }

    public PedidoDto obtener(Long id) {
        return PedidoMapper.pedido(buscar(id));
    }

    /** Pantalla 21: pedidos del cliente que inició sesión. */
    public List<PedidoDto> misPedidos() {
        return pedidos.findByClienteIdOrderByIdDesc(sesion.clienteActual().getId()).stream().map(PedidoMapper::pedido).toList();
    }

    /** Pantalla 25: la atención confirma que el pago llegó. */
    @Transactional
    public void verificarPago(Long id) {
        Pago pago = buscar(id).getPago();
        pago.setEstado(EstadoPago.CONFIRMADO);
        pago.setUsuarioVerifica(sesion.atencionActual());
        pago.setFechaVerificacion(LocalDateTime.now());
    }

    /** Pantallas 25 y 28: enviar a cocina, marcar como listo, en camino, entregado o cancelado. */
    @Transactional
    public void cambiarEstado(Long id, EstadoPedido nuevo) {
        Pedido pedido = buscar(id);
        EstadoPedido actual = pedido.getEstado();
        if (actual == nuevo) {
            return;
        }
        if (actual == EstadoPedido.ENTREGADO || actual == EstadoPedido.CANCELADO) {
            throw new ReglaNegocioException("El pedido #" + id + " ya está " + actual.name().toLowerCase() + " y no se puede cambiar");
        }
        if (nuevo == EstadoPedido.EN_CAMINO && pedido.getModalidad() == Modalidad.RECOJO) {
            throw new ReglaNegocioException("El pedido #" + id + " es para recojo en tienda; no sale a reparto");
        }
        pedido.cambiarEstado(nuevo, sesion.atencionActual());
    }

    private Pedido buscar(Long id) {
        return pedidos.findById(id).orElseThrow(() -> new NoEncontradoException("No existe el pedido #" + id));
    }
}
