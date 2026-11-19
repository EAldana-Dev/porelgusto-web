package com.neksbyte.porelgustoweb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ListaDeseoArregloFloralId implements Serializable {

    @Column(name = "idLista")
    private Integer idLista;

    @Column(name = "idArregloFloral")
    private Integer idArregloFloral;

    public ListaDeseoArregloFloralId() {
    }

    public ListaDeseoArregloFloralId(Integer idLista, Integer idArregloFloral) {
        this.idLista = idLista;
        this.idArregloFloral = idArregloFloral;
    }

    public Integer getIdLista() {
        return idLista;
    }

    public void setIdLista(Integer idLista) {
        this.idLista = idLista;
    }

    public Integer getIdArregloFloral() {
        return idArregloFloral;
    }

    public void setIdArregloFloral(Integer idArregloFloral) {
        this.idArregloFloral = idArregloFloral;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ListaDeseoArregloFloralId that)) return false;
        return Objects.equals(idLista, that.idLista)
                && Objects.equals(idArregloFloral, that.idArregloFloral);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLista, idArregloFloral);
    }
}
