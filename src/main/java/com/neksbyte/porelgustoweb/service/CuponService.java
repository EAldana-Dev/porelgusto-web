package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.dto.CuponForm;
import com.neksbyte.porelgustoweb.model.Cupon;

import java.math.BigDecimal;
import java.util.List;

public interface CuponService extends BaseService<Cupon, Integer> {
    /** Valida el código contra el subtotal del carrito. Lanza ReglaNegocioException con el motivo. */
    Cupon validar(String codigo, BigDecimal subtotal);

    /** Suma un uso al cupón (al registrar un pedido). */
    void registrarUso(Cupon cupon);

    List<Cupon> todosRecientes();

    Cupon porId(Integer id);

    Cupon guardar(CuponForm form);
}
