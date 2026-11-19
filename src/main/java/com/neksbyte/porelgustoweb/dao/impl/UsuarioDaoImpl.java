package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.UsuarioDao;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.repository.UsuarioRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDaoImpl extends BaseDaoImpl<Usuario, Integer> implements UsuarioDao {

    public UsuarioDaoImpl(UsuarioRepository repository) {
        super(repository);
    }
}
