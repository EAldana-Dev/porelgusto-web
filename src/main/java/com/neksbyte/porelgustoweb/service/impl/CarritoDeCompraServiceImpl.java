package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.config.TiendaConfig;
import com.neksbyte.porelgustoweb.dao.CarritoDeCompraDao;
import com.neksbyte.porelgustoweb.dao.ContenidoCarritoDao;
import com.neksbyte.porelgustoweb.dto.CompraEnProceso;
import com.neksbyte.porelgustoweb.dto.LineaCarrito;
import com.neksbyte.porelgustoweb.dto.ProductoVista;
import com.neksbyte.porelgustoweb.dto.ResumenCompra;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.*;
import com.neksbyte.porelgustoweb.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional
public class CarritoDeCompraServiceImpl extends BaseServiceImpl<CarritoDeCompra, Integer> implements CarritoDeCompraService {

    private final CarritoDeCompraDao carritos;
    private final ContenidoCarritoDao contenidos;
    private final ArregloFloralService catalogo;
    private final CuponService cupones;
    private final EnvioService envios;
    private final TiendaConfig tienda;

    public CarritoDeCompraServiceImpl(CarritoDeCompraDao dao, ContenidoCarritoDao contenidos,
                                      ArregloFloralService catalogo, CuponService cupones, EnvioService envios, TiendaConfig tienda) {
        super(dao);
        this.carritos = dao;
        this.contenidos = contenidos;
        this.catalogo = catalogo;
        this.cupones = cupones;
        this.envios = envios;
        this.tienda = tienda;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> agregar(CarritoSesion carrito, Integer idArreglo, int cantidad, List<Integer> idsOpciones, String nota) {
        ArregloFloral p = catalogo.porId(idArreglo);
        if (!p.isDisponible()) {
            throw new ReglaNegocioException(p.getNombre() + " no está disponible por ahora. Puedes agregarlo a tu lista de deseos.");
        }
        int qty = Math.max(1, Math.min(cantidad, MAX_POR_PRODUCTO));
        List<String> noDisponibles = new ArrayList<>();
        List<Modificacion> elegidas = new ArrayList<>();
        if (idsOpciones != null && !idsOpciones.isEmpty()) {
            List<Modificacion> permitidas = catalogo.opcionesPara(p);
            for (Integer id : new LinkedHashSet<>(idsOpciones)) {
                permitidas.stream().filter(o -> o.getIdModificacion().equals(id)).findFirst().ifPresent(o -> {
                    if (o.isDisponible()) elegidas.add(o); else noDisponibles.add(o.getNombre());
                });
            }
        }
        elegidas.sort(Comparator.comparingInt(Modificacion::getOrden).thenComparing(Modificacion::getIdModificacion));
        List<Integer> ids = elegidas.stream().map(Modificacion::getIdModificacion).toList();
        String notaLimpia = Textos.limpio(nota);
        int enCarrito = carrito.getLineas().stream().filter(l -> l.getIdArreglo().equals(p.getIdArregloFloral()))
                .mapToInt(LineaCarrito::getCantidad).sum();
        if (enCarrito + qty > p.getExistencias()) {
            throw new ReglaNegocioException("Solo quedan " + p.getExistencias() + " piezas de " + p.getNombre());
        }
        // Si ya está la misma combinación, solo suma la cantidad.
        Optional<LineaCarrito> igual = carrito.getLineas().stream()
                .filter(l -> l.getIdArreglo().equals(p.getIdArregloFloral()) && l.getIdsOpciones().equals(ids)
                        && Objects.equals(l.getNota(), notaLimpia))
                .findFirst();
        if (igual.isPresent()) {
            igual.get().setCantidad(Math.min(MAX_POR_PRODUCTO, igual.get().getCantidad() + qty));
            return noDisponibles;
        }
        ProductoVista v = catalogo.vista(p);
        LineaCarrito l = new LineaCarrito();
        l.setIdArreglo(p.getIdArregloFloral());
        l.setSku(p.getSku());
        l.setNombre(p.getNombre());
        l.setImagenUrl(p.getImagenUrl());
        l.setPrecio(v.getPrecioFinal());
        l.setPrecioOriginal(p.getCosto());
        l.setCantidad(qty);
        l.setIdsOpciones(new ArrayList<>(ids));
        l.setNombresOpciones(new ArrayList<>(elegidas.stream()
                .map(o -> o.getNombre() + (o.getCosto().signum() > 0 ? " (+$" + o.getCosto().toPlainString() + ")" : "")).toList()));
        l.setCostoOpciones(elegidas.stream().map(Modificacion::getCosto).reduce(BigDecimal.ZERO, BigDecimal::add));
        l.setNota(notaLimpia);
        l.setPermiteQR(p.isPermiteQR());
        l.setPrueba(p.isPrueba());
        carrito.getLineas().add(l);
        return noDisponibles;
    }

    @Override
    @Transactional(readOnly = true)
    public void cambiarCantidad(CarritoSesion carrito, String idLinea, int cantidad) {
        LineaCarrito linea = linea(carrito, idLinea);
        if (cantidad <= 0) {
            carrito.getLineas().remove(linea);
            return;
        }
        ArregloFloral p = catalogo.porId(linea.getIdArreglo());
        int otras = carrito.getLineas().stream().filter(l -> l != linea && l.getIdArreglo().equals(p.getIdArregloFloral()))
                .mapToInt(LineaCarrito::getCantidad).sum();
        int maximo = Math.min(MAX_POR_PRODUCTO, p.getExistencias() - otras);
        if (cantidad > maximo) throw new ReglaNegocioException("Solo puedes pedir " + Math.max(0, maximo) + " piezas de " + p.getNombre());
        linea.setCantidad(cantidad);
    }

    @Override
    public void quitar(CarritoSesion carrito, String idLinea) { carrito.getLineas().remove(linea(carrito, idLinea)); }

    private LineaCarrito linea(CarritoSesion carrito, String idLinea) {
        return carrito.getLineas().stream().filter(l -> l.getIdLinea().equals(idLinea)).findFirst()
                .orElseThrow(() -> new ReglaNegocioException("Ese producto ya no está en tu carrito"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> revalidar(CarritoSesion carrito) {
        List<String> avisos = new ArrayList<>();
        for (LineaCarrito l : carrito.getLineas()) {
            Optional<ArregloFloral> p = catalogo.findById(l.getIdArreglo());
            boolean ok = p.isPresent() && p.get().isDisponible() && p.get().getExistencias() >= l.getCantidad();
            if (!ok && l.isDisponible()) avisos.add(l.getNombre() + " ya no está disponible en la cantidad pedida.");
            l.setDisponible(ok);
            p.ifPresent(prod -> {
                BigDecimal precio = catalogo.vista(prod).getPrecioFinal();
                if (precio.compareTo(l.getPrecio()) != 0) {
                    avisos.add("El precio de " + l.getNombre() + " cambió a $" + precio.stripTrailingZeros().toPlainString() + ".");
                    l.setPrecio(precio);
                    l.setPrecioOriginal(prod.getCosto());
                }
            });
        }
        return avisos;
    }

    @Override
    public boolean todoDisponible(CarritoSesion carrito) {
        return !carrito.isVacio() && carrito.getLineas().stream().allMatch(LineaCarrito::isDisponible);
    }

    @Override
    @Transactional(readOnly = true)
    public void aplicarCupon(CarritoSesion carrito, String codigo) {
        Cupon c = cupones.validar(codigo, subtotal(carrito));
        carrito.setCodigoCupon(c.getCodigo());
    }

    /** Fecha y horario de entrega: desde hoy (si aún da tiempo), sin domingos. */
    @Override
    public void asignarEntrega(CarritoSesion carrito, LocalDate fecha, String horario) {
        if (fecha == null || Textos.vacio(horario) || !HORARIOS.contains(horario)) throw new ReglaNegocioException("Elige la fecha y el horario de entrega");
        if (!fechasDisponibles().contains(fecha)) throw new ReglaNegocioException("No hay entregas en esa fecha. Elige otra.");
        if (fecha.equals(LocalDate.now()) && !horariosDe(fecha).contains(horario)) throw new ReglaNegocioException("Ese horario ya no está disponible hoy");
        carrito.setFechaEntrega(fecha);
        carrito.setHorarioEntrega(horario);
    }

    @Override
    public List<LocalDate> fechasDisponibles() {
        List<LocalDate> res = new ArrayList<>();
        LocalDate d = LocalDate.now();
        if (horariosDe(d).isEmpty()) d = d.plusDays(1);
        for (; res.size() < 45; d = d.plusDays(1)) if (d.getDayOfWeek() != DayOfWeek.SUNDAY) res.add(d);
        return res;
    }

    /** Hoy solo se ofrecen los horarios que empiezan al menos 3 horas después. */
    @Override
    public List<String> horariosDe(LocalDate fecha) {
        if (!fecha.equals(LocalDate.now())) return HORARIOS;
        int limite = LocalTime.now().getHour() + 3;
        return HORARIOS.stream().filter(h -> Integer.parseInt(h.substring(0, 2)) >= limite).toList();
    }

    @Override
    public BigDecimal subtotal(CarritoSesion carrito) {
        return carrito.getLineas().stream().map(LineaCarrito::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenCompra resumen(CarritoSesion carrito) { return resumen(carrito, null); }

    /** Calcula descuento, envío por distancia, IVA desglosado (los precios ya lo incluyen) y total. */
    @Override
    @Transactional(readOnly = true)
    public ResumenCompra resumen(CarritoSesion carrito, com.neksbyte.porelgustoweb.dto.CheckoutForm d) {
        ResumenCompra r = new ResumenCompra();
        BigDecimal subtotal = subtotal(carrito);
        r.setSubtotal(subtotal);
        if (carrito.getCodigoCupon() != null) {
            try {
                Cupon c = cupones.validar(carrito.getCodigoCupon(), subtotal);
                r.setDescuento(c.descuentoDe(subtotal));
                r.setCupon(c.getCodigo());
                r.setCuponNombre(c.getNombre() + " (" + c.getDescuentoTexto() + ")");
            } catch (ReglaNegocioException e) {
                r.setCuponError(e.getMessage());
                carrito.setCodigoCupon(null);
            }
        }
        BigDecimal base = subtotal.subtract(r.getDescuento()).max(BigDecimal.ZERO);
        boolean soloPrueba = carrito.getLineas().stream().allMatch(LineaCarrito::isPrueba);
        r.setFaltaParaEnvioGratis(tienda.getEnvioGratisDesde().subtract(base).max(BigDecimal.ZERO));
        if (carrito.isVacio() || soloPrueba) {
            r.setEnvio(BigDecimal.ZERO);
            r.setEnvioCalculado(true);
            r.setEnvioDetalle(soloPrueba && !carrito.isVacio() ? "Producto de prueba: sin envío" : null);
            r.setFaltaParaEnvioGratis(BigDecimal.ZERO);
        } else if (d != null && d.getCodigoPostal() != null && d.getCodigoPostal().trim().matches("\\d{5}")) {
            EnvioService.Cotizacion c = envios.cotizar(d.getCalle(), d.getColonia(), d.getCiudad(), d.getCodigoPostal().trim(), base);
            r.setEnvio(c.costo());
            r.setEnvioKm(c.km());
            r.setEnvioDetalle(c.detalle());
            r.setEnvioCalculado(true);
        }
        r.setTotal(base.add(r.getEnvio()));
        BigDecimal iva = tienda.getIva();
        r.setIva(r.getTotal().multiply(iva).divide(BigDecimal.ONE.add(iva), 2, RoundingMode.HALF_UP));
        return r;
    }

    @Override
    public CarritoDeCompra registrar(CompraEnProceso compra, Usuario usuario) {
        CarritoDeCompra c = new CarritoDeCompra();
        c.setFecha(LocalDate.now());
        c.setUsuario(usuario);
        c.setTotal(compra.getResumen().getSubtotal());
        carritos.save(c);
        for (LineaCarrito l : compra.getLineas()) {
            ContenidoCarrito cc = new ContenidoCarrito();
            cc.setCarritoDeCompra(c);
            cc.setArregloFloral(catalogo.porId(l.getIdArreglo()));
            cc.setCantidad(l.getCantidad());
            cc.setPrecioUnitario(l.getPrecioUnitario());
            cc.setPersonalizacion(l.getNombresOpciones().isEmpty() ? null : l.getOpcionesTexto());
            cc.setNota(l.getNota());
            contenidos.save(cc);
            c.getContenidos().add(cc);
        }
        return c;
    }
}
