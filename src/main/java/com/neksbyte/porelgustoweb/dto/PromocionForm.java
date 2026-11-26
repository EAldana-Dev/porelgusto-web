package com.neksbyte.porelgustoweb.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Promociones, ofertas y eventos (se aplican solos al precio). */
public class PromocionForm {
    private Integer id;

    @NotBlank(message = "Escribe el nombre")
    @Size(max = 80)
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    @NotNull(message = "Elige el tipo")
    private String tipo = "PROMOCION";

    @NotNull
    private String tipoDescuento = "PORCENTAJE";

    @NotNull(message = "Escribe el descuento")
    @DecimalMin(value = "0.01", message = "El descuento debe ser mayor a 0")
    private BigDecimal valor;

    @Size(max = 300)
    private String condiciones;

    @NotNull(message = "Indica la fecha de inicio")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime inicio;

    @NotNull(message = "Indica la fecha de fin")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fin;

    private Integer categoriaId;

    private List<Integer> productos = new ArrayList<>();

    private boolean activo = true;

    private boolean notificar = true;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getTipoDescuento() { return tipoDescuento; }
    public void setTipoDescuento(String tipoDescuento) { this.tipoDescuento = tipoDescuento; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public String getCondiciones() { return condiciones; }
    public void setCondiciones(String condiciones) { this.condiciones = condiciones; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }
    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }
    public List<Integer> getProductos() { return productos; }
    public void setProductos(List<Integer> productos) { this.productos = productos; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public boolean isNotificar() { return notificar; }
    public void setNotificar(boolean notificar) { this.notificar = notificar; }
}
