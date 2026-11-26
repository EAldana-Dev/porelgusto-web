package com.neksbyte.porelgustoweb.model;

/** Promociones que se aplican solas al precio de los productos (los cupones están en Cupon). */
public enum TipoPromocion {
    PROMOCION("Promoción"), OFERTA("Oferta"), EVENTO("Evento");

    private final String etiqueta;

    TipoPromocion(String etiqueta) { this.etiqueta = etiqueta; }

    public String getEtiqueta() { return etiqueta; }
}
