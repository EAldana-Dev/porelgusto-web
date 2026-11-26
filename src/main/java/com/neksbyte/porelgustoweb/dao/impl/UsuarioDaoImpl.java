package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.UsuarioDao;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.repository.UsuarioRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public class UsuarioDaoImpl extends BaseDaoImpl<Usuario, Integer> implements UsuarioDao {

    private final UsuarioRepository repo;

    public UsuarioDaoImpl(UsuarioRepository repository) {
        super(repository);
        this.repo = repository;
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return repo.findByCorreoElectronicoIgnoreCase(correo);
    }

    @Override
    public List<Usuario> findAllByNombre(String nombre) {
        return repo.findAllByNombreIgnoreCase(nombre);
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return repo.existsByCorreoElectronicoIgnoreCase(correo);
    }

    @Override
    public List<Usuario> findByTipos(java.util.Collection<Integer> tipos) {
        return repo.findByTipoUsuario_IdTipoUsuarioIn(tipos);
    }

    @Override
    public List<Usuario> findAllRecientes() {
        return repo.findAllByOrderByIdUsuarioDesc();
    }
}
