package com.neksbyte.porelgustoweb.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Pedido")
public class Pedido {
    @Id
    @Column(name = "idPedido")
    private Integer idPedido;

    @Column(name = "fechaCreacion")
    private LocalDate fechaCreacion;

    @Column(name = "estadoEnvio", length = 100)
    private String estadoEnvio;

    @Column(name = "direccionEntrega", length = 255)
    private String direccionEntrega;

    @Column(name = "fechaEntrega")
    private LocalDate fechaEntrega;

    @Column(name = "instruccionesEspeciales", length = 255)
    private String instruccionesEspeciales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCarritoDeCompra")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CarritoDeCompra carritoDeCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMetodosDePago")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MetodoPago metodoPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCupon")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Cupon cupon;

    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Ticket ticket;

    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Factura factura;

    public Pedido() {
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getInstruccionesEspeciales() {
        return instruccionesEspeciales;
    }

    public void setInstruccionesEspeciales(String instruccionesEspeciales) {
        this.instruccionesEspeciales = instruccionesEspeciales;
    }

    public CarritoDeCompra getCarritoDeCompra() {
        return carritoDeCompra;
    }

    public void setCarritoDeCompra(CarritoDeCompra carritoDeCompra) {
        this.carritoDeCompra = carritoDeCompra;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Cupon getCupon() {
        return cupon;
    }

    public void setCupon(Cupon cupon) {
        this.cupon = cupon;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

}
