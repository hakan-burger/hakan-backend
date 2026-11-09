package com.hakanburger.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hakanburger.delivery.dto.DireccionDto;
import com.hakanburger.delivery.dto.ZonaDto;
import com.hakanburger.delivery.model.Direccion;
import com.hakanburger.delivery.model.Zona;
import com.hakanburger.delivery.repository.ZonaRepository;
import com.hakanburger.delivery.service.DireccionService;
import com.hakanburger.pedidos.dto.*;
import com.hakanburger.pedidos.model.*;
import com.hakanburger.pedidos.repository.*;
import com.hakanburger.pedidos.service.PedidoService;
import com.hakanburger.pedidos.service.SesionService;

/**
 * Datos de prueba que se cargan al iniciar la aplicación (solo si la base de datos está vacía).
 * Son los mismos del frontend (shared/data/mock.ts) y del Figma, para que las pantallas
 * muestren lo mismo con VITE_USAR_MOCK=true o con el backend.
 */
@Component
public class DatosIniciales implements CommandLineRunner {

    /** Fotos servidas por el frontend (hakan-frontend/public/img/productos). */
    private static final Map<String, String> FOTOS = Map.ofEntries(
            Map.entry("Hakan Royal", "hakan-royal"), Map.entry("Clásica", "clasica"),
            Map.entry("Hawaiana", "hawaiana"), Map.entry("Hamburguesa BBQ", "hamburguesa-bbq"),
            Map.entry("Pollo Crispy", "pollo-crispy"), Map.entry("Alitas BBQ (6 und.)", "alitas-bbq"),
            Map.entry("Salchipapa Hakan", "salchipapa"), Map.entry("Shawarma de pollo", "shawarma-pollo"),
            Map.entry("Limonada Frozen", "limonada-frozen"), Map.entry("Chicha morada 1 L", "chicha-morada"),
            Map.entry("Gaseosa 500 ml", "gaseosa"));

    private final RolRepository roles;
    private final UsuarioRepository usuarios;
    private final CategoriaRepository categorias;
    private final AdicionalRepository adicionales;
    private final ProductoRepository productos;
    private final ZonaRepository zonas;
    private final DireccionService direcciones;
    private final PedidoService pedidos;

    public DatosIniciales(RolRepository roles, UsuarioRepository usuarios, CategoriaRepository categorias,
            AdicionalRepository adicionales, ProductoRepository productos, ZonaRepository zonas,
            DireccionService direcciones, PedidoService pedidos) {
        this.roles = roles;
        this.usuarios = usuarios;
        this.categorias = categorias;
        this.adicionales = adicionales;
        this.productos = productos;
        this.zonas = zonas;
        this.direcciones = direcciones;
        this.pedidos = pedidos;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (categorias.count() > 0) {
            return;
        }

        // ---------- Roles y usuarios ----------
        Rol cliente = roles.save(new Rol("CLIENTE"));
        Rol atencion = roles.save(new Rol("ATENCION"));
        Rol cocina = roles.save(new Rol("COCINA"));
        roles.save(new Rol("REPARTIDOR"));
        roles.save(new Rol("ADMINISTRADOR"));

        Usuario juan = usuarios.save(new Usuario(cliente, "Juan Pérez Quispe", SesionService.CORREO_CLIENTE, "987654321"));
        Usuario ana = usuarios.save(new Usuario(cliente, "Ana Torres Ríos", "ana@gmail.com", "945112870"));
        Usuario rosa = usuarios.save(new Usuario(cliente, "Rosa Huamán", "rosa@gmail.com", "912345678"));
        Usuario andinas = usuarios.save(new Usuario(cliente, "Inversiones Andinas SAC", "compras@andinas.pe", "999888777"));
        usuarios.save(new Usuario(atencion, "Atención Hakan Burger", SesionService.CORREO_ATENCION, "900000001"));
        usuarios.save(new Usuario(cocina, "Cocina Hakan Burger", "cocina@hakanburger.pe", "900000002"));

        // ---------- Carta ----------
        Categoria hamburguesas = categorias.save(new Categoria("Hamburguesas", 1));
        Categoria alitas = categorias.save(new Categoria("Alitas", 2));
        Categoria salchipapas = categorias.save(new Categoria("Salchipapas", 3));
        Categoria shawarmas = categorias.save(new Categoria("Shawarmas", 4));
        Categoria bebidas = categorias.save(new Categoria("Bebidas", 5));
        categorias.save(new Categoria("Extras", 6));

        List<Adicional> extrasHamburguesa = adicionales.saveAll(List.of(
                new Adicional("Queso cheddar", "3.00"),
                new Adicional("Tocino", "4.00"),
                new Adicional("Huevo frito", "2.50")));

        hamburguesa(hamburguesas, extrasHamburguesa, "Hakan Royal", "Doble carne, huevo, plátano, queso y jamón", "27.90", "Más pedida", true);
        hamburguesa(hamburguesas, extrasHamburguesa, "Clásica", "Carne 100% res, lechuga, tomate y queso", "21.90", null, true);
        hamburguesa(hamburguesas, extrasHamburguesa, "Hawaiana", "Carne, piña a la plancha, jamón y queso", "23.90", null, true);
        hamburguesa(hamburguesas, extrasHamburguesa, "Hamburguesa BBQ",
                "Carne 100% res, tocino crocante, queso cheddar y salsa BBQ de la casa", "24.90", "Nuevo", true);
        hamburguesa(hamburguesas, extrasHamburguesa, "Pollo Crispy", "Filete de pollo crocante y coleslaw", "22.90", null, false);
        simple(alitas, "Alitas BBQ (6 und.)", "Con papas y crema a elección", "22.90", null);
        simple(salchipapas, "Salchipapa Hakan", "Papas nativas, hot dog y cremas", "18.90", null);
        simple(shawarmas, "Shawarma de pollo", "Pollo, verduras y salsa de ajo", "19.90", null);
        simple(bebidas, "Limonada Frozen", "Vaso de 500 ml", "8.00", "Promo 2x1");
        simple(bebidas, "Chicha morada 1 L", "Hecha en casa", "9.00", null);
        simple(bebidas, "Gaseosa 500 ml", "Inca Kola o Coca-Cola", "4.50", null);

        // ---------- Zonas de reparto ----------
        Zona a = zonas.save(new Zona("A", "Santa Clara (hasta 2 km)", "5.00", "30 a 55 min"));
        Zona b = zonas.save(new Zona("B", "Ate", "6.00", "35 a 60 min"));
        zonas.save(new Zona("C", "Huachipa", "7.00", "40 a 60 min"));
        zonas.save(new Zona("D", "Ñaña / La Molina", "9.00", "40 a 60 min"));

        // ---------- Direcciones ----------
        direcciones.guardar(juan, direccion("Casa (principal)", "Jr. Las Retamas 158", "Santa Clara, Ate", "frente al parque", a), true);
        direcciones.guardar(juan, direccion("Trabajo", "Av. Nicolás Ayllón 4770", "Ate", null, b), true);
        Direccion casaAna = direcciones.guardar(ana, direccion("Casa", "Av. Las Torres 512", "Ate", null, b), true);
        Direccion oficina = direcciones.guardar(andinas, direccion("Oficina", "Av. Separadora Industrial 320", "Ate", null, b), true);

        // ---------- Pedidos para el panel de atención y cocina (empiezan en #1047) ----------
        pedidos.crear(new NuevoPedidoRequest(
                List.of(item(4, 1), item(6, 1), item(10, 1)),
                new EntregaDto(Modalidad.DELIVERY, conId(casaAna), "945112870"),
                new ComprobanteDto(TipoComprobante.BOLETA, "70845123", "Ana Torres Ríos", null, "ana@gmail.com"),
                new PagoDto(MetodoPago.YAPE_PLIN, "02346001", null, null)),
                ana, LocalDateTime.of(2026, 11, 5, 19, 12));
        pedidos.crear(new NuevoPedidoRequest(
                List.of(item(1, 1), item(11, 1)),
                new EntregaDto(Modalidad.RECOJO, null, "912345678"),
                new ComprobanteDto(TipoComprobante.BOLETA, "41234567", "Rosa Huamán", null, "rosa@gmail.com"),
                new PagoDto(MetodoPago.EFECTIVO, null, new BigDecimal("40.00"), null)),
                rosa, LocalDateTime.of(2026, 11, 5, 19, 14));
        pedidos.crear(new NuevoPedidoRequest(
                List.of(item(2, 2), item(7, 1)),
                new EntregaDto(Modalidad.DELIVERY, conId(oficina), "999888777"),
                new ComprobanteDto(TipoComprobante.FACTURA, "20601234567", "Inversiones Andinas S.A.C.",
                        "Av. Separadora Industrial 320, Ate", "compras@andinas.pe"),
                new PagoDto(MetodoPago.POS, null, null, TipoTarjeta.DEBITO)),
                andinas, LocalDateTime.of(2026, 11, 5, 19, 15));
    }

    private void hamburguesa(Categoria categoria, List<Adicional> extras, String nombre, String descripcion,
            String precio, String etiqueta, boolean disponible) {
        Producto p = new Producto(categoria, nombre, descripcion, precio);
        p.setEtiqueta(etiqueta);
        p.setImagenUrl(foto(nombre));
        p.setDestacado("Más pedida".equals(etiqueta));
        p.setDisponible(disponible);
        p.getOpciones().add(new OpcionProducto(p, "Individual", "0.00", true));
        p.getOpciones().add(new OpcionProducto(p, "Doble carne", "6.00", false));
        p.getAdicionales().addAll(extras);
        p.getIngredientesQuitables().addAll(List.of("Sin cebolla", "Sin tomate", "Sin lechuga"));
        productos.save(p);
    }

    private void simple(Categoria categoria, String nombre, String descripcion, String precio, String etiqueta) {
        Producto p = new Producto(categoria, nombre, descripcion, precio);
        p.setEtiqueta(etiqueta);
        p.setImagenUrl(foto(nombre));
        productos.save(p);
    }

    private static String foto(String nombre) {
        String archivo = FOTOS.get(nombre);
        return archivo == null ? null : "/img/productos/" + archivo + ".webp";
    }

    private static DireccionDto direccion(String nombre, String direccion, String distrito, String referencia, Zona zona) {
        return new DireccionDto(null, nombre, direccion, distrito, referencia, new ZonaDto(zona.getCodigo(), null, null, null));
    }

    private static DireccionDto conId(Direccion d) {
        return new DireccionDto(d.getId(), d.getNombre(), d.getDireccion(), d.getDistrito(), d.getReferencia(),
                new ZonaDto(d.getZona().getCodigo(), null, null, null));
    }

    private static ItemCarritoDto item(long productoId, int cantidad) {
        return new ItemCarritoDto(null, ProductoDto.soloId(productoId), cantidad, null, List.of(), List.of(), null);
    }
}
