package com.neksbyte.porelgustoweb.dao;

import com.neksbyte.porelgustoweb.model.Usuario;
import java.util.List;
import java.util.Optional;


public interface UsuarioDao extends BaseDao<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findAllByNombre(String nombre);
    boolean existsByCorreo(String correo);
    List<Usuario> findByTipos(java.util.Collection<Integer> tipos);
    List<Usuario> findAllRecientes();
}
