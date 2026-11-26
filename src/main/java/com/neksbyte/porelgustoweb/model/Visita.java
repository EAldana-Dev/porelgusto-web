package com.neksbyte.porelgustoweb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Una página vista del sitio (para las estadísticas del panel). No se guarda la IP:
 * el visitante se identifica con una huella anónima que cambia cada día.
 */
@Entity
@Table(name = "Visita")
public class Visita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVisita")
    private Long idVisita;

    @Column(name = "fechaHora")
    private LocalDateTime fechaHora;

    @Column(name = "ruta", length = 200)
    private String ruta;

    /** Huella diaria anónima (hash de IP + navegador + día). */
    @Column(name = "visitante", length = 64)
    private String visitante;

    /** Sitio del que llegó: Google, Facebook, WhatsApp, Directo... */
    @Column(name = "origen", length = 80)
    private String origen;

    @Column(name = "campana", length = 80)
    private String campana;

    @Column(name = "pais", length = 80)
    private String pais;

    @Column(name = "region", length = 80)
    private String region;

    @Column(name = "ciudad", length = 80)
    private String ciudad;

    @Column(name = "dispositivo", length = 20)
    private String dispositivo;

    @Column(name = "navegador", length = 30)
    private String navegador;

    public Long getIdVisita() { return idVisita; }
    public void setIdVisita(Long idVisita) { this.idVisita = idVisita; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }
    public String getVisitante() { return visitante; }
    public void setVisitante(String visitante) { this.visitante = visitante; }
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    public String getCampana() { return campana; }
    public void setCampana(String campana) { this.campana = campana; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getDispositivo() { return dispositivo; }
    public void setDispositivo(String dispositivo) { this.dispositivo = dispositivo; }
    public String getNavegador() { return navegador; }
    public void setNavegador(String navegador) { this.navegador = navegador; }
}
