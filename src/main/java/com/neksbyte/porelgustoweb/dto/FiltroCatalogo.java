package com.neksbyte.porelgustoweb.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Criterios del buscador y filtros del catálogo (llegan como parámetros GET). */
public class FiltroCatalogo {
    private String q;
    private String categoria;
    private String coleccion;
    private List<String> etiqueta = new ArrayList<>();
    private BigDecimal precioMin;
    private BigDecimal precioMax;
    private boolean disponibles;
    private boolean ofertas;
    private String orden = "relevancia";

    /** ¿Hay algún filtro aplicado? (para mostrar "Limpiar filtros"). */
    public boolean isActivo() {
        return (q != null && !q.isBlank()) || (categoria != null && !categoria.isBlank())
                || (coleccion != null && !coleccion.isBlank()) || !etiqueta.isEmpty()
                || precioMin != null || precioMax != null || disponibles || ofertas;
    }

    public String getQ() { return q; }
    public void setQ(String q) { this.q = q; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getColeccion() { return coleccion; }
    public void setColeccion(String coleccion) { this.coleccion = coleccion; }
    public List<String> getEtiqueta() { return etiqueta; }
    public void setEtiqueta(List<String> etiqueta) { this.etiqueta = etiqueta == null ? new ArrayList<>() : etiqueta; }
    public BigDecimal getPrecioMin() { return precioMin; }
    public void setPrecioMin(BigDecimal precioMin) { this.precioMin = precioMin; }
    public BigDecimal getPrecioMax() { return precioMax; }
    public void setPrecioMax(BigDecimal precioMax) { this.precioMax = precioMax; }
    public boolean isDisponibles() { return disponibles; }
    public void setDisponibles(boolean disponibles) { this.disponibles = disponibles; }
    public boolean isOfertas() { return ofertas; }
    public void setOfertas(boolean ofertas) { this.ofertas = ofertas; }
    public String getOrden() { return orden; }
    public void setOrden(String orden) { this.orden = orden == null || orden.isBlank() ? "relevancia" : orden; }
}
