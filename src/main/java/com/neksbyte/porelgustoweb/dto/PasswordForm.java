package com.neksbyte.porelgustoweb.dto;

import jakarta.validation.constraints.*;

/** Cambio de contraseña (con sesión o con código de recuperación). */
public class PasswordForm {
    private String actual;

    private String correo;

    private String codigo;

    @NotBlank(message = "Escribe la nueva contraseña")
    @Size(min = 8, max = 72, message = "La contraseña debe tener al menos 8 caracteres")
    private String nueva;

    @NotBlank(message = "Confirma la nueva contraseña")
    private String confirmacion;

    @AssertTrue(message = "Las contraseñas no coinciden")
    public boolean isPasswordsIguales() { return nueva == null || nueva.equals(confirmacion); }

    public String getActual() { return actual; }
    public void setActual(String actual) { this.actual = actual; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNueva() { return nueva; }
    public void setNueva(String nueva) { this.nueva = nueva; }
    public String getConfirmacion() { return confirmacion; }
    public void setConfirmacion(String confirmacion) { this.confirmacion = confirmacion; }
}
