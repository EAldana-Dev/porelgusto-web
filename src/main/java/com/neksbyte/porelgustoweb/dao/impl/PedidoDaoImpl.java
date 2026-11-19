package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.PedidoDao;
import com.neksbyte.porelgustoweb.model.Pedido;
import com.neksbyte.porelgustoweb.repository.PedidoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PedidoDaoImpl extends BaseDaoImpl<Pedido, Integer> implements PedidoDao {

    public PedidoDaoImpl(PedidoRepository repository) {
        super(repository);
    }
}
