package com.neksbyte.porelgustoweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ArregloFloral")
public class ArregloFloral {

    @Id
    @Column(name = "idArregloFloral")
    private Integer idArregloFloral;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Column(name = "detalles", length = 255)
    private String detalles;

    @Column(name = "costo", precision = 10, scale = 2)
    private BigDecimal costo;

    @Column(name = "idTipoArreglo")
    private Integer idTipoArreglo;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Column(name = "etiqueta", length = 100)
    private String etiqueta;

    @Column(name = "disponibilidad", length = 100)
    private String disponibilidad;

    @OneToMany(mappedBy = "arregloFloral")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ListaDeseoArregloFloral> listasDeseo = new ArrayList<>();

    @OneToMany(mappedBy = "arregloFloral")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ContenidoCarrito> contenidosCarrito = new ArrayList<>();

    @OneToMany(mappedBy = "arregloFloral", fetch = FetchType.EAGER)
    private List<Imagen> imagenes = new ArrayList<>();

    @OneToMany(mappedBy = "arregloFloral")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Modificacion> modificaciones = new ArrayList<>();

    public Integer getIdArregloFloral() {
        return idArregloFloral;
    }

    public void setIdArregloFloral(Integer idArregloFloral) {
        this.idArregloFloral = idArregloFloral;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Integer getIdTipoArreglo() {
        return idTipoArreglo;
    }

    public void setIdTipoArreglo(Integer idTipoArreglo) {
        this.idTipoArreglo = idTipoArreglo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public List<ListaDeseoArregloFloral> getListasDeseo() {
        return listasDeseo;
    }

    public void setListasDeseo(List<ListaDeseoArregloFloral> listasDeseo) {
        this.listasDeseo = listasDeseo;
    }

    public List<ContenidoCarrito> getContenidosCarrito() {
        return contenidosCarrito;
    }

    public void setContenidosCarrito(List<ContenidoCarrito> contenidosCarrito) {
        this.contenidosCarrito = contenidosCarrito;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public List<Modificacion> getModificaciones() {
        return modificaciones;
    }

    public void setModificaciones(List<Modificacion> modificaciones) {
        this.modificaciones = modificaciones;
    }

    public ArregloFloral(){}
}
