package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByCorreoElectronicoIgnoreCase(String correoElectronico);

    java.util.Optional<Usuario> findByCorreoElectronicoIgnoreCase(String correoElectronico);

    java.util.List<Usuario> findAllByNombreIgnoreCase(String nombre);
    List<Usuario> findByTipoUsuario_IdTipoUsuarioIn(java.util.Collection<Integer> tipos);
    List<Usuario> findAllByOrderByIdUsuarioDesc();
}
