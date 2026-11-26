package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.PedidoDao;
import com.neksbyte.porelgustoweb.model.Pedido;
import com.neksbyte.porelgustoweb.repository.PedidoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.neksbyte.porelgustoweb.model.Usuario;


@Repository
public class PedidoDaoImpl extends BaseDaoImpl<Pedido, Integer> implements PedidoDao {

    private final PedidoRepository repo;

    public PedidoDaoImpl(PedidoRepository repository) {
        super(repository);
        this.repo = repository;
    }

    @Override
    public Optional<Pedido> findByFolio(String folio) {
        return repo.findByFolio(folio);
    }

    @Override
    public Optional<Pedido> findByTokenQr(String tokenQr) {
        return repo.findByTokenQr(tokenQr);
    }

    @Override
    public Optional<Pedido> findByNumeroGuia(String numeroGuia) {
        return repo.findByNumeroGuia(numeroGuia);
    }

    @Override
    public List<Pedido> findAllRecientes() {
        return repo.findAllByOrderByIdPedidoDesc();
    }

    @Override
    public List<Pedido> findByEmpleado(Usuario empleado) {
        return repo.findByEmpleadoOrderByIdPedidoDesc(empleado);
    }

    @Override
    public List<Pedido> findByCliente(Usuario cliente) {
        return repo.deCliente(cliente);
    }
}
