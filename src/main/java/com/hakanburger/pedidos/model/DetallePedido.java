package com.hakanburger.pedidos.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Una línea del pedido: producto, tamaño, cantidad, extras e indicaciones. */
@Entity
@Table(name = "detalle_pedido")
@Getter @Setter @NoArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_pedido")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    /** Tamaño elegido (puede no tener). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_opcion")
    private OpcionProducto opcion;

    @Column(nullable = false)
    private Short cantidad;

    /** Precio del producto + precio extra del tamaño, al momento de la compra. */
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal subtotal;

    @Column(length = 150)
    private String indicaciones;

    @OneToMany(mappedBy = "detalle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedidoAdicional> adicionales = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "detalle_pedido_sin_ingrediente", joinColumns = @JoinColumn(name = "id_detalle_pedido"))
    @Column(name = "ingrediente", nullable = false, length = 40)
    @OrderColumn(name = "orden")
    private List<String> sinIngredientes = new ArrayList<>();
}
