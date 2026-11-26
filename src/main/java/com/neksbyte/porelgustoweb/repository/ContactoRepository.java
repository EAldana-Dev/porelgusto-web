package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import com.neksbyte.porelgustoweb.model.Usuario;
import java.util.Optional;
import java.util.List;

public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
    List<Contacto> findByUsuarioOrderByIdContactoDesc(Usuario usuario);
}
