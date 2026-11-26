package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.dto.CompraEnProceso;
import com.neksbyte.porelgustoweb.dto.FacturaForm;
import com.neksbyte.porelgustoweb.model.EstadoPedido;
import com.neksbyte.porelgustoweb.model.Factura;
import com.neksbyte.porelgustoweb.model.Pedido;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.service.pago.ResultadoPago;

import java.math.BigDecimal;
import java.util.List;

/** Pedidos: compra, consulta, etapas, reparto, cancelaciones, dedicatorias con QR y facturas. */
public interface PedidoService extends BaseService<Pedido, Integer> {
    /**
     * Registra el pedido después de un pago aprobado (estado Pendiente). Aparta inventario,
     * guarda el carrito, genera el ticket y el QR de la dedicatoria.
     */
    Pedido registrarCompra(CompraEnProceso compra, Usuario usuario, ResultadoPago resultado);

    Pedido porFolio(String folio);
    List<Pedido> deCliente(Usuario u);
    List<Pedido> todos(EstadoPedido filtro);
    List<Pedido> asignadosA(Usuario empleado);
    Pedido porTokenQr(String token);
    Pedido porGuia(String guia);
    /** ¿Puede este usuario ver el pedido? (dueño o personal de la tienda) */
    boolean puedeVer(Pedido o, Usuario u);

    /** Avanza a la siguiente etapa (pendiente → preparando → en camino → entregado) o cancela. */
    void actualizarEstado(String folio, EstadoPedido nuevo, Usuario por, String nota, String repartidor, String telefonoRepartidor, String recibio);
    void reportarFallo(String folio, Usuario por, String descripcion);
    void asignar(String folio, Integer idEmpleado, Usuario por);
    /** El repartidor escanea el QR del pedido para confirmar la entrega. */
    Pedido verificarEntregaQR(String token, String recibio, Usuario por);
    /** Telemetría: el repartidor envía su ubicación. */
    Pedido actualizarUbicacion(String guia, Double latitud, Double longitud, Usuario por);

    Factura solicitarFactura(String folio, FacturaForm form);

    long contar(EstadoPedido estado);
    BigDecimal totalVendido();
}
