package com.hakanburger.pedidos.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Tamaño o variante de un producto: "Individual", "Doble carne" (+ S/ 6). */
@Entity
@Table(name = "opcion_producto")
@Getter @Setter @NoArgsConstructor
public class OpcionProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal precioExtra;

    @Column(nullable = false)
    private boolean porDefecto;

    public OpcionProducto(Producto producto, String nombre, String precioExtra, boolean porDefecto) {
        this.producto = producto;
        this.nombre = nombre;
        this.precioExtra = new BigDecimal(precioExtra);
        this.porDefecto = porDefecto;
    }
}
