package com.neksbyte.porelgustoweb.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Modificacion")
public class Modificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idModificacion")
    private Integer idModificacion;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "idCosto")
    private Integer idCosto;

    @Column(name = "tipoModificacion", length = 100)
    private String tipoModificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArregloFloral")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ArregloFloral arregloFloral;

    // ---------- Opción de personalización (sin arreglo = disponible para todos) ----------
    @Column(name = "disponible")
    private Boolean disponible;

    @Column(name = "orden")
    private Integer orden;

    public Modificacion() {
    }

    public Integer getIdModificacion() {
        return idModificacion;
    }

    public void setIdModificacion(Integer idModificacion) {
        this.idModificacion = idModificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdCosto() {
        return idCosto;
    }

    public void setIdCosto(Integer idCosto) {
        this.idCosto = idCosto;
    }

    public String getTipoModificacion() {
        return tipoModificacion;
    }

    public void setTipoModificacion(String tipoModificacion) {
        this.tipoModificacion = tipoModificacion;
    }

    public ArregloFloral getArregloFloral() {
        return arregloFloral;
    }

    public void setArregloFloral(ArregloFloral arregloFloral) {
        this.arregloFloral = arregloFloral;
    }

    // ---------- Personalización ----------

    /** Costo adicional en pesos (se guarda en idCosto). */
    public BigDecimal getCosto() { return BigDecimal.valueOf(idCosto == null ? 0 : idCosto); }

    public boolean isDisponible() { return disponible == null || disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }

    public int getOrden() { return orden == null ? 0 : orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    /** Aplica a todos los productos personalizables (no está ligada a un arreglo). */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isGeneral() { return arregloFloral == null; }
}
