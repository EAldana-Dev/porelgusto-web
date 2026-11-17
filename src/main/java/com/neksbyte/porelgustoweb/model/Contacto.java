package com.neksbyte.porelgustoweb.model;
import jakarta.persistence.*;

@Entity
@Table(name = "Contacto")
public class Contacto {
    @Id
    @Column(name = "idContacto")
    private Integer idContacto;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Column(name = "tipoContacto", length = 100)
    private String tipoContacto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    public Contacto() {
    }

    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoContacto() {
        return tipoContacto;
    }

    public void setTipoContacto(String tipoContacto) {
        this.tipoContacto = tipoContacto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
