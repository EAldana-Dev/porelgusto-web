package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.TipoUsuarioDao;
import com.neksbyte.porelgustoweb.model.TipoUsuario;
import com.neksbyte.porelgustoweb.service.TipoUsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TipoUsuarioServiceImpl extends BaseServiceImpl<TipoUsuario, Integer> implements TipoUsuarioService {

    public TipoUsuarioServiceImpl(TipoUsuarioDao dao) {
        super(dao);
    }
}
