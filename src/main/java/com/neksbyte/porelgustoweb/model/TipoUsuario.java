package com.neksbyte.porelgustoweb.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TipoUsuario")
public class TipoUsuario {
    @Id
    @Column(name = "idTipoUsuario")
    private Integer idTipoUsuario;

    @Column(name = "rol", length = 100)
    private String rol;

    @OneToMany(mappedBy = "tipoUsuario")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Usuario> usuarios = new ArrayList<>();

    public TipoUsuario() {
    }

    public Integer getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(Integer idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}
