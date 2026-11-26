package com.neksbyte.porelgustoweb.dto;

import jakarta.validation.constraints.*;

/** Opciones de personalización (tabla Modificacion). */
public class OpcionForm {
    private Integer id;

    @NotBlank(message = "Escribe el nombre")
    @Size(max = 150)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

    @NotNull(message = "Escribe el costo")
    @Min(value = 0, message = "El costo no puede ser negativo")
    private Integer costo;

    @Size(max = 100)
    private String tipo = "Extra";

    private boolean disponible = true;

    private int orden;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getCosto() { return costo; }
    public void setCosto(Integer costo) { this.costo = costo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }
}
