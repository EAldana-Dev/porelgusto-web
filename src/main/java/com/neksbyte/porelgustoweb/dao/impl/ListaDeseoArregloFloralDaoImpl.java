package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.ListaDeseoArregloFloralDao;
import com.neksbyte.porelgustoweb.model.ListaDeseoArregloFloral;
import com.neksbyte.porelgustoweb.model.ListaDeseoArregloFloralId;
import com.neksbyte.porelgustoweb.repository.ListaDeseoArregloFloralRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.model.ListaDeDeseos;


@Repository
public class ListaDeseoArregloFloralDaoImpl extends BaseDaoImpl<ListaDeseoArregloFloral, ListaDeseoArregloFloralId> implements ListaDeseoArregloFloralDao {

    private final ListaDeseoArregloFloralRepository repo;

    public ListaDeseoArregloFloralDaoImpl(ListaDeseoArregloFloralRepository repository) {
        super(repository);
        this.repo = repository;
    }

    @Override
    public List<ListaDeseoArregloFloral> findByLista(ListaDeDeseos lista) {
        return repo.findByListaDeDeseos(lista);
    }

    @Override
    public List<ListaDeseoArregloFloral> findByArreglo(ArregloFloral arreglo) {
        return repo.findByArregloFloral(arreglo);
    }

    @Override
    public void delete(ListaDeseoArregloFloral entidad) {
        repo.delete(entidad);
    }
}
