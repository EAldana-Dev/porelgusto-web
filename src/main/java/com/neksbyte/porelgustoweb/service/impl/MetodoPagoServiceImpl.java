package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.MetodoPagoDao;
import com.neksbyte.porelgustoweb.model.MetodoPago;
import com.neksbyte.porelgustoweb.service.MetodoPagoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MetodoPagoServiceImpl extends BaseServiceImpl<MetodoPago, Integer> implements MetodoPagoService {

    public MetodoPagoServiceImpl(MetodoPagoDao dao) {
        super(dao);
    }
}
