package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.dto.CompraEnProceso;
import com.neksbyte.porelgustoweb.dto.ResumenCompra;
import com.neksbyte.porelgustoweb.model.CarritoDeCompra;
import com.neksbyte.porelgustoweb.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Reglas del carrito: disponibilidad, personalización, cupones, fecha de entrega y totales. */
public interface CarritoDeCompraService extends BaseService<CarritoDeCompra, Integer> {
    /** Horarios de entrega que se ofrecen. */
    List<String> HORARIOS = List.of("09:00 - 12:00", "12:00 - 15:00", "15:00 - 18:00", "18:00 - 21:00");
    int MAX_POR_PRODUCTO = 10;

    /** Agrega un producto; regresa los nombres de las opciones que no estaban disponibles. */
    List<String> agregar(CarritoSesion carrito, Integer idArreglo, int cantidad, List<Integer> opciones, String nota);
    void cambiarCantidad(CarritoSesion carrito, String idLinea, int cantidad);
    void quitar(CarritoSesion carrito, String idLinea);
    /** Revisa disponibilidad y precios vigentes; regresa avisos para mostrar. */
    List<String> revalidar(CarritoSesion carrito);
    boolean todoDisponible(CarritoSesion carrito);
    void aplicarCupon(CarritoSesion carrito, String codigo);
    void asignarEntrega(CarritoSesion carrito, LocalDate fecha, String horario);
    List<LocalDate> fechasDisponibles();
    List<String> horariosDe(LocalDate fecha);
    BigDecimal subtotal(CarritoSesion carrito);
    /** Resumen sin dirección: el envío queda "por calcular". */
    ResumenCompra resumen(CarritoSesion carrito);
    /** Resumen con el envío calculado por distancia a la dirección de entrega. */
    ResumenCompra resumen(CarritoSesion carrito, com.neksbyte.porelgustoweb.dto.CheckoutForm direccion);
    /** Guarda en la base de datos el carrito de una compra pagada (CarritoDeCompra + ContenidoCarrito). */
    CarritoDeCompra registrar(CompraEnProceso compra, Usuario usuario);
}
