package com.hakanburger.pedidos.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "producto")
@Getter @Setter @NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @Column(nullable = false, length = 100)
    private String nombre;

    private String descripcion;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal precio;

    private String imagenUrl;

    /** Lo marca la atención cuando el producto se agota (pantalla 29). */
    @Column(nullable = false)
    private boolean disponible = true;

    @Column(nullable = false)
    private boolean destacado;

    /** Etiqueta que se muestra en la carta: "Más pedida", "Nuevo", "Promo 2x1". */
    @Column(length = 20)
    private String etiqueta;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<OpcionProducto> opciones = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "producto_adicional",
            joinColumns = @JoinColumn(name = "id_producto"),
            inverseJoinColumns = @JoinColumn(name = "id_adicional"))
    @OrderBy("id")
    private List<Adicional> adicionales = new ArrayList<>();

    /** Ingredientes que el cliente puede quitar: "Sin cebolla", "Sin tomate"... */
    @ElementCollection
    @CollectionTable(name = "producto_ingrediente", joinColumns = @JoinColumn(name = "id_producto"))
    @Column(name = "ingrediente", nullable = false, length = 40)
    @OrderColumn(name = "orden")
    private List<String> ingredientesQuitables = new ArrayList<>();

    public Producto(Categoria categoria, String nombre, String descripcion, String precio) {
        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = new BigDecimal(precio);
    }

    public boolean esPromo2x1() {
        return "Promo 2x1".equals(etiqueta);
    }
}
