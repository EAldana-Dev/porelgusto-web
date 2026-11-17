package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.FacturaDao;
import com.neksbyte.porelgustoweb.model.Factura;
import com.neksbyte.porelgustoweb.service.FacturaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FacturaServiceImpl extends BaseServiceImpl<Factura, Integer> implements FacturaService {

    public FacturaServiceImpl(FacturaDao dao) {
        super(dao);
    }
}
