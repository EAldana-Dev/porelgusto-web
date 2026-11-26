package com.neksbyte.porelgustoweb.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Categoría del menú (Ramos, Cajas...), grupo (Cumpleaños, Lujo...) o temporada (Primavera...).
 * Los productos guardan el nombre de su categoría en ArregloFloral.categoria y sus colecciones
 * en la tabla ArregloFloral_Coleccion.
 */
@Entity
@Table(name = "Categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCategoria")
    private Integer idCategoria;

    @Column(name = "nombre", length = 80)
    private String nombre;

    @Column(name = "slug", length = 80)
    private String slug;

    @Column(name = "descripcion", length = 300)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "tipo", length = 20)
    private TipoCategoria tipo = TipoCategoria.CATEGORIA;

    @Column(name = "fechaInicio")
    private LocalDate fechaInicio;

    @Column(name = "fechaFin")
    private LocalDate fechaFin;

    @Column(name = "orden")
    private Integer orden;

    public Categoria() {
    }

    /** Sin fechas = siempre vigente. */
    public boolean isVigente() {
        LocalDate hoy = LocalDate.now();
        return (fechaInicio == null || !hoy.isBefore(fechaInicio)) && (fechaFin == null || !hoy.isAfter(fechaFin));
    }

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public TipoCategoria getTipo() { return tipo; }
    public void setTipo(TipoCategoria tipo) { this.tipo = tipo; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public int getOrden() { return orden == null ? 0 : orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria that)) return false;
        return idCategoria != null && idCategoria.equals(that.idCategoria);
    }

    @Override
    public int hashCode() { return Objects.hashCode(idCategoria); }
}
