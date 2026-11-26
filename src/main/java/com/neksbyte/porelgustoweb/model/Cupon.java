package com.neksbyte.porelgustoweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cupón de descuento que el cliente escribe en el carrito. Descuenta un porcentaje
 * (porcentajeDescuento) o un monto fijo (montoDescuento) del subtotal.
 */
@Entity
@Table(name = "Cupon")
public class Cupon {
    public static final String ACTIVO = "Activo";
    public static final String INACTIVO = "Inactivo";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // ---------- Reglas adicionales ----------
    @Column(name = "montoDescuento", precision = 10, scale = 2)
    private BigDecimal montoDescuento;

    @Column(name = "compraMinima", precision = 10, scale = 2)
    private BigDecimal compraMinima;

    @Column(name = "fechaInicio")
    private LocalDate fechaInicio;

    @Column(name = "usos")
    private Integer usos;

    @Column(name = "descripcion", length = 300)
    private String descripcion;

    @OneToMany(mappedBy = "cupon")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Pedido> pedidos = new ArrayList<>();

    public Cupon() {
    }

    public boolean isActivo() { return estado == null || ACTIVO.equalsIgnoreCase(estado); }

    public boolean isVencido() { return fechaVencimiento != null && LocalDate.now().isAfter(fechaVencimiento); }

    public boolean isVigente() {
        return isActivo() && !isVencido() && (fechaInicio == null || !LocalDate.now().isBefore(fechaInicio))
                && (limiteDeUsos == null || getUsos() < limiteDeUsos);
    }

    public boolean isMonto() { return montoDescuento != null && montoDescuento.signum() > 0; }

    /** Descuento sobre el subtotal (redondeado a pesos, nunca más que el subtotal). */
    public BigDecimal descuentoDe(BigDecimal subtotal) {
        if (subtotal == null) return BigDecimal.ZERO;
        BigDecimal d = isMonto() ? montoDescuento
                : subtotal.multiply(porcentajeDescuento == null ? BigDecimal.ZERO : porcentajeDescuento)
                        .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
        return d.min(subtotal);
    }

    /** "10%" o "$150". */
    public String getDescuentoTexto() {
        if (isMonto()) return "$" + montoDescuento.stripTrailingZeros().toPlainString();
        return porcentajeDescuento == null ? "" : porcentajeDescuento.stripTrailingZeros().toPlainString() + "%";
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

    public BigDecimal getMontoDescuento() {
        return montoDescuento;
    }

    public void setMontoDescuento(BigDecimal montoDescuento) {
        this.montoDescuento = montoDescuento;
    }

    public BigDecimal getCompraMinima() {
        return compraMinima;
    }

    public void setCompraMinima(BigDecimal compraMinima) {
        this.compraMinima = compraMinima;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getUsos() {
        return usos == null ? 0 : usos;
    }

    public void setUsos(Integer usos) {
        this.usos = usos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}
