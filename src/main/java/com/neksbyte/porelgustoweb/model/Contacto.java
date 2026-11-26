package com.neksbyte.porelgustoweb.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "Contacto")
public class Contacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idContacto")
    private Integer idContacto;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Column(name = "tipoContacto", length = 100)
    private String tipoContacto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Usuario usuario;

    // ---------- Dirección de entrega guardada (tipoContacto = alias: "Casa", "Oficina"...) ----------
    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "calle", length = 160)
    private String calle;

    @Column(name = "colonia", length = 80)
    private String colonia;

    @Column(name = "ciudad", length = 80)
    private String ciudad;

    @Column(name = "codigoPostal", length = 10)
    private String codigoPostal;

    @Column(name = "referencias", length = 200)
    private String referencias;

    @Column(name = "activo")
    private Boolean activo;

    public Contacto() {
    }

    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoContacto() {
        return tipoContacto;
    }

    public void setTipoContacto(String tipoContacto) {
        this.tipoContacto = tipoContacto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // ---------- Dirección guardada ----------

    public String getAlias() { return tipoContacto == null || tipoContacto.isBlank() ? nombre : tipoContacto; }

    /** Tiene los datos mínimos para usarse como dirección de entrega. */
    public boolean isDireccion() { return calle != null && !calle.isBlank(); }

    public boolean isActivo() { return activo == null || activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    /** "Laura López, Av. Insurgentes 1234, Col. Del Valle, Benito Juárez, C.P. 03100" */
    public String formatoEtiqueta() {
        return nombre + ", " + calle + (colonia == null || colonia.isBlank() ? "" : ", Col. " + colonia)
                + ", " + ciudad + ", C.P. " + codigoPostal;
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getColonia() { return colonia; }
    public void setColonia(String colonia) { this.colonia = colonia; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    public String getReferencias() { return referencias; }
    public void setReferencias(String referencias) { this.referencias = referencias; }
}
