package com.neksbyte.porelgustoweb.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ListaDeDeseos")
public class ListaDeDeseos {
    @Id
    @Column(name = "idLista")
    private Integer idLista;

    @Column(name = "fechaCreacion")
    private LocalDate fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "listaDeDeseos")
    private List<ListaDeseoArregloFloral> arreglos = new ArrayList<>();

    public ListaDeDeseos() {
    }

    public Integer getIdLista() {
        return idLista;
    }

    public void setIdLista(Integer idLista) {
        this.idLista = idLista;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ListaDeseoArregloFloral> getArreglos() {
        return arreglos;
    }

    public void setArreglos(List<ListaDeseoArregloFloral> arreglos) {
        this.arreglos = arreglos;
    }

}
