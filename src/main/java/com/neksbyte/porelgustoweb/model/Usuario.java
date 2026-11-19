package com.neksbyte.porelgustoweb.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    @Column(name = "idUsuario")
    private Integer idUsuario;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "correoElectronico", length = 150)
    private String correoElectronico;

    @Column(name = "telefono", length = 50)
    private String telefono;

    @Column(name = "fechaRegistro")
    private LocalDate fechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTipoUsuario")
    private TipoUsuario tipoUsuario;

    @OneToMany(mappedBy = "usuario")
    private List<Contacto> contactos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<MetodoPago> metodosPago = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<ListaDeDeseos> listasDeDeseos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<CarritoDeCompra> carritosDeCompra = new ArrayList<>();

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

}
