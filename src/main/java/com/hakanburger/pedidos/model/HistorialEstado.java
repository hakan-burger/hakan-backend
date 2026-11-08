package com.hakanburger.pedidos.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Registro de cada cambio de estado del pedido. */
@Entity
@Table(name = "historial_estado")
@Getter @Setter @NoArgsConstructor
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedido estado;

    @Column(nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    public HistorialEstado(Pedido pedido, Usuario usuario, EstadoPedido estado) {
        this.pedido = pedido;
        this.usuario = usuario;
        this.estado = estado;
    }
}
