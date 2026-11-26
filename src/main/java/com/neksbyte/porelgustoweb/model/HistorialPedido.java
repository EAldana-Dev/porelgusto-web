package com.neksbyte.porelgustoweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Cada cambio de etapa de un pedido, con fecha, hora y quién lo hizo. */
@Entity
@Table(name = "HistorialPedido")
public class HistorialPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idHistorial")
    private Integer idHistorial;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idPedido")
    @JsonIgnore
    private Pedido pedido;

    @Column(name = "estado", length = 30)
    private String estado;

    @Column(name = "nota", length = 500)
    private String nota;

    @Column(name = "autor", length = 120)
    private String autor;

    @Column(name = "fechaHora")
    private LocalDateTime fechaHora;

    public EstadoPedido getEstadoPedido() { return EstadoPedido.desde(estado); }

    public Integer getIdHistorial() { return idHistorial; }
    public void setIdHistorial(Integer idHistorial) { this.idHistorial = idHistorial; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota == null || nota.isBlank() ? null : (nota.length() > 500 ? nota.substring(0, 500) : nota); }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
