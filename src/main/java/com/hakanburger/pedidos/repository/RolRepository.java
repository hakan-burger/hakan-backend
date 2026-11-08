package com.hakanburger.pedidos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hakanburger.pedidos.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(String nombre);
}
