package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.ArregloFloralDao;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.repository.ArregloFloralRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ArregloFloralDaoImpl extends BaseDaoImpl<ArregloFloral, Integer> implements ArregloFloralDao {

    public ArregloFloralDaoImpl(ArregloFloralRepository repository) {
        super(repository);
    }
}
