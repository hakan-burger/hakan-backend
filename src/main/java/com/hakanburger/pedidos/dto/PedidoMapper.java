package com.hakanburger.pedidos.dto;

import java.util.List;

import com.hakanburger.delivery.dto.DeliveryMapper;
import com.hakanburger.pedidos.model.*;

/**
 * Patrón DTO: convierte las entidades JPA en objetos simples con la misma forma
 * que los tipos del frontend (shared/types/index.ts). Así la API no expone las tablas.
 */
public final class PedidoMapper {

    private PedidoMapper() {
    }

    public static CategoriaDto categoria(Categoria c) {
        return new CategoriaDto(c.getId(), c.getNombre());
    }

    public static OpcionTamanoDto tamano(OpcionProducto o) {
        return new OpcionTamanoDto(o.getId(), o.getNombre(), o.getPrecioExtra());
    }

    public static AdicionalDto adicional(Adicional a) {
        return new AdicionalDto(a.getId(), a.getNombre(), a.getPrecio());
    }

    public static ProductoDto producto(Producto p) {
        return new ProductoDto(p.getId(), p.getNombre(), p.getDescripcion(), p.getPrecio(), p.getCategoria().getId(),
                p.getImagenUrl(), p.isDisponible(), p.getEtiqueta(),
                p.getOpciones().stream().map(PedidoMapper::tamano).toList(),
                List.copyOf(p.getIngredientesQuitables()),
                p.getAdicionales().stream().map(PedidoMapper::adicional).toList());
    }

    public static ItemCarritoDto item(DetallePedido d) {
        return new ItemCarritoDto(String.valueOf(d.getId()), producto(d.getProducto()), d.getCantidad().intValue(),
                d.getOpcion() == null ? null : tamano(d.getOpcion()),
                List.copyOf(d.getSinIngredientes()),
                // precio guardado al momento de la compra
                d.getAdicionales().stream()
                        .map(a -> new AdicionalDto(a.getAdicional().getId(), a.getAdicional().getNombre(), a.getPrecio()))
                        .toList(),
                d.getIndicaciones());
    }

    public static PedidoDto pedido(Pedido p) {
        Usuario c = p.getCliente();
        Comprobante cp = p.getComprobante();
        Pago pg = p.getPago();
        return new PedidoDto(
                p.getId(),
                p.getFechaRegistro(),
                new ClienteDto(c.getId(), c.getNombres(), c.getTelefono()),
                p.getDetalles().stream().map(PedidoMapper::item).toList(),
                new EntregaDto(p.getModalidad(),
                        p.getDireccion() == null ? null : DeliveryMapper.direccion(p.getDireccion()),
                        p.getCelularContacto()),
                new ComprobanteDto(cp.getTipo(), cp.getNumeroDocumento(), cp.getNombreRazonSocial(),
                        cp.getDireccionFiscal(), cp.getCorreo()),
                new PagoDto(pg.getMetodo(), pg.getNumeroOperacion(), pg.getMontoEntregado(), pg.getTipoTarjeta()),
                pg.getEstado() == EstadoPago.CONFIRMADO,
                new TotalesDto(p.getSubtotal(), p.getDescuento(), p.getCostoDelivery(), p.getTotal()),
                p.getEstado());
    }
}
