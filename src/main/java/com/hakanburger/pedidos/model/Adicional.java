package com.hakanburger.pedidos.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Extra que se puede agregar a un producto: queso cheddar, tocino... */
@Entity
@Table(name = "adicional")
@Getter @Setter @NoArgsConstructor
public class Adicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adicional")
    private Long id;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private boolean disponible = true;

    public Adicional(String nombre, String precio) {
        this.nombre = nombre;
        this.precio = new BigDecimal(precio);
    }
}
