package com.neksbyte.porelgustoweb;

import com.neksbyte.porelgustoweb.dto.CheckoutForm;
import com.neksbyte.porelgustoweb.dto.CompraEnProceso;
import com.neksbyte.porelgustoweb.dto.FiltroCatalogo;
import com.neksbyte.porelgustoweb.dto.ResumenCompra;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.model.EstadoPedido;
import com.neksbyte.porelgustoweb.model.FormaPago;
import com.neksbyte.porelgustoweb.model.Pedido;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.service.*;
import com.neksbyte.porelgustoweb.service.pago.ResultadoPago;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Reglas principales de la tienda con los datos de ejemplo (data.sql) en H2. */
@SpringBootTest(properties = "app.envio.geocodificador.activo=false")
@ActiveProfiles("h2")
@Transactional
class TiendaFlujoTests {
    @Autowired EnvioService envios;
    @Autowired com.neksbyte.porelgustoweb.repository.UsuarioRepository usuarioRepo;
    @Autowired org.springframework.security.crypto.password.PasswordEncoder encoder;

    /** Contraseña temporal solo para la prueba (la transacción se deshace al terminar). */
    private static final String CLAVE = "clave-de-prueba-123";

    @org.junit.jupiter.api.BeforeEach
    void contrasenasTemporales() {
        for (String correo : List.of("admin@porelgusto.mx", "cliente@porelgusto.mx")) {
            Usuario u = usuarioRepo.findByCorreoElectronicoIgnoreCase(correo).orElseThrow();
            u.setPassword(encoder.encode(CLAVE));
        }
    }
    @Autowired com.neksbyte.porelgustoweb.config.Cifrador cifrador;
    @Autowired ArregloFloralService catalogo;
    @Autowired CarritoDeCompraService carritos;
    @Autowired PedidoService pedidos;
    @Autowired UsuarioService usuarios;

    @Test
    void catalogoAplicaPromocionYFiltros() {
        // PG-005 está en la colección Primavera (20% de descuento).
        ArregloFloral p = catalogo.porSku("PG-005");
        assertEquals(0, new BigDecimal("1360").compareTo(catalogo.vista(p).getPrecioFinal()));
        FiltroCatalogo f = new FiltroCatalogo();
        f.setQ("orquidea");
        assertFalse(catalogo.buscar(f).isEmpty(), "el buscador ignora acentos");
        f = new FiltroCatalogo();
        f.setDisponibles(true);
        assertTrue(catalogo.buscar(f).stream().allMatch(v -> v.getProducto().isDisponible()));
    }

    @Test
    void cuponesYEnvioEnElResumen() {
        CarritoSesion carrito = new CarritoSesion();
        carritos.agregar(carrito, catalogo.porSku("PG-004").getIdArregloFloral(), 1, null, null); // $1,200
        ResumenCompra r = carritos.resumen(carrito);
        assertFalse(r.isEnvioCalculado(), "sin dirección el envío queda por calcular");
        assertThrows(ReglaNegocioException.class, () -> carritos.aplicarCupon(carrito, "VERANO25"), "cupón vencido");
        carritos.aplicarCupon(carrito, "bienvenida10");
        r = carritos.resumen(carrito);
        assertEquals(0, new BigDecimal("120").compareTo(r.getDescuento()));
        assertEquals(0, new BigDecimal("1080").compareTo(r.getTotal()));
        // Sin mapa (desactivado en las pruebas) la dirección no se ubica y se cobra la tarifa máxima.
        CheckoutForm dir = new CheckoutForm();
        dir.setCalle("Calle 1");
        dir.setCodigoPostal("02830");
        r = carritos.resumen(carrito, dir);
        assertTrue(r.isEnvioCalculado());
        assertEquals(0, new BigDecimal("200").compareTo(r.getEnvio()));
        assertEquals(0, new BigDecimal("1280").compareTo(r.getTotal()));
    }

    @Test
    void compraApartaInventarioYAvanzaPorEtapas() {
        ArregloFloral p = catalogo.porSku("PG-004");
        int existencias = p.getExistencias();
        CarritoSesion carrito = new CarritoSesion();
        carritos.agregar(carrito, p.getIdArregloFloral(), 2, null, "Sin lirios");
        carritos.asignarEntrega(carrito, carritos.fechasDisponibles().get(1), CarritoDeCompraService.HORARIOS.get(0));

        CheckoutForm datos = new CheckoutForm();
        datos.setNombreCliente("Prueba");
        datos.setCorreoCliente("prueba@ejemplo.com");
        datos.setTelefonoCliente("5512345678");
        datos.setDestinatario("Quien recibe");
        datos.setCalle("Calle 1");
        datos.setCiudad("CDMX");
        datos.setCodigoPostal("01000");
        datos.setDedicatoria("¡Felicidades!");
        CompraEnProceso compra = new CompraEnProceso();
        compra.setDatos(datos);
        compra.setForma(FormaPago.TARJETA);
        compra.setLineas(new ArrayList<>(carrito.getLineas()));
        compra.setResumen(carritos.resumen(carrito));
        compra.setFechaEntrega(carrito.getFechaEntrega());
        compra.setHorarioEntrega(carrito.getHorarioEntrega());

        Pedido o = pedidos.registrarCompra(compra, null, ResultadoPago.aprobado("SIM-TEST", "Simulado"));
        assertEquals(EstadoPedido.PENDIENTE, o.getEstado());
        assertEquals(existencias - 2, catalogo.porSku("PG-004").getExistencias());
        assertNotNull(o.getTicket());
        assertTrue(o.isConDedicatoria());
        assertEquals(1, o.getItems().size());

        Usuario admin = usuarios.autenticar("admin@porelgusto.mx", CLAVE);
        assertThrows(ReglaNegocioException.class,
                () -> pedidos.actualizarEstado(o.getFolio(), EstadoPedido.ENTREGADO, admin, null, null, null, null), "no se salta etapas");
        pedidos.actualizarEstado(o.getFolio(), EstadoPedido.PREPARANDO, admin, null, null, null, null);
        pedidos.actualizarEstado(o.getFolio(), EstadoPedido.EN_CAMINO, admin, null, "Repartidor", null, null);
        assertNotNull(o.getNumeroGuia());
        pedidos.actualizarEstado(o.getFolio(), EstadoPedido.CANCELADO, admin, "Prueba", null, null, null);
        assertEquals(existencias, catalogo.porSku("PG-004").getExistencias(), "cancelar regresa el inventario");
        assertEquals("REEMBOLSADO", o.getEstadoPago());
        assertEquals(List.of("Pendiente", "Preparando", "En camino", "Cancelado"),
                o.getHistorial().stream().map(h -> h.getEstado()).toList());
    }

    @Test
    void inicioDeSesionConCorreoONombre() {
        assertEquals("Ana López", usuarios.autenticar("CLIENTE@porelgusto.mx", CLAVE).getNombre());
        assertEquals("Elena Aldana", usuarios.autenticar("Elena Aldana", CLAVE).getNombre());
        assertThrows(ReglaNegocioException.class, () -> usuarios.autenticar("cliente@porelgusto.mx", "otra"));
        // Las contraseñas de prueba antiguas (sin cifrar) no permiten entrar.
        assertThrows(ReglaNegocioException.class, () -> usuarios.autenticar("ana.lopez@email.com", "hash_ana123"));
    }

    @Test
    void envioPorDistanciaDesdeLaFlorería() {
        BigDecimal poco = new BigDecimal("900");
        BigDecimal mucho = new BigDecimal("1500");
        assertEquals(0, new BigDecimal("100").compareTo(envios.calcular(4.0, true, poco).costo()));
        assertEquals(0, new BigDecimal("100").compareTo(envios.calcular(10.0, true, poco).costo()));
        assertEquals(0, new BigDecimal("150").compareTo(envios.calcular(12.5, true, poco).costo()));
        assertEquals(0, new BigDecimal("200").compareTo(envios.calcular(19.9, true, poco).costo()));
        assertEquals(0, BigDecimal.ZERO.compareTo(envios.calcular(19.9, true, mucho).costo()), "gratis desde $1,500 en la CDMX");
        assertEquals(0, new BigDecimal("200").compareTo(envios.calcular(25.0, true, mucho).costo()), "más de 20 km: no aplica el gratis");
        assertEquals(0, new BigDecimal("200").compareTo(envios.calcular(8.0, false, mucho).costo()), "fuera de la CDMX: no aplica el gratis");
        assertTrue(EnvioService.esCdmx("02830"));
        assertFalse(EnvioService.esCdmx("53100"), "Naucalpan (Estado de México)");
        // San Bernabé (Azcapotzalco) → Zócalo: unos 7 km en línea recta.
        double km = EnvioService.distanciaKm(19.4688808, -99.1729382, 19.4326, -99.1332);
        assertTrue(km > 5 && km < 7, "distancia " + km);
    }

    @Test
    void productoDePruebaOcultoYSinEnvio() {
        ArregloFloral prueba = catalogo.porSku("PG-000");
        assertTrue(prueba.isPrueba() && prueba.isDisponible());
        FiltroCatalogo f = new FiltroCatalogo();
        f.setQ("prueba");
        assertTrue(catalogo.buscar(f).isEmpty(), "no aparece en el buscador");
        CarritoSesion carrito = new CarritoSesion();
        carritos.agregar(carrito, prueba.getIdArregloFloral(), 1, null, null);
        CheckoutForm dir = new CheckoutForm();
        dir.setCodigoPostal("02830");
        ResumenCompra r = carritos.resumen(carrito, dir);
        assertEquals(0, BigDecimal.ZERO.compareTo(r.getEnvio()));
        assertEquals(0, BigDecimal.TEN.compareTo(r.getTotal()));
    }

    @Test
    void llavesDePagoCifradas() {
        String c = cifrador.cifrar("sk_live_123456789012345");
        assertNotEquals("sk_live_123456789012345", c);
        assertEquals("sk_live_123456789012345", cifrador.descifrar(c));
        assertNull(cifrador.descifrar("basura"));
        assertEquals("sk_live_••••2345", ConfiguracionService.enmascarar("sk_live_123456789012345"));
    }
}
