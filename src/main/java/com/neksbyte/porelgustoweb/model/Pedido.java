package com.neksbyte.porelgustoweb.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Pedido de la tienda. Los productos son el contenido de su CarritoDeCompra; el estado se guarda
 * como texto en estadoEnvio (ver {@link EstadoPedido}) y cada cambio queda en {@link HistorialPedido}.
 */
@Entity
@Table(name = "Pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPedido")
    private Integer idPedido;

    @Column(name = "fechaCreacion")
    private LocalDate fechaCreacion;

    @Column(name = "estadoEnvio", length = 100)
    private String estadoEnvio;

    /** Dirección completa para la etiqueta: calle, colonia, ciudad y C.P. */
    @Column(name = "direccionEntrega", length = 255)
    private String direccionEntrega;

    @Column(name = "fechaEntrega")
    private LocalDate fechaEntrega;

    /** Referencias para encontrar el domicilio. */
    @Column(name = "instruccionesEspeciales", length = 255)
    private String instruccionesEspeciales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCarritoDeCompra")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CarritoDeCompra carritoDeCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMetodosDePago")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MetodoPago metodoPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCupon")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Cupon cupon;

    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Ticket ticket;

    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Factura factura;

    // ---------- Datos de la compra en línea ----------
    @Column(name = "folio", length = 20)
    private String folio;

    @Column(name = "fechaHora")
    private LocalDateTime fechaHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Usuario cliente;

    @Column(name = "nombreCliente", length = 120)
    private String nombreCliente;

    @Column(name = "correoCliente", length = 120)
    private String correoCliente;

    @Column(name = "telefonoCliente", length = 30)
    private String telefonoCliente;

    @Column(name = "destinatario", length = 120)
    private String destinatario;

    @Column(name = "telefonoDestinatario", length = 30)
    private String telefonoDestinatario;

    @Column(name = "horarioEntrega", length = 20)
    private String horarioEntrega;

    /** Distancia en línea recta desde la florería (km), si se pudo ubicar la dirección. */
    @Column(name = "distanciaKm")
    private Double distanciaKm;

    // ---------- Importes (los precios incluyen IVA) ----------
    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "descuento", precision = 10, scale = 2)
    private BigDecimal descuento;

    @Column(name = "costoEnvio", precision = 10, scale = 2)
    private BigDecimal costoEnvio;

    @Column(name = "iva", precision = 10, scale = 2)
    private BigDecimal iva;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    // ---------- Pago ----------
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "formaPago", length = 20)
    private FormaPago formaPago;

    /** APROBADO, PENDIENTE, REEMBOLSADO o REEMBOLSO_PENDIENTE. */
    @Column(name = "estadoPago", length = 30)
    private String estadoPago;

    @Column(name = "proveedorPago", length = 40)
    private String proveedorPago;

    @Column(name = "transaccion", length = 120)
    private String transaccion;

    @Column(name = "fechaPago")
    private LocalDateTime fechaPago;

    // ---------- Reparto ----------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEmpleado")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Usuario empleado;

    @Column(name = "numeroGuia", length = 20)
    private String numeroGuia;

    @Column(name = "repartidor", length = 120)
    private String repartidor;

    @Column(name = "telefonoRepartidor", length = 30)
    private String telefonoRepartidor;

    @Column(name = "horaEstimada")
    private LocalDateTime horaEstimada;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "ubicacionActualizada")
    private LocalDateTime ubicacionActualizada;

    @Column(name = "recibio", length = 120)
    private String recibio;

    @Column(name = "fechaEntregado")
    private LocalDateTime fechaEntregado;

    @Column(name = "entregaVerificadaQR")
    private Boolean entregaVerificadaQR;

    // ---------- Dedicatoria con código QR ----------
    @Column(name = "dedicatoria", length = 500)
    private String dedicatoria;

    @Column(name = "tokenQr", length = 40)
    @JsonIgnore
    private String tokenQr;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fechaHora ASC, idHistorial ASC")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<HistorialPedido> historial = new ArrayList<>();

    public Pedido() {
    }

    // ---------- Reglas del pedido ----------

    @JsonIgnore
    public EstadoPedido getEstado() { return EstadoPedido.desde(estadoEnvio); }

    /** Cambia la etapa y la registra en el historial con fecha y hora. */
    public void cambiarEstado(EstadoPedido nuevo, String nota, String autor) {
        this.estadoEnvio = nuevo.getEtiqueta();
        HistorialPedido h = new HistorialPedido();
        h.setPedido(this);
        h.setEstado(nuevo.getEtiqueta());
        h.setNota(nota);
        h.setAutor(autor == null ? "Sistema" : autor);
        h.setFechaHora(LocalDateTime.now());
        historial.add(h);
    }

    /** Agrega una nota al historial sin cambiar la etapa (p. ej. una incidencia). */
    public void anotar(String nota, String autor) { cambiarEstado(getEstado(), nota, autor); }

    /** Fecha en que llegó a una etapa (línea de tiempo). */
    public LocalDateTime fechaDe(EstadoPedido e) {
        return historial.stream().filter(h -> h.getEstadoPedido() == e)
                .map(HistorialPedido::getFechaHora).findFirst().orElse(null);
    }

    /** Productos del pedido (contenido del carrito con el que se compró). */
    @JsonIgnore
    public List<ContenidoCarrito> getItems() {
        return carritoDeCompra == null ? List.of() : carritoDeCompra.getContenidos();
    }

    @JsonIgnore
    public int getTotalPiezas() {
        return getItems().stream().mapToInt(i -> i.getCantidad() == null ? 0 : i.getCantidad()).sum();
    }

    /** Folio para mostrar (los pedidos antiguos sin folio usan su id). */
    public String getFolio() { return folio != null ? folio : "PG" + idPedido; }

    public boolean isPagado() { return "APROBADO".equals(estadoPago); }

    /** El pago está pendiente de confirmar (depósito). */
    public boolean isPendienteDePago() { return getEstado() == EstadoPedido.PENDIENTE_PAGO; }

    /** Total (si es un pedido antiguo sin importes, se calcula de sus productos). */
    public BigDecimal getTotal() {
        if (total != null) return total;
        return getItems().stream().map(ContenidoCarrito::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getSubtotal() { return subtotal != null ? subtotal : getTotal(); }
    public BigDecimal getDescuento() { return descuento == null ? BigDecimal.ZERO : descuento; }
    public BigDecimal getCostoEnvio() { return costoEnvio == null ? BigDecimal.ZERO : costoEnvio; }

    public BigDecimal getIva() {
        if (iva != null) return iva;
        BigDecimal tasa = new BigDecimal("0.16");
        return getTotal().multiply(tasa).divide(BigDecimal.ONE.add(tasa), 2, RoundingMode.HALF_UP);
    }

    /** Nombre de quien recibe (pedidos antiguos: el del cliente). */
    public String getDestinatario() {
        if (destinatario != null) return destinatario;
        return carritoDeCompra != null && carritoDeCompra.getUsuario() != null ? carritoDeCompra.getUsuario().getNombre() : "";
    }

    /** Fecha y hora de creación (pedidos antiguos: el inicio del día). */
    public LocalDateTime getFechaHora() {
        if (fechaHora != null) return fechaHora;
        return fechaCreacion == null ? null : fechaCreacion.atStartOfDay();
    }

    @JsonIgnore
    public boolean isConEnvio() { return numeroGuia != null; }

    @JsonIgnore
    public boolean isUbicacionDefinida() { return latitud != null && longitud != null; }

    @JsonIgnore
    public boolean isConDedicatoria() { return tokenQr != null && dedicatoria != null; }

    /** El QR de la dedicatoria se puede ver durante un año. */
    @JsonIgnore
    public boolean isDedicatoriaVencida() {
        LocalDateTime base = getFechaHora();
        return base != null && LocalDateTime.now().isAfter(base.plusYears(1));
    }

    // ---------- getters y setters ----------

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getInstruccionesEspeciales() {
        return instruccionesEspeciales;
    }

    public void setInstruccionesEspeciales(String instruccionesEspeciales) {
        this.instruccionesEspeciales = instruccionesEspeciales;
    }

    public CarritoDeCompra getCarritoDeCompra() {
        return carritoDeCompra;
    }

    public void setCarritoDeCompra(CarritoDeCompra carritoDeCompra) {
        this.carritoDeCompra = carritoDeCompra;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Cupon getCupon() {
        return cupon;
    }

    public void setCupon(Cupon cupon) {
        this.cupon = cupon;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public void setFolio(String folio) { this.folio = folio; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }
    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
    public String getTelefonoDestinatario() { return telefonoDestinatario; }
    public void setTelefonoDestinatario(String telefonoDestinatario) { this.telefonoDestinatario = telefonoDestinatario; }
    public Double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(Double distanciaKm) { this.distanciaKm = distanciaKm; }
    public String getHorarioEntrega() { return horarioEntrega; }
    public void setHorarioEntrega(String horarioEntrega) { this.horarioEntrega = horarioEntrega; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }
    public void setCostoEnvio(BigDecimal costoEnvio) { this.costoEnvio = costoEnvio; }
    public void setIva(BigDecimal iva) { this.iva = iva; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public FormaPago getFormaPago() { return formaPago; }
    public void setFormaPago(FormaPago formaPago) { this.formaPago = formaPago; }
    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }
    public String getProveedorPago() { return proveedorPago; }
    public void setProveedorPago(String proveedorPago) { this.proveedorPago = proveedorPago; }
    public String getTransaccion() { return transaccion; }
    public void setTransaccion(String transaccion) { this.transaccion = transaccion; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
    public Usuario getEmpleado() { return empleado; }
    public void setEmpleado(Usuario empleado) { this.empleado = empleado; }
    public String getNumeroGuia() { return numeroGuia; }
    public void setNumeroGuia(String numeroGuia) { this.numeroGuia = numeroGuia; }
    public String getRepartidor() { return repartidor; }
    public void setRepartidor(String repartidor) { this.repartidor = repartidor; }
    public String getTelefonoRepartidor() { return telefonoRepartidor; }
    public void setTelefonoRepartidor(String telefonoRepartidor) { this.telefonoRepartidor = telefonoRepartidor; }
    public LocalDateTime getHoraEstimada() { return horaEstimada; }
    public void setHoraEstimada(LocalDateTime horaEstimada) { this.horaEstimada = horaEstimada; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public LocalDateTime getUbicacionActualizada() { return ubicacionActualizada; }
    public void setUbicacionActualizada(LocalDateTime ubicacionActualizada) { this.ubicacionActualizada = ubicacionActualizada; }
    public String getRecibio() { return recibio; }
    public void setRecibio(String recibio) { this.recibio = recibio; }
    public LocalDateTime getFechaEntregado() { return fechaEntregado; }
    public void setFechaEntregado(LocalDateTime fechaEntregado) { this.fechaEntregado = fechaEntregado; }
    public boolean isEntregaVerificadaQR() { return entregaVerificadaQR != null && entregaVerificadaQR; }
    public void setEntregaVerificadaQR(Boolean entregaVerificadaQR) { this.entregaVerificadaQR = entregaVerificadaQR; }
    public String getDedicatoria() { return dedicatoria; }
    public void setDedicatoria(String dedicatoria) { this.dedicatoria = dedicatoria; }
    public String getTokenQr() { return tokenQr; }
    public void setTokenQr(String tokenQr) { this.tokenQr = tokenQr; }
    public List<HistorialPedido> getHistorial() { return historial; }
    public void setHistorial(List<HistorialPedido> historial) { this.historial = historial; }
}
