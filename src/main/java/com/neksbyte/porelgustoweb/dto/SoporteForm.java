package com.neksbyte.porelgustoweb.dto;

import jakarta.validation.constraints.*;

/** Mensaje para abrir una solicitud de soporte. */
public class SoporteForm {
    @NotBlank(message = "Escribe tu nombre")
    @Size(max = 120)
    private String nombre;

    @NotBlank(message = "Escribe tu correo")
    @Email(message = "Escribe un correo válido")
    @Size(max = 120)
    private String correo;

    @NotBlank(message = "Escribe el asunto")
    @Size(max = 120)
    private String asunto;

    @Size(max = 20)
    private String folio;

    @NotBlank(message = "Cuéntanos qué pasó")
    @Size(max = 2000)
    private String mensaje;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
