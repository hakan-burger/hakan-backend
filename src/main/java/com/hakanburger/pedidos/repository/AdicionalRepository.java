package com.hakanburger.pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hakanburger.pedidos.model.Adicional;

public interface AdicionalRepository extends JpaRepository<Adicional, Long> {
}
