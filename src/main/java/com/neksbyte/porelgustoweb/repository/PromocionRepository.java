package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PromocionRepository extends JpaRepository<Promocion, Integer> {
    List<Promocion> findAllByOrderByFechaInicioDesc();

    @Query("select p from Promocion p where (p.activa is null or p.activa = true) and p.fechaInicio <= :ahora and p.fechaFin >= :ahora order by p.fechaInicio desc")
    List<Promocion> findVigentes(LocalDateTime ahora);
}
