package com.neksbyte.porelgustoweb.model;

import java.util.Arrays;
import java.util.List;

/**
 * Etapas del pedido. Se guardan como texto en Pedido.estadoEnvio con su etiqueta
 * ("Pendiente", "Preparando", "En camino", "Entregado"...), igual que en los datos existentes.
 */
public enum EstadoPedido {
    PENDIENTE_PAGO("Pendiente de pago", "warning"),
    PENDIENTE("Pendiente", "secondary"),
    PREPARANDO("Preparando", "info"),
    EN_CAMINO("En camino", "primary"),
    ENTREGADO("Entregado", "success"),
    CANCELADO("Cancelado", "danger");

    private final String etiqueta;
    private final String color;

    EstadoPedido(String etiqueta, String color) {
        this.etiqueta = etiqueta;
        this.color = color;
    }

    public String getEtiqueta() { return etiqueta; }

    /** Color del badge de Bootstrap. */
    public String getColor() { return color; }

    /** Siguiente etapa permitida (o null si ya terminó). */
    public EstadoPedido siguiente() {
        return switch (this) {
            case PENDIENTE_PAGO -> PENDIENTE;
            case PENDIENTE -> PREPARANDO;
            case PREPARANDO -> EN_CAMINO;
            case EN_CAMINO -> ENTREGADO;
            default -> null;
        };
    }

    public boolean esFinal() { return this == ENTREGADO || this == CANCELADO; }

    /** Etapas de la línea de tiempo del seguimiento. */
    public static List<EstadoPedido> etapas() { return List.of(PENDIENTE, PREPARANDO, EN_CAMINO, ENTREGADO); }

    /** Convierte el texto guardado ("En camino", "EN_CAMINO", "Aceptado"...) en la etapa. */
    public static EstadoPedido desde(String texto) {
        if (texto == null || texto.isBlank()) return PENDIENTE;
        String t = texto.trim();
        if (t.equalsIgnoreCase("Aceptado")) return PREPARANDO;
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(t) || e.etiqueta.equalsIgnoreCase(t))
                .findFirst().orElse(PENDIENTE);
    }
}
