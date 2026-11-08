package com.hakanburger.pedidos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pago")
@Getter @Setter @NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", unique = true)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MetodoPago metodo;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal monto;

    /** Yape/Plin. */
    @Column(length = 20)
    private String numeroOperacion;

    /** Efectivo: con cuánto paga el cliente. */
    @Column(precision = 8, scale = 2)
    private BigDecimal montoEntregado;

    @Column(precision = 8, scale = 2)
    private BigDecimal vuelto;

    /** POS. */
    @Enumerated(EnumType.STRING)
    @Column(length = 7)
    private TipoTarjeta tipoTarjeta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_verifica")
    private Usuario usuarioVerifica;

    private LocalDateTime fechaVerificacion;
}
