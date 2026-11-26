package com.neksbyte.porelgustoweb.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Un producto dentro del carrito de la sesión (con su personalización). */
public class LineaCarrito implements Serializable {
    private final String idLinea = UUID.randomUUID().toString().substring(0, 8);
    private Integer idArreglo;
    private String sku;
    private String nombre;
    private String imagenUrl;
    /** Precio del producto con promoción (sin personalización). */
    private BigDecimal precio;
    private BigDecimal precioOriginal;
    private int cantidad;
    private List<Integer> idsOpciones = new ArrayList<>();
    private List<String> nombresOpciones = new ArrayList<>();
    private BigDecimal costoOpciones = BigDecimal.ZERO;
    private String nota;
    private boolean permiteQR;
    /** Producto de prueba (no cobra envío). */
    private boolean prueba;
    private boolean disponible = true;

    public BigDecimal getPrecioUnitario() { return precio.add(costoOpciones); }
    public BigDecimal getSubtotal() { return getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad)); }
    public boolean isConDescuento() { return precioOriginal != null && precioOriginal.compareTo(precio) > 0; }
    public String getOpcionesTexto() { return String.join(", ", nombresOpciones); }

    public String getIdLinea() { return idLinea; }
    public Integer getIdArreglo() { return idArreglo; }
    public void setIdArreglo(Integer idArreglo) { this.idArreglo = idArreglo; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public BigDecimal getPrecioOriginal() { return precioOriginal; }
    public void setPrecioOriginal(BigDecimal precioOriginal) { this.precioOriginal = precioOriginal; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public List<Integer> getIdsOpciones() { return idsOpciones; }
    public void setIdsOpciones(List<Integer> idsOpciones) { this.idsOpciones = idsOpciones; }
    public List<String> getNombresOpciones() { return nombresOpciones; }
    public void setNombresOpciones(List<String> nombresOpciones) { this.nombresOpciones = nombresOpciones; }
    public BigDecimal getCostoOpciones() { return costoOpciones; }
    public void setCostoOpciones(BigDecimal costoOpciones) { this.costoOpciones = costoOpciones; }
    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }
    public boolean isPrueba() { return prueba; }
    public void setPrueba(boolean prueba) { this.prueba = prueba; }
    public boolean isPermiteQR() { return permiteQR; }
    public void setPermiteQR(boolean permiteQR) { this.permiteQR = permiteQR; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
