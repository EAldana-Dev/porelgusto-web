package com.neksbyte.porelgustoweb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Aviso dentro del sitio (campana del menú). */
@Entity
@Table(name = "Notificacion")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idNotificacion")
    private Integer idNotificacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @Column(name = "titulo", length = 150)
    private String titulo;

    @Column(name = "cuerpo", length = 500)
    private String cuerpo;

    @Column(name = "liga", length = 200)
    private String liga;

    @Column(name = "leida")
    private Boolean leida = false;

    @Column(name = "fechaHora")
    private LocalDateTime fechaHora;

    public Integer getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(Integer idNotificacion) { this.idNotificacion = idNotificacion; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getCuerpo() { return cuerpo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }
    public String getLiga() { return liga; }
    public void setLiga(String liga) { this.liga = liga; }
    public boolean isLeida() { return leida != null && leida; }
    public void setLeida(Boolean leida) { this.leida = leida; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
