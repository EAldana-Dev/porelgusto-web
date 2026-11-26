package com.neksbyte.porelgustoweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "ListaDeseo_ArregloFloral")
public class ListaDeseoArregloFloral {

    @EmbeddedId
    private ListaDeseoArregloFloralId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idLista")
    @JoinColumn(name = "idLista")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ListaDeDeseos listaDeDeseos;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idArregloFloral")
    @JoinColumn(name = "idArregloFloral")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ArregloFloral arregloFloral;

    /** Ya se avisó al cliente que el producto volvió a estar disponible. */
    @Column(name = "avisado")
    private Boolean avisado;

    @Column(name = "fechaAgregado")
    private java.time.LocalDateTime fechaAgregado;

    public ListaDeseoArregloFloral() {
    }

    public ListaDeseoArregloFloralId getId() {
        return id;
    }

    public void setId(ListaDeseoArregloFloralId id) {
        this.id = id;
    }

    public ListaDeDeseos getListaDeDeseos() {
        return listaDeDeseos;
    }

    public void setListaDeDeseos(ListaDeDeseos listaDeDeseos) {
        this.listaDeDeseos = listaDeDeseos;
    }

    public ArregloFloral getArregloFloral() {
        return arregloFloral;
    }

    public void setArregloFloral(ArregloFloral arregloFloral) {
        this.arregloFloral = arregloFloral;
    }

    public boolean isAvisado() { return avisado != null && avisado; }
    public void setAvisado(Boolean avisado) { this.avisado = avisado; }
    public java.time.LocalDateTime getFechaAgregado() { return fechaAgregado; }
    public void setFechaAgregado(java.time.LocalDateTime fechaAgregado) { this.fechaAgregado = fechaAgregado; }
}
