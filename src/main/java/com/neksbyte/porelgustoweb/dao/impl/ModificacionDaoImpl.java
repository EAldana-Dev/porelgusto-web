package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.ModificacionDao;
import com.neksbyte.porelgustoweb.model.Modificacion;
import com.neksbyte.porelgustoweb.repository.ModificacionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ModificacionDaoImpl extends BaseDaoImpl<Modificacion, Integer> implements ModificacionDao {

    public ModificacionDaoImpl(ModificacionRepository repository) {
        super(repository);
    }
}
