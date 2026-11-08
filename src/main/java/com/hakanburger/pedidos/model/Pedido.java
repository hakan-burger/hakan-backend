package com.hakanburger.pedidos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hakanburger.delivery.model.Direccion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pedido")
@Getter @Setter @NoArgsConstructor
public class Pedido {

    /** Los números de pedido empiezan en 1047, como en el Figma. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_seq")
    @SequenceGenerator(name = "pedido_seq", sequenceName = "pedido_id_pedido_seq", initialValue = 1047, allocationSize = 1)
    @Column(name = "id_pedido")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario cliente;

    /** Solo cuando la modalidad es DELIVERY. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Modalidad modalidad;

    @Column(nullable = false, length = 9)
    private String celularContacto;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal descuento;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal costoDelivery;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedido estado = EstadoPedido.REGISTRADO;

    @Column(length = 200)
    private String motivoRechazo;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<DetallePedido> detalles = new ArrayList<>();

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Comprobante comprobante;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pago pago;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fechaHora")
    private List<HistorialEstado> historial = new ArrayList<>();

    public void agregarDetalle(DetallePedido detalle) {
        detalle.setPedido(this);
        detalles.add(detalle);
    }

    public void asignarComprobante(Comprobante comprobante) {
        comprobante.setPedido(this);
        this.comprobante = comprobante;
    }

    public void asignarPago(Pago pago) {
        pago.setPedido(this);
        this.pago = pago;
    }

    /** Cambia el estado y deja registro de quién lo hizo y cuándo. */
    public void cambiarEstado(EstadoPedido nuevo, Usuario responsable) {
        this.estado = nuevo;
        historial.add(new HistorialEstado(this, responsable, nuevo));
    }
}
