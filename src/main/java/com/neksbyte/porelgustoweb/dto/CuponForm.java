package com.neksbyte.porelgustoweb.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Cupones de descuento (el cliente escribe el código en el carrito). */
public class CuponForm {
    private Integer id;

    @NotBlank(message = "Escribe el nombre")
    @Size(max = 150)
    private String nombre;

    @NotBlank(message = "Escribe el código")
    @Pattern(regexp = "^[A-Za-z0-9_-]{3,30}$", message = "Solo letras, números, guion y guion bajo (3 a 30)")
    private String codigo;

    @Size(max = 300)
    private String descripcion;

    @NotNull
    private String tipoDescuento = "PORCENTAJE";

    @NotNull(message = "Escribe el descuento")
    @DecimalMin(value = "0.01", message = "El descuento debe ser mayor a 0")
    private BigDecimal valor;

    @DecimalMin(value = "0", message = "La compra mínima no puede ser negativa")
    private BigDecimal compraMinima;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate inicio;

    @NotNull(message = "Indica la fecha de vencimiento")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate vencimiento;

    @Min(value = 1, message = "Mínimo 1 uso")
    private Integer limiteDeUsos;

    private boolean activo = true;

    private boolean notificar;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipoDescuento() { return tipoDescuento; }
    public void setTipoDescuento(String tipoDescuento) { this.tipoDescuento = tipoDescuento; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public BigDecimal getCompraMinima() { return compraMinima; }
    public void setCompraMinima(BigDecimal compraMinima) { this.compraMinima = compraMinima; }
    public LocalDate getInicio() { return inicio; }
    public void setInicio(LocalDate inicio) { this.inicio = inicio; }
    public LocalDate getVencimiento() { return vencimiento; }
    public void setVencimiento(LocalDate vencimiento) { this.vencimiento = vencimiento; }
    public Integer getLimiteDeUsos() { return limiteDeUsos; }
    public void setLimiteDeUsos(Integer limiteDeUsos) { this.limiteDeUsos = limiteDeUsos; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public boolean isNotificar() { return notificar; }
    public void setNotificar(boolean notificar) { this.notificar = notificar; }
}
