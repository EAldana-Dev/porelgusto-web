package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.CuponDao;
import com.neksbyte.porelgustoweb.model.Cupon;
import com.neksbyte.porelgustoweb.service.CuponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CuponServiceImpl extends BaseServiceImpl<Cupon, Integer> implements CuponService {

    public CuponServiceImpl(CuponDao dao) {
        super(dao);
    }
}
