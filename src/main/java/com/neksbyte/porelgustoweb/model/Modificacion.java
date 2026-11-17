package com.neksbyte.porelgustoweb.model;
import jakarta.persistence.*;

@Entity
@Table(name = "Modificacion")
public class Modificacion {
    @Id
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
    private ArregloFloral arregloFloral;

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

}
