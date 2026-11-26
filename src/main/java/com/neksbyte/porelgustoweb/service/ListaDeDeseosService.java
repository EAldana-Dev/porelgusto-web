package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.model.ListaDeDeseos;
import com.neksbyte.porelgustoweb.model.Usuario;

import java.util.List;
import java.util.Set;

public interface ListaDeDeseosService extends BaseService<ListaDeDeseos, Integer> {
    /** Productos guardados por el usuario (los más recientes primero). */
    List<ArregloFloral> productosDe(Usuario u);

    /** SKUs guardados (para pintar el corazón lleno en el catálogo). */
    Set<String> skus(Usuario u);

    /** Regresa true si se agregó, false si ya estaba. */
    boolean agregar(Usuario u, ArregloFloral p);

    void quitar(Usuario u, ArregloFloral p);

    void quitarDeTodas(ArregloFloral p);

    /** Avisa (notificación + correo) a quienes esperaban que el producto volviera. */
    void avisarDisponible(ArregloFloral p);
}
