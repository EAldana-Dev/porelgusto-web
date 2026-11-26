package com.neksbyte.porelgustoweb.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;

/** Datos de contacto y entrega para pagar. */
public class CheckoutForm implements Serializable {
    @NotBlank(message = "Escribe tu nombre")
    @Size(max = 120)
    private String nombreCliente;

    @NotBlank(message = "Escribe tu correo")
    @Email(message = "Escribe un correo válido")
    @Size(max = 120)
    private String correoCliente;

    @NotBlank(message = "Escribe tu teléfono")
    @Pattern(regexp = "^([0-9 +()-]{8,20})?$", message = "Escribe un teléfono válido (10 dígitos)")
    private String telefonoCliente;

    @NotBlank(message = "¿Quién recibe?")
    @Size(max = 120)
    private String destinatario;

    @Pattern(regexp = "^([0-9 +()-]{8,20})?$", message = "Escribe un teléfono válido (10 dígitos)")
    private String telefonoDestinatario;

    @NotBlank(message = "Escribe la calle y número")
    @Size(max = 160)
    private String calle;

    @Size(max = 80)
    private String colonia;

    @NotBlank(message = "Escribe la ciudad o alcaldía")
    @Size(max = 80)
    private String ciudad;

    @NotBlank(message = "Escribe el código postal")
    @Pattern(regexp = "^\\d{5}$", message = "El código postal tiene 5 dígitos")
    private String codigoPostal;

    @Size(max = 200)
    private String referencias;

    private boolean guardarDireccion;

    @Size(max = 500, message = "La dedicatoria puede tener hasta 500 caracteres")
    private String dedicatoria;

    @NotNull(message = "Elige un método de pago")
    private String metodoPago;

    /** Dirección completa para la etiqueta del envío. */
    public String direccionCompleta() {
        return calle.trim() + (colonia == null || colonia.isBlank() ? "" : ", Col. " + colonia.trim())
                + ", " + ciudad.trim() + ", C.P. " + codigoPostal;
    }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }
    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }
    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
    public String getTelefonoDestinatario() { return telefonoDestinatario; }
    public void setTelefonoDestinatario(String telefonoDestinatario) { this.telefonoDestinatario = telefonoDestinatario; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getColonia() { return colonia; }
    public void setColonia(String colonia) { this.colonia = colonia; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    public String getReferencias() { return referencias; }
    public void setReferencias(String referencias) { this.referencias = referencias; }
    public boolean isGuardarDireccion() { return guardarDireccion; }
    public void setGuardarDireccion(boolean guardarDireccion) { this.guardarDireccion = guardarDireccion; }
    public String getDedicatoria() { return dedicatoria; }
    public void setDedicatoria(String dedicatoria) { this.dedicatoria = dedicatoria; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}
