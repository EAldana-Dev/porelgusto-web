package com.neksbyte.porelgustoweb.model;

/** Tipos de agrupación del catálogo: categorías del menú, grupos (colecciones) y temporadas o eventos. */
public enum TipoCategoria {
    CATEGORIA("Categoría"), GRUPO("Grupo"), TEMPORADA("Temporada");

    private final String etiqueta;

    TipoCategoria(String etiqueta) { this.etiqueta = etiqueta; }

    public String getEtiqueta() { return etiqueta; }
}
