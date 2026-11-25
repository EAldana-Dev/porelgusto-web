package com.neksbyte.porelgustoweb.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Cupon")
public class Cupon {
    @Id
    @Column(name = "idCupon")
    private Integer idCupon;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Column(name = "codigo", length = 100)
    private String codigo;

    @Column(name = "fechaVencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "limiteDeUsos")
    private Integer limiteDeUsos;

    @Column(name = "estado", length = 50)
    private String estado;

    @Column(name = "porcentajeDescuento", precision = 5, scale = 2)
    private BigDecimal porcentajeDescuento;

    @OneToMany(mappedBy = "cupon")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Pedido> pedidos = new ArrayList<>();

    public Cupon() {
    }

    public Integer getIdCupon() {
        return idCupon;
    }

    public void setIdCupon(Integer idCupon) {
        this.idCupon = idCupon;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Integer getLimiteDeUsos() {
        return limiteDeUsos;
    }

    public void setLimiteDeUsos(Integer limiteDeUsos) {
        this.limiteDeUsos = limiteDeUsos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

}
