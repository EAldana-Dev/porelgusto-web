package com.neksbyte.porelgustoweb.model;

public enum EstadoSoporte {
    ABIERTO("Abierto", "warning"), EN_PROCESO("En proceso", "info"), CERRADO("Cerrado", "secondary");

    private final String etiqueta;
    private final String color;

    EstadoSoporte(String etiqueta, String color) {
        this.etiqueta = etiqueta;
        this.color = color;
    }

    public String getEtiqueta() { return etiqueta; }
    public String getColor() { return color; }
}
