package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.CarritoDeCompraDao;
import com.neksbyte.porelgustoweb.model.CarritoDeCompra;
import com.neksbyte.porelgustoweb.service.CarritoDeCompraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CarritoDeCompraServiceImpl extends BaseServiceImpl<CarritoDeCompra, Integer> implements CarritoDeCompraService {

    public CarritoDeCompraServiceImpl(CarritoDeCompraDao dao) {
        super(dao);
    }
}
