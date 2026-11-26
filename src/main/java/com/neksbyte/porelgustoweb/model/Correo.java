package com.neksbyte.porelgustoweb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Correo enviado por la tienda (copia que se ve en el panel: Correos enviados). */
@Entity
@Table(name = "Correo")
public class Correo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCorreo")
    private Integer idCorreo;

    @Column(name = "para", length = 150)
    private String para;

    @Column(name = "asunto", length = 200)
    private String asunto;

    @Column(name = "cuerpo", length = 4000)
    private String cuerpo;

    @Column(name = "fechaHora")
    private LocalDateTime fechaHora;

    public Integer getIdCorreo() { return idCorreo; }
    public void setIdCorreo(Integer idCorreo) { this.idCorreo = idCorreo; }
    public String getPara() { return para; }
    public void setPara(String para) { this.para = para; }
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public String getCuerpo() { return cuerpo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
