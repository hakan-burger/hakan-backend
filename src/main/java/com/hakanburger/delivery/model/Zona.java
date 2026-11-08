package com.hakanburger.delivery.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Zona de reparto: define la tarifa y el tiempo estimado del delivery. */
@Entity
@Table(name = "zona")
@Getter @Setter @NoArgsConstructor
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona")
    private Long id;

    @Column(nullable = false, unique = true, length = 1)
    private String codigo;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal tarifa;

    @Column(nullable = false, length = 20)
    private String tiempoEstimado;

    public Zona(String codigo, String nombre, String tarifa, String tiempoEstimado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tarifa = new BigDecimal(tarifa);
        this.tiempoEstimado = tiempoEstimado;
    }
}
