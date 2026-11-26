package com.neksbyte.porelgustoweb.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/** Registro de una cuenta de cliente. */
public class RegistroForm {
    @NotBlank(message = "Escribe tu nombre")
    @Size(max = 60)
    private String nombre;

    @NotBlank(message = "Escribe tu apellido")
    @Size(max = 80)
    private String apellido;

    @NotBlank(message = "Escribe tu correo")
    @Email(message = "Escribe un correo válido")
    @Size(max = 150)
    private String correo;

    @Pattern(regexp = "^([0-9 +()-]{8,20})?$", message = "Escribe un teléfono válido (10 dígitos)")
    private String telefono;

    @Past(message = "La fecha debe ser anterior a hoy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;

    @NotBlank(message = "Escribe una contraseña")
    @Size(min = 8, max = 72, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "Confirma tu contraseña")
    private String confirmacion;

    @AssertTrue(message = "Las contraseñas no coinciden")
    public boolean isPasswordsIguales() { return password == null || password.equals(confirmacion); }

    /** Nombre completo que se guarda en Usuario.nombre. */
    public String nombreCompleto() { return (nombre.trim() + " " + apellido.trim()).trim(); }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmacion() { return confirmacion; }
    public void setConfirmacion(String confirmacion) { this.confirmacion = confirmacion; }
}
