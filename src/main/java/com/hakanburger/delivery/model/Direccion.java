package com.hakanburger.delivery.model;

import java.math.BigDecimal;

import com.hakanburger.pedidos.model.Usuario;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "direccion")
@Getter @Setter @NoArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_zona")
    private Zona zona;

    /** "Casa (principal)", "Trabajo"... */
    @Column(nullable = false, length = 40)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(nullable = false, length = 60)
    private String distrito;

    @Column(length = 150)
    private String referencia;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitud;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitud;

    @Column(nullable = false)
    private boolean principal;

    /** false = se usó solo para un pedido; no aparece en "Mis direcciones". */
    @Column(nullable = false)
    private boolean guardada = true;
}
