package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.CuponDao;
import com.neksbyte.porelgustoweb.model.Cupon;
import com.neksbyte.porelgustoweb.repository.CuponRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CuponDaoImpl extends BaseDaoImpl<Cupon, Integer> implements CuponDao {

    public CuponDaoImpl(CuponRepository repository) {
        super(repository);
    }
}
