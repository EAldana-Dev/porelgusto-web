package com.neksbyte.porelgustoweb.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/** Datos personales en Mi cuenta. */
public class PerfilForm {
    @NotBlank(message = "Escribe tu nombre")
    @Size(max = 150)
    private String nombre;

    @NotBlank(message = "Escribe tu correo")
    @Email(message = "Escribe un correo válido")
    @Size(max = 150)
    private String correo;

    @Pattern(regexp = "^([0-9 +()-]{8,20})?$", message = "Escribe un teléfono válido (10 dígitos)")
    private String telefono;

    @Size(max = 255)
    private String direccion;

    @Past(message = "La fecha debe ser anterior a hoy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
}
