package com.hakanburger.pedidos.repository;

import java.util.List;

import com.hakanburger.pedidos.model.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hakanburger.pedidos.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByOrderByIdDesc();

    List<Pedido> findByEstadoOrderByIdDesc(EstadoPedido estado);

    List<Pedido> findByClienteIdOrderByIdDesc(Long clienteId);
}
