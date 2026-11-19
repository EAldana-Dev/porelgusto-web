package com.neksbyte.porelgustoweb.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "ListaDeseo_ArregloFloral")
public class ListaDeseoArregloFloral {

    @EmbeddedId
    private ListaDeseoArregloFloralId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idLista")
    @JoinColumn(name = "idLista")
    private ListaDeDeseos listaDeDeseos;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idArregloFloral")
    @JoinColumn(name = "idArregloFloral")
    private ArregloFloral arregloFloral;

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
}
