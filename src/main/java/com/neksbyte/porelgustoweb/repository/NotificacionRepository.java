package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.Notificacion;
import com.neksbyte.porelgustoweb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findTop30ByUsuarioOrderByFechaHoraDesc(Usuario usuario);

    @Query("select count(n) from Notificacion n where n.usuario = :usuario and (n.leida is null or n.leida = false)")
    long contarPendientes(Usuario usuario);
}
