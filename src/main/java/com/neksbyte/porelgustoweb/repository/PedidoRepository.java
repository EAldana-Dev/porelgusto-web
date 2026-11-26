package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.neksbyte.porelgustoweb.model.Usuario;
import java.util.Optional;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    Optional<Pedido> findByFolio(String folio);
    Optional<Pedido> findByTokenQr(String tokenQr);
    Optional<Pedido> findByNumeroGuia(String numeroGuia);
    List<Pedido> findAllByOrderByIdPedidoDesc();
    List<Pedido> findByEmpleadoOrderByIdPedidoDesc(Usuario empleado);

    /** Pedidos del cliente (los antiguos se ligan por su carrito). */
    @Query("select p from Pedido p left join p.carritoDeCompra c where p.cliente = :u or c.usuario = :u order by p.idPedido desc")
    List<Pedido> deCliente(Usuario u);
}
