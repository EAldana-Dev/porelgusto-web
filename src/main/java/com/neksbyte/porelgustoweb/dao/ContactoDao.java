package com.neksbyte.porelgustoweb.dao;

import com.neksbyte.porelgustoweb.model.Contacto;
import java.util.List;
import java.util.Optional;
import com.neksbyte.porelgustoweb.model.Usuario;


public interface ContactoDao extends BaseDao<Contacto, Integer> {
    List<Contacto> findByUsuario(Usuario usuario);
}
