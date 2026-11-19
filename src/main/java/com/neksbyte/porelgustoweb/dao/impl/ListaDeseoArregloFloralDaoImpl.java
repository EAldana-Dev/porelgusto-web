package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.ListaDeseoArregloFloralDao;
import com.neksbyte.porelgustoweb.model.ListaDeseoArregloFloral;
import com.neksbyte.porelgustoweb.model.ListaDeseoArregloFloralId;
import com.neksbyte.porelgustoweb.repository.ListaDeseoArregloFloralRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ListaDeseoArregloFloralDaoImpl extends BaseDaoImpl<ListaDeseoArregloFloral, ListaDeseoArregloFloralId> implements ListaDeseoArregloFloralDao {

    public ListaDeseoArregloFloralDaoImpl(ListaDeseoArregloFloralRepository repository) {
        super(repository);
    }
}
