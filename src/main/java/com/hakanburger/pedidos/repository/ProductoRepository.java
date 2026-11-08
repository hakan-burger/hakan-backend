package com.hakanburger.pedidos.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hakanburger.pedidos.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /** Carta: filtra por categoría (opcional) y por texto en el nombre. */
    @Query("""
            select p from Producto p
            where (:categoria is null or p.categoria.id = :categoria)
              and lower(p.nombre) like :patron
            order by p.id""")
    List<Producto> buscar(@Param("categoria") Long categoria, @Param("patron") String patron);

    /** Productos del carrito que ya se agotaron. */
    List<Producto> findByIdInAndDisponibleFalseOrderById(Collection<Long> ids);
}
