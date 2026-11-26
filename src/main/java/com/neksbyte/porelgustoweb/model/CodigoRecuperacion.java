package com.neksbyte.porelgustoweb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Código temporal de 6 dígitos para restablecer la contraseña (se guarda cifrado). */
@Entity
@Table(name = "CodigoRecuperacion")
public class CodigoRecuperacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCodigo")
    private Integer idCodigo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @Column(name = "codigoHash", length = 100)
    private String codigoHash;

    @Column(name = "vence")
    private LocalDateTime vence;

    @Column(name = "usado")
    private Boolean usado = false;

    @Column(name = "intentos")
    private Integer intentos = 0;

    @Column(name = "fechaHora")
    private LocalDateTime fechaHora;

    public boolean isVencido() { return vence != null && LocalDateTime.now().isAfter(vence); }

    public Integer getIdCodigo() { return idCodigo; }
    public void setIdCodigo(Integer idCodigo) { this.idCodigo = idCodigo; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getCodigoHash() { return codigoHash; }
    public void setCodigoHash(String codigoHash) { this.codigoHash = codigoHash; }
    public LocalDateTime getVence() { return vence; }
    public void setVence(LocalDateTime vence) { this.vence = vence; }
    public boolean isUsado() { return usado != null && usado; }
    public void setUsado(Boolean usado) { this.usado = usado; }
    public int getIntentos() { return intentos == null ? 0 : intentos; }
    public void setIntentos(Integer intentos) { this.intentos = intentos; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
