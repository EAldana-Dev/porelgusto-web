package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.ListaDeDeseosDao;
import com.neksbyte.porelgustoweb.model.ListaDeDeseos;
import com.neksbyte.porelgustoweb.repository.ListaDeDeseosRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.neksbyte.porelgustoweb.model.Usuario;


@Repository
public class ListaDeDeseosDaoImpl extends BaseDaoImpl<ListaDeDeseos, Integer> implements ListaDeDeseosDao {

    private final ListaDeDeseosRepository repo;

    public ListaDeDeseosDaoImpl(ListaDeDeseosRepository repository) {
        super(repository);
        this.repo = repository;
    }

    @Override
    public Optional<ListaDeDeseos> findByUsuario(Usuario usuario) {
        return repo.findFirstByUsuarioOrderByIdListaAsc(usuario);
    }
}
