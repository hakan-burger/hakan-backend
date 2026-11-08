package com.hakanburger.pedidos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hakanburger.common.NoEncontradoException;
import com.hakanburger.pedidos.dto.CategoriaDto;
import com.hakanburger.pedidos.dto.PedidoMapper;
import com.hakanburger.pedidos.dto.ProductoDto;
import com.hakanburger.pedidos.model.Producto;
import com.hakanburger.pedidos.repository.CategoriaRepository;
import com.hakanburger.pedidos.repository.ProductoRepository;

/** Carta del local: categorías, productos y su disponibilidad. */
@Service
@Transactional(readOnly = true)
public class CatalogoService {

    private final CategoriaRepository categorias;
    private final ProductoRepository productos;

    public CatalogoService(CategoriaRepository categorias, ProductoRepository productos) {
        this.categorias = categorias;
        this.productos = productos;
    }

    public List<CategoriaDto> listarCategorias() {
        return categorias.findByActivaTrueOrderByOrdenAsc().stream().map(PedidoMapper::categoria).toList();
    }

    public List<ProductoDto> listarProductos(Long categoriaId, String buscar) {
        String patron = "%" + (buscar == null ? "" : buscar.trim().toLowerCase()) + "%";
        return productos.buscar(categoriaId, patron).stream().map(PedidoMapper::producto).toList();
    }

    public ProductoDto obtenerProducto(Long id) {
        return PedidoMapper.producto(buscarProducto(id));
    }

    /** Pantalla 29: la atención marca un producto como disponible o agotado. */
    @Transactional
    public void cambiarDisponibilidad(Long id, boolean disponible) {
        buscarProducto(id).setDisponible(disponible);
    }

    /** Pantallas 04 y 17: devuelve los productos del carrito que ya no están disponibles. */
    public List<ProductoDto> verificarDisponibilidad(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return productos.findByIdInAndDisponibleFalseOrderById(ids).stream().map(PedidoMapper::producto).toList();
    }

    Producto buscarProducto(Long id) {
        return productos.findById(id).orElseThrow(() -> new NoEncontradoException("No existe el producto " + id));
    }
}
