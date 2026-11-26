package com.neksbyte.porelgustoweb.dto;

import com.neksbyte.porelgustoweb.model.FormaPago;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Compra en proceso: copia del carrito y datos de entrega mientras el usuario paga
 * (se guarda en la sesión; el pedido se registra solo cuando el pago se aprueba).
 */
public class CompraEnProceso implements Serializable {
    private final String referencia = UUID.randomUUID().toString();
    private final LocalDateTime creada = LocalDateTime.now();
    private CheckoutForm datos;
    private FormaPago forma;
    private List<LineaCarrito> lineas = new ArrayList<>();
    private ResumenCompra resumen;
    private LocalDate fechaEntrega;
    private String horarioEntrega;
    /** Id del pago en Stripe (sesión) o PayPal (orden). */
    private String referenciaProveedor;

    public boolean isVencida() { return creada.isBefore(LocalDateTime.now().minusMinutes(60)); }
    public BigDecimal getTotal() { return resumen == null ? BigDecimal.ZERO : resumen.getTotal(); }

    public String getReferencia() { return referencia; }
    public CheckoutForm getDatos() { return datos; }
    public void setDatos(CheckoutForm datos) { this.datos = datos; }
    public FormaPago getForma() { return forma; }
    public void setForma(FormaPago forma) { this.forma = forma; }
    public List<LineaCarrito> getLineas() { return lineas; }
    public void setLineas(List<LineaCarrito> lineas) { this.lineas = lineas; }
    public ResumenCompra getResumen() { return resumen; }
    public void setResumen(ResumenCompra resumen) { this.resumen = resumen; }
    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    public String getHorarioEntrega() { return horarioEntrega; }
    public void setHorarioEntrega(String horarioEntrega) { this.horarioEntrega = horarioEntrega; }
    public String getReferenciaProveedor() { return referenciaProveedor; }
    public void setReferenciaProveedor(String referenciaProveedor) { this.referenciaProveedor = referenciaProveedor; }
}
