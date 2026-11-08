package com.hakanburger.pedidos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Extra agregado a una línea del pedido, con el precio que tenía al comprar. */
@Entity
@Table(name = "detalle_pedido_adicional")
@IdClass(DetallePedidoAdicional.Clave.class)
@Getter @Setter @NoArgsConstructor
public class DetallePedidoAdicional {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_detalle_pedido")
    private DetallePedido detalle;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adicional")
    private Adicional adicional;

    @Column(nullable = false)
    private Short cantidad = 1;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal precio;

    public DetallePedidoAdicional(DetallePedido detalle, Adicional adicional) {
        this.detalle = detalle;
        this.adicional = adicional;
        this.precio = adicional.getPrecio();
    }

    /** Clave compuesta: (id_detalle_pedido, id_adicional). */
    @Getter @Setter @NoArgsConstructor
    public static class Clave implements Serializable {
        private Long detalle;
        private Long adicional;

        @Override
        public boolean equals(Object o) {
            return o instanceof Clave c && Objects.equals(detalle, c.detalle) && Objects.equals(adicional, c.adicional);
        }

        @Override
        public int hashCode() {
            return Objects.hash(detalle, adicional);
        }
    }
}
