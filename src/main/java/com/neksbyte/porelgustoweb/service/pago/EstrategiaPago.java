package com.neksbyte.porelgustoweb.service.pago;

import com.neksbyte.porelgustoweb.dto.CompraEnProceso;
import com.neksbyte.porelgustoweb.model.FormaPago;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Patrón Strategy: cada pasarela (tarjeta o PayPal) implementa la misma interfaz,
 * así el pedido no depende de qué servicio cobra.
 */
public interface EstrategiaPago {
    FormaPago forma();

    /** true si no hay claves configuradas y el pago se simula (solo con app.pagos.simulados=true). */
    boolean isSimulado();

    /** Hay llaves configuradas: cobra de verdad (o en el modo de prueba de la pasarela). */
    boolean isConfigurado();

    /** Inicia el pago y regresa la URL a la que hay que llevar al usuario (pasarela o simulación). */
    String iniciar(CompraEnProceso compra, String urlBase);

    /** Confirma el pago cuando el usuario regresa (o envía el formulario simulado). */
    ResultadoPago confirmar(CompraEnProceso compra, Map<String, String> parametros);

    /** Devolución (por ejemplo, al cancelar un pedido pagado). */
    boolean reembolsar(String transaccion, BigDecimal monto);
}
