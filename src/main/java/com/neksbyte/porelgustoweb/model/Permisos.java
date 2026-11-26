package com.neksbyte.porelgustoweb.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Permisos del panel de administración por tipo de usuario (tabla TipoUsuario):
 * 1 Cliente, 2 Administrador, 3 Empleado, 4 Repartidor.
 */
public final class Permisos {
    public static final int CLIENTE = 1;
    public static final int ADMINISTRADOR = 2;
    public static final int EMPLEADO = 3;
    public static final int REPARTIDOR = 4;

    /** Módulos del panel (el primer segmento de la URL después de /admin). */
    public static final List<String> MODULOS = List.of("DASHBOARD", "PEDIDOS", "PRODUCTOS", "CATALOGO", "PROMOCIONES", "USUARIOS", "SOPORTE", "QR",
            "ESTADISTICAS", "CONFIGURACION");

    private static final Map<Integer, List<String>> POR_ROL = Map.of(
            CLIENTE, List.of(),
            ADMINISTRADOR, MODULOS,
            EMPLEADO, List.of("DASHBOARD", "PEDIDOS", "SOPORTE", "QR"),
            REPARTIDOR, List.of("DASHBOARD", "PEDIDOS"));

    private static final Map<Integer, String> DESCRIPCIONES = new LinkedHashMap<>();
    static {
        DESCRIPCIONES.put(CLIENTE, "Compra, sigue sus pedidos y guarda su lista de deseos");
        DESCRIPCIONES.put(ADMINISTRADOR, "Acceso a todas las funciones administrativas");
        DESCRIPCIONES.put(EMPLEADO, "Atiende pedidos, soporte y genera códigos QR");
        DESCRIPCIONES.put(REPARTIDOR, "Entrega los pedidos que tiene asignados");
    }

    private Permisos() { }

    public static List<String> de(Integer idTipoUsuario) {
        return idTipoUsuario == null ? List.of() : POR_ROL.getOrDefault(idTipoUsuario, List.of());
    }

    public static String descripcion(Integer idTipoUsuario) {
        return idTipoUsuario == null ? "" : DESCRIPCIONES.getOrDefault(idTipoUsuario, "");
    }
}
