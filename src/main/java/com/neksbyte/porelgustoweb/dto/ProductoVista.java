package com.neksbyte.porelgustoweb.dto;

import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.model.Promocion;

import java.math.BigDecimal;

/** Producto listo para mostrarse: incluye el precio final con la mejor promoción vigente. */
public class ProductoVista {
    private final ArregloFloral producto;
    private final Promocion promocion;
    private final BigDecimal precioFinal;

    public ProductoVista(ArregloFloral producto, Promocion promocion) {
        this.producto = producto;
        this.promocion = promocion;
        BigDecimal base = producto.getCosto() == null ? BigDecimal.ZERO : producto.getCosto();
        this.precioFinal = promocion == null ? base : base.subtract(promocion.descuentoDe(base)).setScale(0, java.math.RoundingMode.HALF_UP);
    }

    public boolean isConDescuento() { return producto.getCosto() != null && precioFinal.compareTo(producto.getCosto()) < 0; }
    public ArregloFloral getProducto() { return producto; }
    public Promocion getPromocion() { return promocion; }
    public BigDecimal getPrecioFinal() { return precioFinal; }
}
