package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByCorreoElectronicoIgnoreCase(String correoElectronico);

    java.util.Optional<Usuario> findByCorreoElectronicoIgnoreCase(String correoElectronico);

    java.util.List<Usuario> findAllByNombreIgnoreCase(String nombre);
}
