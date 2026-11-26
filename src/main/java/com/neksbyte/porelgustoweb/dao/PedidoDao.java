package com.neksbyte.porelgustoweb.dao;

import com.neksbyte.porelgustoweb.model.Pedido;
import java.util.List;
import java.util.Optional;
import com.neksbyte.porelgustoweb.model.Usuario;


public interface PedidoDao extends BaseDao<Pedido, Integer> {
    Optional<Pedido> findByFolio(String folio);
    Optional<Pedido> findByTokenQr(String tokenQr);
    Optional<Pedido> findByNumeroGuia(String numeroGuia);
    List<Pedido> findAllRecientes();
    List<Pedido> findByEmpleado(Usuario empleado);
    List<Pedido> findByCliente(Usuario cliente);
}
