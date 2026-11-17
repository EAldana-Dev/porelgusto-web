package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.TipoUsuarioDao;
import com.neksbyte.porelgustoweb.model.TipoUsuario;
import com.neksbyte.porelgustoweb.repository.TipoUsuarioRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TipoUsuarioDaoImpl extends BaseDaoImpl<TipoUsuario, Integer> implements TipoUsuarioDao {

    public TipoUsuarioDaoImpl(TipoUsuarioRepository repository) {
        super(repository);
    }
}
