package com.neksbyte.porelgustoweb.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/** Categorías, grupos y temporadas del catálogo. */
public class CategoriaForm {
    private Integer id;

    @NotBlank(message = "Escribe el nombre")
    @Size(max = 60)
    private String nombre;

    @Size(max = 300)
    private String descripcion;

    @NotNull
    private String tipo = "CATEGORIA";

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate inicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fin;

    private int orden;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDate getInicio() { return inicio; }
    public void setInicio(LocalDate inicio) { this.inicio = inicio; }
    public LocalDate getFin() { return fin; }
    public void setFin(LocalDate fin) { this.fin = fin; }
    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }
}
