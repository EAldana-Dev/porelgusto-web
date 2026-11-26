package com.neksbyte.porelgustoweb.model;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Integer idUsuario;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "password", length = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "correoElectronico", length = 150)
    private String correoElectronico;

    @Column(name = "telefono", length = 50)
    private String telefono;

    @Column(name = "fechaRegistro")
    private LocalDate fechaRegistro;

    // El rol se usa en cada página (menú y permisos), así que se carga junto con el usuario.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idTipoUsuario")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private TipoUsuario tipoUsuario;

    @OneToMany(mappedBy = "usuario")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Contacto> contactos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<MetodoPago> metodosPago = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ListaDeDeseos> listasDeDeseos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<CarritoDeCompra> carritosDeCompra = new ArrayList<>();

    // ---------- Datos adicionales de la cuenta ----------
    @Column(name = "fechaNacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "activo")
    private Boolean activo;

    public Usuario() {
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }

    public List<MetodoPago> getMetodosPago() {
        return metodosPago;
    }

    public void setMetodosPago(List<MetodoPago> metodosPago) {
        this.metodosPago = metodosPago;
    }

    public List<ListaDeDeseos> getListasDeDeseos() {
        return listasDeDeseos;
    }

    public void setListasDeDeseos(List<ListaDeDeseos> listasDeDeseos) {
        this.listasDeDeseos = listasDeDeseos;
    }

    public List<CarritoDeCompra> getCarritosDeCompra() {
        return carritosDeCompra;
    }

    public void setCarritosDeCompra(List<CarritoDeCompra> carritosDeCompra) {
        this.carritosDeCompra = carritosDeCompra;
    }

    // ---------- Rol, permisos y nombre para las vistas ----------

    /** Cuenta habilitada (los registros anteriores sin valor cuentan como activos). */
    public boolean isActivo() { return activo == null || activo; }

    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }

    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public int getIdRol() {
        return tipoUsuario == null || tipoUsuario.getIdTipoUsuario() == null ? Permisos.CLIENTE : tipoUsuario.getIdTipoUsuario();
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getRol() { return tipoUsuario == null ? "Cliente" : tipoUsuario.getRol(); }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isAdmin() { return getIdRol() == Permisos.ADMINISTRADOR; }

    /** Administrador, empleado o repartidor: puede entrar al panel. */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isPersonal() { return getIdRol() != Permisos.CLIENTE; }

    public boolean tienePermiso(String modulo) {
        return isActivo() && Permisos.de(getIdRol()).contains(modulo.toUpperCase(Locale.ROOT));
    }

    /** "Ana María López" -> "Ana" */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getPrimerNombre() {
        if (nombre == null || nombre.isBlank()) return "";
        return nombre.trim().split("\\s+")[0];
    }

    /** "Ana María López" -> "AL" */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getIniciales() {
        if (nombre == null || nombre.isBlank()) return "?";
        String[] p = nombre.trim().split("\\s+");
        String ini = p[0].substring(0, 1) + (p.length > 1 ? p[p.length - 1].substring(0, 1) : "");
        return ini.toUpperCase(Locale.ROOT);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario that)) return false;
        return idUsuario != null && idUsuario.equals(that.idUsuario);
    }

    @Override
    public int hashCode() { return java.util.Objects.hashCode(idUsuario); }
}
