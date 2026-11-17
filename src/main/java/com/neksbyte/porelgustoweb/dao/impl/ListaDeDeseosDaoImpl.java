package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.ListaDeDeseosDao;
import com.neksbyte.porelgustoweb.model.ListaDeDeseos;
import com.neksbyte.porelgustoweb.repository.ListaDeDeseosRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ListaDeDeseosDaoImpl extends BaseDaoImpl<ListaDeDeseos, Integer> implements ListaDeDeseosDao {

    public ListaDeDeseosDaoImpl(ListaDeDeseosRepository repository) {
        super(repository);
    }
}
