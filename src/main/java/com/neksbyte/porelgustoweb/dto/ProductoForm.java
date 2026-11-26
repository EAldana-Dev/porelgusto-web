package com.neksbyte.porelgustoweb.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Alta y edición de productos (arreglos florales). */
public class ProductoForm {
    private Integer id;

    @NotBlank(message = "Escribe el nombre")
    @Size(max = 150)
    private String nombre;

    @Size(max = 255, message = "Máximo 255 caracteres")
    private String descripcion;

    @NotNull(message = "Escribe el precio")
    @DecimalMin(value = "1", message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "Precio inválido")
    private BigDecimal precio;

    @NotBlank(message = "Elige una categoría")
    private String categoria;

    private List<Integer> colecciones = new ArrayList<>();

    @Size(max = 100, message = "Máximo 100 caracteres")
    private String etiquetas;

    @Min(value = 0, message = "Las existencias no pueden ser negativas")
    private int existencias;

    @Min(value = 0)
    private int puntoReorden = 3;

    private boolean activo = true;

    private boolean personalizable = true;

    private boolean permiteQR = true;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public List<Integer> getColecciones() { return colecciones; }
    public void setColecciones(List<Integer> colecciones) { this.colecciones = colecciones; }
    public String getEtiquetas() { return etiquetas; }
    public void setEtiquetas(String etiquetas) { this.etiquetas = etiquetas; }
    public int getExistencias() { return existencias; }
    public void setExistencias(int existencias) { this.existencias = existencias; }
    public int getPuntoReorden() { return puntoReorden; }
    public void setPuntoReorden(int puntoReorden) { this.puntoReorden = puntoReorden; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public boolean isPersonalizable() { return personalizable; }
    public void setPersonalizable(boolean personalizable) { this.personalizable = personalizable; }
    private boolean prueba;
    public boolean isPrueba() { return prueba; }
    public void setPrueba(boolean prueba) { this.prueba = prueba; }
    public boolean isPermiteQR() { return permiteQR; }
    public void setPermiteQR(boolean permiteQR) { this.permiteQR = permiteQR; }
}
