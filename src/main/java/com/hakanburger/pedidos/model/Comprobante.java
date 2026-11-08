package com.hakanburger.pedidos.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comprobante")
@Getter @Setter @NoArgsConstructor
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comprobante")
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", unique = true)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private TipoComprobante tipo;

    /** DNI (boleta) o RUC (factura). */
    @Column(nullable = false, length = 11)
    private String numeroDocumento;

    @Column(nullable = false, length = 150)
    private String nombreRazonSocial;

    /** Solo factura. */
    @Column(length = 150)
    private String direccionFiscal;

    @Column(nullable = false, length = 120)
    private String correo;

    /** Ej.: B001-00001047. */
    @Column(unique = true, length = 13)
    private String serieNumero;

    @Column(nullable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();
}
