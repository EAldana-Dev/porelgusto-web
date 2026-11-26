package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface CuponRepository extends JpaRepository<Cupon, Integer> {
    Optional<Cupon> findFirstByCodigoIgnoreCase(String codigo);
    List<Cupon> findAllByOrderByIdCuponDesc();
}
