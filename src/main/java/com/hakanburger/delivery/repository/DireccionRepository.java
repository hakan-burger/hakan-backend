package com.hakanburger.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hakanburger.delivery.model.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {

    /** "Mis direcciones": solo las que el cliente decidió guardar. */
    List<Direccion> findByUsuarioIdAndGuardadaTrueOrderById(Long usuarioId);

    Optional<Direccion> findByIdAndUsuarioId(Long id, Long usuarioId);

    boolean existsByUsuarioIdAndGuardadaTrue(Long usuarioId);
}
