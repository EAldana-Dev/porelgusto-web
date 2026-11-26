package com.neksbyte.porelgustoweb.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Solicitud de soporte (ticket de ayuda) entre un cliente y el personal de la tienda. */
@Entity
@Table(name = "SolicitudSoporte")
public class SolicitudSoporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSolicitud")
    private Integer idSolicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @Column(name = "nombreContacto", length = 120)
    private String nombreContacto;

    @Column(name = "correoContacto", length = 120)
    private String correoContacto;

    @Column(name = "asunto", length = 120)
    private String asunto;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "estado", length = 20)
    private EstadoSoporte estado = EstadoSoporte.ABIERTO;

    @Column(name = "folioPedido", length = 20)
    private String folioPedido;

    /** Responder antes de esta fecha (24 h). */
    @Column(name = "fechaLimite")
    private LocalDateTime fechaLimite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAgente")
    private Usuario agente;

    @Column(name = "fechaHora")
    private LocalDateTime fechaHora;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fechaHora ASC, idMensaje ASC")
    private List<MensajeSoporte> mensajes = new ArrayList<>();

    public MensajeSoporte agregarMensaje(Usuario autor, String nombreAutor, String contenido, boolean dePersonal) {
        MensajeSoporte m = new MensajeSoporte();
        m.setSolicitud(this);
        m.setAutor(autor);
        m.setNombreAutor(nombreAutor);
        m.setContenido(contenido);
        m.setDePersonal(dePersonal);
        m.setFechaHora(LocalDateTime.now());
        mensajes.add(m);
        return m;
    }

    /** Un empleado la toma: pasa a "En proceso". */
    public void asignarA(Usuario agente) {
        this.agente = agente;
        if (estado == EstadoSoporte.ABIERTO) estado = EstadoSoporte.EN_PROCESO;
    }

    public boolean isDe(Usuario u) { return u != null && usuario != null && usuario.getIdUsuario().equals(u.getIdUsuario()); }

    public Integer getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(Integer idSolicitud) { this.idSolicitud = idSolicitud; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getNombreContacto() { return nombreContacto; }
    public void setNombreContacto(String nombreContacto) { this.nombreContacto = nombreContacto; }
    public String getCorreoContacto() { return correoContacto; }
    public void setCorreoContacto(String correoContacto) { this.correoContacto = correoContacto; }
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public EstadoSoporte getEstado() { return estado; }
    public void setEstado(EstadoSoporte estado) { this.estado = estado; }
    public String getFolioPedido() { return folioPedido; }
    public void setFolioPedido(String folioPedido) { this.folioPedido = folioPedido; }
    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaLimite = fechaLimite; }
    public Usuario getAgente() { return agente; }
    public void setAgente(Usuario agente) { this.agente = agente; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public List<MensajeSoporte> getMensajes() { return mensajes; }
    public void setMensajes(List<MensajeSoporte> mensajes) { this.mensajes = mensajes; }
}
