package com.neksbyte.porelgustoweb.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Promoción, oferta o evento que se aplica sola al precio de los productos mientras está vigente.
 * Puede aplicar a todo el catálogo, a una categoría/colección o a productos específicos.
 */
@Entity
@Table(name = "Promocion")
public class Promocion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPromocion")
    private Integer idPromocion;

    @Column(name = "nombre", length = 80)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "tipo", length = 20)
    private TipoPromocion tipo = TipoPromocion.PROMOCION;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "tipoDescuento", length = 20)
    private TipoDescuento tipoDescuento = TipoDescuento.PORCENTAJE;

    @Column(name = "valor", precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "condiciones", length = 300)
    private String condiciones;

    @Column(name = "fechaInicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fechaFin")
    private LocalDateTime fechaFin;

    @Column(name = "activa")
    private Boolean activa = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCategoria")
    private Categoria categoria;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Promocion_ArregloFloral",
            joinColumns = @JoinColumn(name = "idPromocion"),
            inverseJoinColumns = @JoinColumn(name = "idArregloFloral"))
    private Set<ArregloFloral> productos = new HashSet<>();

    public Promocion() {
    }

    public boolean isVigente() {
        LocalDateTime ahora = LocalDateTime.now();
        return isActiva() && fechaInicio != null && fechaFin != null && !ahora.isBefore(fechaInicio) && !ahora.isAfter(fechaFin);
    }

    /** ¿Se aplica a este producto? */
    public boolean aplicaA(ArregloFloral p) {
        if (!isVigente()) return false;
        if (!productos.isEmpty()) return productos.contains(p);
        if (categoria != null) {
            return categoria.getNombre().equalsIgnoreCase(p.getCategoria()) || p.getColecciones().contains(categoria);
        }
        return true;
    }

    /** Cuánto se descuenta de un importe (nunca más que el importe). */
    public BigDecimal descuentoDe(BigDecimal importe) {
        if (importe == null || valor == null) return BigDecimal.ZERO;
        BigDecimal d = tipoDescuento == TipoDescuento.PORCENTAJE
                ? importe.multiply(valor).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                : valor;
        return d.min(importe);
    }

    /** "20%" o "$150". */
    public String getDescuentoTexto() {
        if (valor == null) return "";
        return tipoDescuento == TipoDescuento.PORCENTAJE
                ? valor.stripTrailingZeros().toPlainString() + "%"
                : "$" + valor.stripTrailingZeros().toPlainString();
    }

    public Integer getIdPromocion() { return idPromocion; }
    public void setIdPromocion(Integer idPromocion) { this.idPromocion = idPromocion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public TipoPromocion getTipo() { return tipo; }
    public void setTipo(TipoPromocion tipo) { this.tipo = tipo; }
    public TipoDescuento getTipoDescuento() { return tipoDescuento; }
    public void setTipoDescuento(TipoDescuento tipoDescuento) { this.tipoDescuento = tipoDescuento; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public String getCondiciones() { return condiciones; }
    public void setCondiciones(String condiciones) { this.condiciones = condiciones; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public boolean isActiva() { return activa == null || activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Set<ArregloFloral> getProductos() { return productos; }
    public void setProductos(Set<ArregloFloral> productos) { this.productos = productos; }
}
