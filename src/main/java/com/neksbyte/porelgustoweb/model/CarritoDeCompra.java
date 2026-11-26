package com.neksbyte.porelgustoweb.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CarritoDeCompra")
public class CarritoDeCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCarritoDeCompra")
    private Integer idCarritoDeCompra;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Usuario usuario;

    @OneToMany(mappedBy = "carritoDeCompra")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ContenidoCarrito> contenidos = new ArrayList<>();

    @OneToMany(mappedBy = "carritoDeCompra")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Pedido> pedidos = new ArrayList<>();

    public CarritoDeCompra() {
    }

    public Integer getIdCarritoDeCompra() {
        return idCarritoDeCompra;
    }

    public void setIdCarritoDeCompra(Integer idCarritoDeCompra) {
        this.idCarritoDeCompra = idCarritoDeCompra;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ContenidoCarrito> getContenidos() {
        return contenidos;
    }

    public void setContenidos(List<ContenidoCarrito> contenidos) {
        this.contenidos = contenidos;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

}
