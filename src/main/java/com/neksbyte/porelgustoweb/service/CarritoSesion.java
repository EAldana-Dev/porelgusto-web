package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.dto.LineaCarrito;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Carrito de compras guardado en la sesión: funciona con o sin cuenta. Al pagar se registra
 * en la base de datos como CarritoDeCompra con su ContenidoCarrito.
 */
@Component
@SessionScope
public class CarritoSesion implements Serializable {
    private final List<LineaCarrito> lineas = new ArrayList<>();
    private String codigoCupon;
    private LocalDate fechaEntrega;
    private String horarioEntrega;

    public int getCantidad() { return lineas.stream().mapToInt(LineaCarrito::getCantidad).sum(); }
    public boolean isVacio() { return lineas.isEmpty(); }
    public boolean isConEntrega() { return fechaEntrega != null && horarioEntrega != null && !horarioEntrega.isBlank(); }

    public void vaciar() {
        lineas.clear();
        codigoCupon = null;
        fechaEntrega = null;
        horarioEntrega = null;
    }

    public List<LineaCarrito> getLineas() { return lineas; }
    public String getCodigoCupon() { return codigoCupon; }
    public void setCodigoCupon(String codigoCupon) { this.codigoCupon = codigoCupon; }
    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    public String getHorarioEntrega() { return horarioEntrega; }
    public void setHorarioEntrega(String horarioEntrega) { this.horarioEntrega = horarioEntrega; }
}
