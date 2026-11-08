package com.hakanburger.pedidos.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Cliente o personal del local (atención, cocina, repartidor), según su rol. */
@Entity
@Table(name = "usuario")
@Getter @Setter @NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol")
    private Rol rol;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(unique = true, length = 120)
    private String correo;

    @Column(unique = true, length = 9)
    private String telefono;

    private String contrasena;

    @Column(nullable = false, length = 10)
    private String metodoRegistro = "CORREO";

    @Column(nullable = false)
    private boolean activo = true;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    public Usuario(Rol rol, String nombres, String correo, String telefono) {
        this.rol = rol;
        this.nombres = nombres;
        this.correo = correo;
        this.telefono = telefono;
    }
}
