package com.neksbyte.porelgustoweb.dao;

import com.neksbyte.porelgustoweb.model.ListaDeDeseos;
import java.util.List;
import java.util.Optional;
import com.neksbyte.porelgustoweb.model.Usuario;


public interface ListaDeDeseosDao extends BaseDao<ListaDeDeseos, Integer> {
    Optional<ListaDeDeseos> findByUsuario(Usuario usuario);
}
