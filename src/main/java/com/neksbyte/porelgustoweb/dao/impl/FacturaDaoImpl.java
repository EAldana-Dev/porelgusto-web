package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.FacturaDao;
import com.neksbyte.porelgustoweb.model.Factura;
import com.neksbyte.porelgustoweb.repository.FacturaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class FacturaDaoImpl extends BaseDaoImpl<Factura, Integer> implements FacturaDao {

    public FacturaDaoImpl(FacturaRepository repository) {
        super(repository);
    }
}
