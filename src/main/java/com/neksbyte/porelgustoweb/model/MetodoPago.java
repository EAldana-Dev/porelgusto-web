package com.neksbyte.porelgustoweb.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MetodoPago")
public class MetodoPago {
    @Id
    @Column(name = "idMetodosDePago")
    private Integer idMetodosDePago;

    @Column(name = "tipoPago", length = 100)
    private String tipoPago;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "metodoPago")
    private List<Pedido> pedidos = new ArrayList<>();

    public MetodoPago() {
    }

    public Integer getIdMetodosDePago() {
        return idMetodosDePago;
    }

    public void setIdMetodosDePago(Integer idMetodosDePago) {
        this.idMetodosDePago = idMetodosDePago;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

}
