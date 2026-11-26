package com.neksbyte.porelgustoweb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "MensajeSoporte")
public class MensajeSoporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMensaje")
    private Integer idMensaje;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idSolicitud")
    private SolicitudSoporte solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    private Usuario autor;

    @Column(name = "nombreAutor", length = 120)
    private String nombreAutor;

    @Column(name = "contenido", length = 2000)
    private String contenido;

    @Column(name = "dePersonal")
    private Boolean dePersonal = false;

    @Column(name = "fechaHora")
    private LocalDateTime fechaHora;

    public Integer getIdMensaje() { return idMensaje; }
    public void setIdMensaje(Integer idMensaje) { this.idMensaje = idMensaje; }
    public SolicitudSoporte getSolicitud() { return solicitud; }
    public void setSolicitud(SolicitudSoporte solicitud) { this.solicitud = solicitud; }
    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }
    public String getNombreAutor() { return nombreAutor; }
    public void setNombreAutor(String nombreAutor) { this.nombreAutor = nombreAutor; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public boolean isDePersonal() { return dePersonal != null && dePersonal; }
    public void setDePersonal(Boolean dePersonal) { this.dePersonal = dePersonal; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
