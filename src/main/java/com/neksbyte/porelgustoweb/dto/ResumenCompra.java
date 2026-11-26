package com.neksbyte.porelgustoweb.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/** Importes del carrito: subtotal, descuento, envío, IVA (incluido) y total. */
public class ResumenCompra implements Serializable {
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal descuento = BigDecimal.ZERO;
    private BigDecimal envio = BigDecimal.ZERO;
    private BigDecimal iva = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal faltaParaEnvioGratis = BigDecimal.ZERO;
    private String cupon;
    private String cuponNombre;
    private String cuponError;
    /** false mientras no se conoce la dirección (el envío se calcula con ella). */
    private boolean envioCalculado;
    private Double envioKm;
    private String envioDetalle;

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal v) { subtotal = v; }
    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal v) { descuento = v; }
    public BigDecimal getEnvio() { return envio; }
    public void setEnvio(BigDecimal v) { envio = v; }
    public BigDecimal getIva() { return iva; }
    public void setIva(BigDecimal v) { iva = v; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal v) { total = v; }
    public BigDecimal getFaltaParaEnvioGratis() { return faltaParaEnvioGratis; }
    public void setFaltaParaEnvioGratis(BigDecimal v) { faltaParaEnvioGratis = v; }
    public String getCupon() { return cupon; }
    public void setCupon(String v) { cupon = v; }
    public String getCuponNombre() { return cuponNombre; }
    public void setCuponNombre(String v) { cuponNombre = v; }
    public boolean isEnvioCalculado() { return envioCalculado; }
    public void setEnvioCalculado(boolean v) { envioCalculado = v; }
    public Double getEnvioKm() { return envioKm; }
    public void setEnvioKm(Double v) { envioKm = v; }
    public String getEnvioDetalle() { return envioDetalle; }
    public void setEnvioDetalle(String v) { envioDetalle = v; }
    public String getCuponError() { return cuponError; }
    public void setCuponError(String v) { cuponError = v; }
}
