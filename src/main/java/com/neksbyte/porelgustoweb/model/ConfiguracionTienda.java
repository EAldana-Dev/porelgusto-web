package com.neksbyte.porelgustoweb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Configuración que el administrador cambia desde el panel (un solo registro, id 1):
 * cuentas que reciben los pagos y punto de partida de los envíos.
 * Las llaves secretas se guardan cifradas (ver config.Cifrador).
 */
@Entity
@Table(name = "ConfiguracionTienda")
public class ConfiguracionTienda {
    public static final int ID = 1;

    @Id
    @Column(name = "idConfiguracion")
    private Integer idConfiguracion = ID;

    // ---------- Pagos ----------
    @Column(name = "stripeSecreta", length = 600)
    private String stripeSecretaCifrada;

    @Column(name = "paypalClientId", length = 200)
    private String paypalClientId;

    @Column(name = "paypalSecreta", length = 600)
    private String paypalSecretaCifrada;

    /** true = PayPal real (api-m.paypal.com); false = Sandbox. */
    @Column(name = "paypalProduccion")
    private Boolean paypalProduccion;

    // ---------- Punto de partida de los envíos ----------
    @Column(name = "origenDireccion", length = 255)
    private String origenDireccion;

    @Column(name = "origenLatitud")
    private Double origenLatitud;

    @Column(name = "origenLongitud")
    private Double origenLongitud;

    @Column(name = "actualizado")
    private LocalDateTime actualizado;

    @Column(name = "actualizadoPor", length = 150)
    private String actualizadoPor;

    public Integer getIdConfiguracion() { return idConfiguracion; }
    public void setIdConfiguracion(Integer idConfiguracion) { this.idConfiguracion = idConfiguracion; }
    public String getStripeSecretaCifrada() { return stripeSecretaCifrada; }
    public void setStripeSecretaCifrada(String v) { this.stripeSecretaCifrada = v; }
    public String getPaypalClientId() { return paypalClientId; }
    public void setPaypalClientId(String paypalClientId) { this.paypalClientId = paypalClientId; }
    public String getPaypalSecretaCifrada() { return paypalSecretaCifrada; }
    public void setPaypalSecretaCifrada(String v) { this.paypalSecretaCifrada = v; }
    public boolean isPaypalProduccion() { return paypalProduccion != null && paypalProduccion; }
    public void setPaypalProduccion(Boolean paypalProduccion) { this.paypalProduccion = paypalProduccion; }
    public String getOrigenDireccion() { return origenDireccion; }
    public void setOrigenDireccion(String origenDireccion) { this.origenDireccion = origenDireccion; }
    public Double getOrigenLatitud() { return origenLatitud; }
    public void setOrigenLatitud(Double origenLatitud) { this.origenLatitud = origenLatitud; }
    public Double getOrigenLongitud() { return origenLongitud; }
    public void setOrigenLongitud(Double origenLongitud) { this.origenLongitud = origenLongitud; }
    public LocalDateTime getActualizado() { return actualizado; }
    public void setActualizado(LocalDateTime actualizado) { this.actualizado = actualizado; }
    public String getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(String actualizadoPor) { this.actualizadoPor = actualizadoPor; }
}
