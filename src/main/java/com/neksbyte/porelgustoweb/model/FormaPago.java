package com.neksbyte.porelgustoweb.model;

/** Formas de pago que acepta la tienda (se guardan en Pedido.formaPago y en MetodoPago.tipoPago). */
public enum FormaPago {
    TARJETA("Tarjeta de crédito o débito"), PAYPAL("PayPal"), WHATSAPP("WhatsApp");

    private final String etiqueta;

    FormaPago(String etiqueta) { this.etiqueta = etiqueta; }

    public String getEtiqueta() { return etiqueta; }
}
