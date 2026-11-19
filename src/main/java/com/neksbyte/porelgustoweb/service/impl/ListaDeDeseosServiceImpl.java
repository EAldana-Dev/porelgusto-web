package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.ListaDeDeseosDao;
import com.neksbyte.porelgustoweb.model.ListaDeDeseos;
import com.neksbyte.porelgustoweb.service.ListaDeDeseosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ListaDeDeseosServiceImpl extends BaseServiceImpl<ListaDeDeseos, Integer> implements ListaDeDeseosService {

    public ListaDeDeseosServiceImpl(ListaDeDeseosDao dao) {
        super(dao);
    }
}
