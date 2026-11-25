package com.neksbyte.porelgustoweb.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ContenidoCarrito")
public class ContenidoCarrito {
    @Id
    @Column(name = "idContenido")
    private Integer idContenido;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precioUnitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCarritoDeCompra")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CarritoDeCompra carritoDeCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArregloFloral")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ArregloFloral arregloFloral;

    public ContenidoCarrito() {
    }

    public Integer getIdContenido() {
        return idContenido;
    }

    public void setIdContenido(Integer idContenido) {
        this.idContenido = idContenido;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public CarritoDeCompra getCarritoDeCompra() {
        return carritoDeCompra;
    }

    public void setCarritoDeCompra(CarritoDeCompra carritoDeCompra) {
        this.carritoDeCompra = carritoDeCompra;
    }

    public ArregloFloral getArregloFloral() {
        return arregloFloral;
    }

    public void setArregloFloral(ArregloFloral arregloFloral) {
        this.arregloFloral = arregloFloral;
    }

}
