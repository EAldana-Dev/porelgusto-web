package com.neksbyte.porelgustoweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neksbyte.porelgustoweb.service.Textos;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Producto del catálogo (arreglo floral). La categoría principal se guarda como texto en
 * {@code categoria} (coincide con el nombre de una {@link Categoria} de tipo CATEGORIA) y las
 * etiquetas (flores) separadas por comas en {@code etiqueta}.
 */
@Entity
@Table(name = "ArregloFloral")
public class ArregloFloral {
    /** Imagen que se muestra si el producto aún no tiene foto. */
    public static final String SIN_IMAGEN = "/imagenes/logo.png";
    private static final int PUNTO_REORDEN = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idArregloFloral")
    private Integer idArregloFloral;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Column(name = "detalles", length = 255)
    private String detalles;

    @Column(name = "costo", precision = 10, scale = 2)
    private BigDecimal costo;

    @Column(name = "idTipoArreglo")
    private Integer idTipoArreglo;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Column(name = "etiqueta", length = 100)
    private String etiqueta;

    @Column(name = "disponibilidad", length = 100)
    private String disponibilidad;

    // ---------- Inventario y catálogo en línea ----------
    @Column(name = "sku", length = 20)
    private String sku;

    @Column(name = "existencias")
    private Integer existencias;

    @Column(name = "puntoReorden")
    private Integer puntoReorden;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "personalizable")
    private Boolean personalizable;

    @Column(name = "permiteQR")
    private Boolean permiteQR;

    @Column(name = "vistas")
    private Long vistas;

    @Column(name = "ventas")
    private Integer ventas;

    @Column(name = "fechaAlta")
    private LocalDateTime fechaAlta;

    /** Producto de prueba: no aparece en el catálogo (solo con su liga) y no cobra envío. */
    @Column(name = "prueba")
    private Boolean prueba;

    /** Colecciones, grupos y temporadas a los que pertenece (Primavera, Cumpleaños...). */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ArregloFloral_Coleccion",
            joinColumns = @JoinColumn(name = "idArregloFloral"),
            inverseJoinColumns = @JoinColumn(name = "idCategoria"))
    private Set<Categoria> colecciones = new HashSet<>();

    @OneToMany(mappedBy = "arregloFloral")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ListaDeseoArregloFloral> listasDeseo = new ArrayList<>();

    @OneToMany(mappedBy = "arregloFloral")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ContenidoCarrito> contenidosCarrito = new ArrayList<>();

    @OneToMany(mappedBy = "arregloFloral", fetch = FetchType.EAGER)
    @OrderBy("idImagen ASC")
    private List<Imagen> imagenes = new ArrayList<>();

    @OneToMany(mappedBy = "arregloFloral")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Modificacion> modificaciones = new ArrayList<>();

    public ArregloFloral(){}

    // ---------- Reglas del producto ----------

    /** Foto principal (la primera imagen). */
    public String getImagenUrl() {
        return imagenes == null || imagenes.isEmpty() || imagenes.get(0).getDireccionImg() == null
                ? SIN_IMAGEN : imagenes.get(0).getDireccionImg();
    }

    /** Slug de la categoría para las URLs: "Orquídeas" -> "orquideas". */
    public String getCategoriaSlug() { return Textos.slug(categoria); }

    /** Etiquetas (flores) como lista. */
    public List<String> getEtiquetas() {
        if (Textos.vacio(etiqueta)) return List.of();
        return Arrays.stream(etiqueta.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
    }

    public void setEtiquetas(Collection<String> lista) {
        this.etiqueta = lista == null || lista.isEmpty() ? null : String.join(", ", new TreeSet<>(lista));
    }

    /** Visible en el catálogo (los registros anteriores sin valor cuentan como activos). */
    public boolean isActivo() { return activo == null || activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public boolean isPersonalizable() { return personalizable == null || personalizable; }
    public void setPersonalizable(Boolean personalizable) { this.personalizable = personalizable; }

    public boolean isPermiteQR() { return permiteQR == null || permiteQR; }
    public void setPermiteQR(Boolean permiteQR) { this.permiteQR = permiteQR; }

    public int getExistencias() { return existencias == null ? 0 : existencias; }
    public void setExistencias(Integer existencias) {
        this.existencias = existencias;
        this.disponibilidad = getExistencias() > 0 ? "Disponible" : "Agotado";
    }

    public int getPuntoReorden() { return puntoReorden == null ? PUNTO_REORDEN : puntoReorden; }
    public void setPuntoReorden(Integer puntoReorden) { this.puntoReorden = puntoReorden; }

    public long getVistas() { return vistas == null ? 0 : vistas; }
    public void setVistas(Long vistas) { this.vistas = vistas; }

    public int getVentas() { return ventas == null ? 0 : ventas; }
    public void setVentas(Integer ventas) { this.ventas = ventas; }

    /** Se puede comprar: está activo y hay existencias. */
    public boolean isDisponible() { return isActivo() && getExistencias() > 0; }

    public boolean isPrueba() { return prueba != null && prueba; }
    public void setPrueba(Boolean prueba) { this.prueba = prueba; }

    /** Se muestra en el catálogo, buscador y recomendaciones. */
    public boolean isVisibleEnCatalogo() { return isActivo() && !isPrueba(); }

    public boolean isBajoInventario() { return getExistencias() <= getPuntoReorden(); }

    /** Agregado en los últimos 30 días. */
    public boolean isNuevo() { return fechaAlta != null && fechaAlta.isAfter(LocalDateTime.now().minusDays(30)); }

    /** Aparta piezas para un pedido. Regresa false si no alcanzan. */
    public boolean reservar(int cantidad) {
        if (cantidad <= 0 || getExistencias() < cantidad) return false;
        setExistencias(getExistencias() - cantidad);
        return true;
    }

    /** Regresa piezas al inventario (pedido cancelado). */
    public void liberar(int cantidad) { setExistencias(getExistencias() + Math.max(0, cantidad)); }

    @JsonIgnore
    public String getEtiquetasTexto() { return etiqueta == null ? "" : etiqueta; }

    // ---------- getters y setters ----------

    public Integer getIdArregloFloral() {
        return idArregloFloral;
    }

    public void setIdArregloFloral(Integer idArregloFloral) {
        this.idArregloFloral = idArregloFloral;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Integer getIdTipoArreglo() {
        return idTipoArreglo;
    }

    public void setIdTipoArreglo(Integer idTipoArreglo) {
        this.idTipoArreglo = idTipoArreglo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Set<Categoria> getColecciones() {
        return colecciones;
    }

    public void setColecciones(Set<Categoria> colecciones) {
        this.colecciones = colecciones;
    }

    public List<ListaDeseoArregloFloral> getListasDeseo() {
        return listasDeseo;
    }

    public void setListasDeseo(List<ListaDeseoArregloFloral> listasDeseo) {
        this.listasDeseo = listasDeseo;
    }

    public List<ContenidoCarrito> getContenidosCarrito() {
        return contenidosCarrito;
    }

    public void setContenidosCarrito(List<ContenidoCarrito> contenidosCarrito) {
        this.contenidosCarrito = contenidosCarrito;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public List<Modificacion> getModificaciones() {
        return modificaciones;
    }

    public void setModificaciones(List<Modificacion> modificaciones) {
        this.modificaciones = modificaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArregloFloral that)) return false;
        return idArregloFloral != null && idArregloFloral.equals(that.idArregloFloral);
    }

    @Override
    public int hashCode() { return Objects.hashCode(idArregloFloral); }
}
