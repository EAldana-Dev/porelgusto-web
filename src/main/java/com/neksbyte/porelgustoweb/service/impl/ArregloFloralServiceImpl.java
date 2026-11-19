package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.ArregloFloralDao;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.service.ArregloFloralService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArregloFloralServiceImpl extends BaseServiceImpl<ArregloFloral, Integer> implements ArregloFloralService {

    public ArregloFloralServiceImpl(ArregloFloralDao dao) {
        super(dao);
    }
}
