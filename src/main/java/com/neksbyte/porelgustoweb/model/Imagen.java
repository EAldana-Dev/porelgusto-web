package com.neksbyte.porelgustoweb.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "Imagen")
public class Imagen {
    @Id
    @Column(name = "idImagen")
    private Integer idImagen;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Column(name = "direccionImg", length = 255)
    private String direccionImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArregloFloral")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ArregloFloral arregloFloral;

    public Imagen() {
    }

    public Integer getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(Integer idImagen) {
        this.idImagen = idImagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccionImg() {
        return direccionImg;
    }

    public void setDireccionImg(String direccionImg) {
        this.direccionImg = direccionImg;
    }

    public ArregloFloral getArregloFloral() {
        return arregloFloral;
    }

    public void setArregloFloral(ArregloFloral arregloFloral) {
        this.arregloFloral = arregloFloral;
    }

}
