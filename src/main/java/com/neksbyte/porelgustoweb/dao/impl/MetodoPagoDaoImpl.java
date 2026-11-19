package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.MetodoPagoDao;
import com.neksbyte.porelgustoweb.model.MetodoPago;
import com.neksbyte.porelgustoweb.repository.MetodoPagoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MetodoPagoDaoImpl extends BaseDaoImpl<MetodoPago, Integer> implements MetodoPagoDao {

    public MetodoPagoDaoImpl(MetodoPagoRepository repository) {
        super(repository);
    }
}
