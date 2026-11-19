package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.ListaDeseoArregloFloralDao;
import com.neksbyte.porelgustoweb.model.ListaDeseoArregloFloral;
import com.neksbyte.porelgustoweb.model.ListaDeseoArregloFloralId;
import com.neksbyte.porelgustoweb.service.ListaDeseoArregloFloralService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ListaDeseoArregloFloralServiceImpl extends BaseServiceImpl<ListaDeseoArregloFloral, ListaDeseoArregloFloralId> implements ListaDeseoArregloFloralService {

    public ListaDeseoArregloFloralServiceImpl(ListaDeseoArregloFloralDao dao) {
        super(dao);
    }
}
