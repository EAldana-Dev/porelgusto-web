package com.neksbyte.porelgustoweb.service.pago;

/** Respuesta de una pasarela de pago. */
public class ResultadoPago {
    private final boolean aprobado;
    private final String transaccion;
    private final String mensaje;
    private final String proveedor;
    /** Descripción del medio usado (p. ej. "Visa terminación 4242"); se guarda en MetodoPago. */
    private final String descripcion;

    private ResultadoPago(boolean aprobado, String transaccion, String mensaje, String proveedor, String descripcion) {
        this.aprobado = aprobado;
        this.transaccion = transaccion;
        this.mensaje = mensaje;
        this.proveedor = proveedor;
        this.descripcion = descripcion;
    }

    public static ResultadoPago aprobado(String transaccion, String proveedor) {
        return new ResultadoPago(true, transaccion, "Pago aprobado", proveedor, null);
    }

    public static ResultadoPago aprobado(String transaccion, String proveedor, String descripcion) {
        return new ResultadoPago(true, transaccion, "Pago aprobado", proveedor, descripcion);
    }

    public static ResultadoPago rechazado(String mensaje, String proveedor) {
        return new ResultadoPago(false, null, mensaje, proveedor, null);
    }

    public boolean isAprobado() { return aprobado; }
    public String getTransaccion() { return transaccion; }
    public String getMensaje() { return mensaje; }
    public String getProveedor() { return proveedor; }
    public String getDescripcion() { return descripcion; }
}
