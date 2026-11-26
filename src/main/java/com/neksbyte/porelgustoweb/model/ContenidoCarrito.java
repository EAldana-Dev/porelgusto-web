package com.neksbyte.porelgustoweb.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ContenidoCarrito")
public class ContenidoCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // ---------- Personalización elegida en la tienda ----------
    @Column(name = "personalizacion", length = 500)
    private String personalizacion;

    @Column(name = "nota", length = 300)
    private String nota;

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

    /** precioUnitario × cantidad. */
    public BigDecimal getSubtotal() {
        if (precioUnitario == null || cantidad == null) return BigDecimal.ZERO;
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public String getPersonalizacion() { return personalizacion; }
    public void setPersonalizacion(String personalizacion) { this.personalizacion = personalizacion; }
    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }
}
