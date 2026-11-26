package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.config.TiendaConfig;
import com.neksbyte.porelgustoweb.dao.*;
import com.neksbyte.porelgustoweb.dto.CheckoutForm;
import com.neksbyte.porelgustoweb.dto.CompraEnProceso;
import com.neksbyte.porelgustoweb.dto.FacturaForm;
import com.neksbyte.porelgustoweb.dto.LineaCarrito;
import com.neksbyte.porelgustoweb.exception.NoEncontradoException;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.*;
import com.neksbyte.porelgustoweb.service.*;
import com.neksbyte.porelgustoweb.service.pago.PagoService;
import com.neksbyte.porelgustoweb.service.pago.ResultadoPago;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
public class PedidoServiceImpl extends BaseServiceImpl<Pedido, Integer> implements PedidoService {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String APROBADO = "APROBADO";
    private static final String PENDIENTE = "PENDIENTE";

    private final PedidoDao pedidos;
    private final ArregloFloralDao arreglos;
    private final MetodoPagoDao metodosPago;
    private final TicketDao tickets;
    private final FacturaDao facturas;
    private final UsuarioDao usuarios;
    private final CuponDao cuponDao;
    private final CarritoDeCompraService carritos;
    private final CuponService cupones;
    private final NotificacionService notificaciones;
    private final CorreoService correo;
    private final PagoService pagos;
    private final TiendaConfig tienda;
    private final SecureRandom random = new SecureRandom();

    public PedidoServiceImpl(PedidoDao dao, ArregloFloralDao arreglos, MetodoPagoDao metodosPago, TicketDao tickets,
                             FacturaDao facturas, UsuarioDao usuarios, CuponDao cuponDao, CarritoDeCompraService carritos,
                             CuponService cupones, NotificacionService notificaciones, CorreoService correo,
                             PagoService pagos, TiendaConfig tienda) {
        super(dao);
        this.pedidos = dao;
        this.arreglos = arreglos;
        this.metodosPago = metodosPago;
        this.tickets = tickets;
        this.facturas = facturas;
        this.usuarios = usuarios;
        this.cuponDao = cuponDao;
        this.carritos = carritos;
        this.cupones = cupones;
        this.notificaciones = notificaciones;
        this.correo = correo;
        this.pagos = pagos;
        this.tienda = tienda;
    }

    // ======================= Compra =======================

    @Override
    public Pedido registrarCompra(CompraEnProceso compra, Usuario usuario, ResultadoPago resultado) {
        if (resultado == null || !resultado.isAprobado()) throw new ReglaNegocioException("Pago rechazado");
        CheckoutForm d = compra.getDatos();

        // Apartar inventario de cada producto.
        for (LineaCarrito l : compra.getLineas()) {
            ArregloFloral p = arreglos.findById(l.getIdArreglo())
                    .orElseThrow(() -> new ReglaNegocioException(l.getNombre() + " ya no existe"));
            if (!p.isActivo() || !p.reservar(l.getCantidad())) {
                throw new ReglaNegocioException("Ya no hay suficientes piezas de " + p.getNombre());
            }
            p.setVentas(p.getVentas() + l.getCantidad());
        }
        CarritoDeCompra carrito = carritos.registrar(compra, usuario);

        Pedido o = new Pedido();
        o.setFolio(nuevoFolio());
        o.setFechaCreacion(LocalDate.now());
        o.setFechaHora(LocalDateTime.now());
        o.setCliente(usuario);
        o.setNombreCliente(d.getNombreCliente().trim());
        o.setCorreoCliente(d.getCorreoCliente().trim().toLowerCase(Locale.ROOT));
        o.setTelefonoCliente(d.getTelefonoCliente());
        o.setDestinatario(d.getDestinatario().trim());
        o.setTelefonoDestinatario(Textos.limpio(d.getTelefonoDestinatario()));
        o.setDireccionEntrega(d.direccionCompleta());
        o.setInstruccionesEspeciales(Textos.limpio(d.getReferencias()));
        o.setFechaEntrega(compra.getFechaEntrega());
        o.setHorarioEntrega(compra.getHorarioEntrega());
        o.setDistanciaKm(compra.getResumen().getEnvioKm());
        o.setCarritoDeCompra(carrito);
        o.setSubtotal(compra.getResumen().getSubtotal());
        o.setDescuento(compra.getResumen().getDescuento());
        o.setCostoEnvio(compra.getResumen().getEnvio());
        o.setIva(compra.getResumen().getIva());
        o.setTotal(compra.getResumen().getTotal());
        if (compra.getResumen().getCupon() != null) {
            cuponDao.findByCodigo(compra.getResumen().getCupon()).ifPresent(c -> {
                o.setCupon(c);
                cupones.registrarUso(c);
            });
        }

        // Pago y método de pago usado.
        o.setFormaPago(compra.getForma());
        MetodoPago mp = new MetodoPago();
        mp.setUsuario(usuario);
        mp.setTipoPago(compra.getForma().getEtiqueta());
        o.setEstadoPago(APROBADO);
        o.setProveedorPago(resultado.getProveedor());
        o.setTransaccion(resultado.getTransaccion());
        o.setFechaPago(LocalDateTime.now());
        mp.setDescripcion(resultado.getDescripcion() != null ? resultado.getDescripcion()
                : compra.getForma().getEtiqueta() + " (" + resultado.getProveedor() + ")");
        o.setMetodoPago(metodosPago.save(mp));

        // Dedicatoria con QR (si algún producto lo permite y se escribió una).
        if (!Textos.vacio(d.getDedicatoria()) && compra.getLineas().stream().anyMatch(LineaCarrito::isPermiteQR)) {
            o.setDedicatoria(d.getDedicatoria().trim());
            o.setTokenQr(UUID.randomUUID().toString().replace("-", ""));
        }
        o.cambiarEstado(EstadoPedido.PENDIENTE, "Pago aprobado", usuario == null ? o.getNombreCliente() : usuario.getNombre());
        pedidos.save(o);
        generarTicket(o);

        String resumen = "Folio: " + o.getFolio() + "\nTotal: $" + o.getTotal().stripTrailingZeros().toPlainString()
                + "\nEntrega: " + (o.getFechaEntrega() == null ? "" : o.getFechaEntrega().format(FECHA)) + " de " + o.getHorarioEntrega()
                + "\nPara: " + o.getDestinatario() + ", " + o.getDireccionEntrega();
        correo.enviar(o.getCorreoCliente(), "Recibimos tu pedido " + o.getFolio(), "¡Gracias por tu compra!\n\n" + resumen);
        notificaciones.notificar(usuario, "Pedido " + o.getFolio() + " registrado",
                "Ya lo estamos preparando.", "/pedido/" + o.getFolio(), false);
        notificaciones.notificarAdmins("Nuevo pedido " + o.getFolio(),
                o.getNombreCliente() + " · $" + o.getTotal().stripTrailingZeros().toPlainString(), "/admin/pedidos/" + o.getFolio());
        return o;
    }

    private String nuevoFolio() {
        String folio;
        do {
            folio = "PG" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + (1000 + random.nextInt(9000));
        } while (pedidos.findByFolio(folio).isPresent());
        return folio;
    }

    /** Ticket de venta (comprobante) cuando el pago queda aprobado. */
    private void generarTicket(Pedido o) {
        if (o.getTicket() != null) return;
        Ticket t = new Ticket();
        t.setFolioVenta("TK-" + o.getFolio());
        t.setFecha(LocalDate.now());
        t.setHora(LocalTime.now().withNano(0));
        t.setTotal(o.getTotal());
        t.setPedido(o);
        tickets.save(t);
        o.setTicket(t);
    }

    // ======================= Consulta =======================

    @Override
    @Transactional(readOnly = true)
    public Pedido porFolio(String folio) {
        String f = folio == null ? "" : folio.trim().toUpperCase(Locale.ROOT);
        return pedidos.findByFolio(f)
                .or(() -> f.matches("PG\\d{1,6}") ? pedidos.findById(Integer.parseInt(f.substring(2))).filter(p -> p.getFolio().equals(f)) : java.util.Optional.empty())
                .orElseThrow(() -> new NoEncontradoException("No hay pedidos con el folio " + folio));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> deCliente(Usuario u) { return pedidos.findByCliente(u); }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> todos(EstadoPedido filtro) {
        List<Pedido> lista = pedidos.findAllRecientes();
        return filtro == null ? lista : lista.stream().filter(o -> o.getEstado() == filtro).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> asignadosA(Usuario empleado) { return pedidos.findByEmpleado(empleado); }

    @Override
    @Transactional(readOnly = true)
    public Pedido porTokenQr(String token) {
        return pedidos.findByTokenQr(token == null ? "" : token).orElseThrow(() -> new NoEncontradoException("El código QR no es válido"));
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido porGuia(String guia) {
        return pedidos.findByNumeroGuia(guia).orElseThrow(() -> new NoEncontradoException("No existe el envío " + guia));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean puedeVer(Pedido o, Usuario u) {
        if (u == null) return false;
        if (u.isPersonal()) return true;
        if (o.getCliente() != null && o.getCliente().equals(u)) return true;
        return o.getCarritoDeCompra() != null && u.equals(o.getCarritoDeCompra().getUsuario());
    }

    // ======================= Etapas =======================

    @Override
    public void actualizarEstado(String folio, EstadoPedido nuevo, Usuario por, String nota, String repartidor, String telefono, String recibio) {
        Pedido o = porFolio(folio);
        validarPermiso(o, por);
        if (nuevo == EstadoPedido.CANCELADO) {
            cancelar(o, por, nota);
            return;
        }
        EstadoPedido permitido = o.getEstado().siguiente();
        if (nuevo != permitido) {
            throw new ReglaNegocioException("Cambio no permitido. Desde «" + o.getEstado().getEtiqueta() + "» solo se puede pasar a «"
                    + (permitido == null ? "ninguna etapa" : permitido.getEtiqueta()) + "» o «Cancelado».");
        }
        if (o.getEstado() == EstadoPedido.PENDIENTE_PAGO) {
            o.setEstadoPago(APROBADO);
            o.setFechaPago(LocalDateTime.now());
            generarTicket(o);
            o.cambiarEstado(EstadoPedido.PENDIENTE, Textos.vacio(nota) ? "Pago confirmado" : nota, por.getNombre());
            avisarCambio(o);
            return;
        }
        if (nuevo == EstadoPedido.EN_CAMINO) despachar(o, repartidor, telefono);
        if (nuevo == EstadoPedido.ENTREGADO) completarEntrega(o, recibio, false);
        o.cambiarEstado(nuevo, nota, por.getNombre());
        avisarCambio(o);
    }

    /** El pedido sale a ruta: número de guía, repartidor y hora estimada (fin del horario elegido). */
    private void despachar(Pedido o, String repartidor, String telefono) {
        if (o.getNumeroGuia() == null) o.setNumeroGuia("TRK" + (100000 + random.nextInt(900000)));
        o.setRepartidor(Textos.vacio(repartidor) ? "Repartidor Por el Gusto" : repartidor.trim());
        o.setTelefonoRepartidor(Textos.limpio(telefono));
        LocalDateTime eta = LocalDateTime.now().plusHours(3);
        if (o.getFechaEntrega() != null && o.getHorarioEntrega() != null && o.getHorarioEntrega().length() >= 10) {
            eta = o.getFechaEntrega().atTime(Integer.parseInt(o.getHorarioEntrega().substring(8, 10)), 0);
        }
        o.setHoraEstimada(eta);
    }

    private void completarEntrega(Pedido o, String recibio, boolean conQr) {
        if (o.getNumeroGuia() == null) despachar(o, null, null);
        o.setRecibio(Textos.vacio(recibio) ? o.getDestinatario() : recibio.trim());
        o.setFechaEntregado(LocalDateTime.now());
        o.setEntregaVerificadaQR(conQr);
    }

    private void cancelar(Pedido o, Usuario por, String motivo) {
        if (o.getEstado().esFinal()) throw new ReglaNegocioException("El pedido ya está " + o.getEstado().getEtiqueta().toLowerCase());
        for (ContenidoCarrito i : o.getItems()) {
            ArregloFloral p = i.getArregloFloral();
            if (p != null && i.getCantidad() != null) {
                p.liberar(i.getCantidad());
                p.setVentas(Math.max(0, p.getVentas() - i.getCantidad()));
            }
        }
        if (APROBADO.equals(o.getEstadoPago()) && o.getFormaPago() != null && o.getFormaPago() != FormaPago.WHATSAPP) {
            boolean ok = pagos.estrategia(o.getFormaPago()).reembolsar(o.getTransaccion(), o.getTotal());
            o.setEstadoPago(ok ? "REEMBOLSADO" : "REEMBOLSO_PENDIENTE");
        }
        o.cambiarEstado(EstadoPedido.CANCELADO, Textos.vacio(motivo) ? "Pedido cancelado" : motivo, por.getNombre());
        avisarCambio(o);
    }

    @Override
    public void reportarFallo(String folio, Usuario por, String descripcion) {
        if (Textos.vacio(descripcion)) throw new ReglaNegocioException("Describe qué falló");
        Pedido o = porFolio(folio);
        validarPermiso(o, por);
        o.anotar("Incidencia: " + descripcion.trim(), por.getNombre());
        String texto = "Tuvimos un problema con tu pedido " + o.getFolio() + ": " + descripcion.trim() + ". Te contactaremos para resolverlo.";
        correo.enviar(o.getCorreoCliente(), "Aviso sobre tu pedido " + o.getFolio(), texto);
        notificaciones.notificar(clienteDe(o), "Aviso sobre tu pedido " + o.getFolio(), texto, "/pedido/" + o.getFolio(), false);
        notificaciones.notificarAdmins("Incidencia en pedido " + o.getFolio(), descripcion.trim() + " (reportó " + por.getNombre() + ")",
                "/admin/pedidos/" + o.getFolio());
    }

    @Override
    public void asignar(String folio, Integer idEmpleado, Usuario por) {
        if (!por.isAdmin()) throw new ReglaNegocioException("Solo un administrador puede asignar pedidos");
        Pedido o = porFolio(folio);
        Usuario emp = idEmpleado == null ? null : usuarios.findById(idEmpleado).filter(Usuario::isPersonal)
                .orElseThrow(() -> new ReglaNegocioException("Elige un empleado válido"));
        o.setEmpleado(emp);
        if (emp != null) {
            notificaciones.notificar(emp, "Te asignaron el pedido " + o.getFolio(),
                    "Entrega el " + (o.getFechaEntrega() == null ? "" : o.getFechaEntrega().format(FECHA)) + " de " + o.getHorarioEntrega(),
                    "/admin/pedidos/" + o.getFolio(), false);
        }
    }

    @Override
    public Pedido verificarEntregaQR(String token, String recibio, Usuario por) {
        Pedido o = porTokenQr(token);
        validarPermiso(o, por);
        if (o.getEstado() != EstadoPedido.EN_CAMINO) throw new ReglaNegocioException("El pedido no está en camino");
        completarEntrega(o, recibio, true);
        o.cambiarEstado(EstadoPedido.ENTREGADO, "Entrega verificada con código QR", por.getNombre());
        avisarCambio(o);
        return o;
    }

    @Override
    public Pedido actualizarUbicacion(String guia, Double latitud, Double longitud, Usuario por) {
        Pedido o = porGuia(guia);
        validarPermiso(o, por);
        if (o.getEstado() != EstadoPedido.EN_CAMINO) throw new ReglaNegocioException("El envío no está en camino");
        if (latitud == null || longitud == null || Math.abs(latitud) > 90 || Math.abs(longitud) > 180) {
            throw new ReglaNegocioException("Coordenadas inválidas");
        }
        o.setLatitud(latitud);
        o.setLongitud(longitud);
        o.setUbicacionActualizada(LocalDateTime.now());
        return o;
    }

    private void validarPermiso(Pedido o, Usuario por) {
        if (por == null || !por.tienePermiso("PEDIDOS")) throw new ReglaNegocioException("No tienes permiso para modificar pedidos");
        if (!por.isAdmin() && (o.getEmpleado() == null || !o.getEmpleado().equals(por))) {
            throw new ReglaNegocioException("Este pedido está asignado a otra persona");
        }
    }

    private Usuario clienteDe(Pedido o) {
        if (o.getCliente() != null) return o.getCliente();
        return o.getCarritoDeCompra() == null ? null : o.getCarritoDeCompra().getUsuario();
    }

    private void avisarCambio(Pedido o) {
        String texto = "Tu pedido " + o.getFolio() + " ahora está: " + o.getEstado().getEtiqueta() + ".";
        correo.enviar(o.getCorreoCliente(), "Tu pedido " + o.getFolio() + ": " + o.getEstado().getEtiqueta(),
                texto + "\n\nConsulta el detalle en Mis pedidos o con tu folio en Seguimiento.");
        notificaciones.notificar(clienteDe(o), "Pedido " + o.getFolio() + ": " + o.getEstado().getEtiqueta(), texto,
                "/pedido/" + o.getFolio(), false);
    }

    // ======================= Factura =======================

    @Override
    public Factura solicitarFactura(String folio, FacturaForm f) {
        Pedido o = porFolio(folio);
        if (o.getFactura() != null) throw new ReglaNegocioException("Este pedido ya tiene factura");
        if (o.getEstado() == EstadoPedido.CANCELADO) throw new ReglaNegocioException("No se puede facturar un pedido cancelado");
        if (PENDIENTE.equals(o.getEstadoPago())) throw new ReglaNegocioException("Podrás pedir tu factura cuando confirmemos tu pago");
        Factura fa = new Factura();
        fa.setRfc(f.getRfc().trim().toUpperCase(Locale.ROOT));
        fa.setRazonSocial(f.getRazonSocial().trim());
        fa.setDireccionFiscal(f.getDireccionFiscal().trim());
        fa.setCfdi(f.getCfdi());
        fa.setFechaEmision(LocalDate.now());
        fa.setTotal(o.getTotal());
        fa.setPedido(o);
        facturas.save(fa);
        o.setFactura(fa);
        if (o.getCorreoCliente() != null) {
            correo.enviar(o.getCorreoCliente(), "Recibimos tu solicitud de factura del pedido " + o.getFolio(),
                    "RFC: " + fa.getRfc() + "\nRazón social: " + fa.getRazonSocial() + "\nUso del CFDI: " + fa.getCfdi()
                            + "\nTotal: $" + fa.getTotal().stripTrailingZeros().toPlainString()
                            + "\n\nTe enviaremos el XML y el PDF a este correo en un máximo de 72 horas.");
        }
        notificaciones.notificarAdmins("Factura solicitada " + o.getFolio(), fa.getRfc() + " · " + fa.getRazonSocial(), "/admin/pedidos/" + o.getFolio());
        return fa;
    }

    // ======================= Tablero =======================

    @Override
    @Transactional(readOnly = true)
    public long contar(EstadoPedido estado) { return pedidos.findAll().stream().filter(o -> o.getEstado() == estado).count(); }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal totalVendido() {
        return pedidos.findAll().stream()
                .filter(o -> o.getEstado() != EstadoPedido.CANCELADO && o.getEstado() != EstadoPedido.PENDIENTE_PAGO)
                .map(Pedido::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
