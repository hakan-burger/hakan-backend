package com.hakanburger.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hakanburger.delivery.model.Zona;

public interface ZonaRepository extends JpaRepository<Zona, Long> {

    Optional<Zona> findByCodigo(String codigo);

    List<Zona> findAllByOrderByCodigoAsc();
}
