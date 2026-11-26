package com.neksbyte.porelgustoweb.model;

public enum TipoDescuento {
    PORCENTAJE("Porcentaje"), MONTO("Monto fijo");

    private final String etiqueta;

    TipoDescuento(String etiqueta) { this.etiqueta = etiqueta; }

    public String getEtiqueta() { return etiqueta; }
}
