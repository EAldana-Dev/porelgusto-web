package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.ListaDeDeseosDao;
import com.neksbyte.porelgustoweb.dao.ListaDeseoArregloFloralDao;
import com.neksbyte.porelgustoweb.model.*;
import com.neksbyte.porelgustoweb.service.ListaDeDeseosService;
import com.neksbyte.porelgustoweb.service.NotificacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ListaDeDeseosServiceImpl extends BaseServiceImpl<ListaDeDeseos, Integer> implements ListaDeDeseosService {

    private final ListaDeDeseosDao listas;
    private final ListaDeseoArregloFloralDao elementos;
    private final NotificacionService notificaciones;

    public ListaDeDeseosServiceImpl(ListaDeDeseosDao dao, ListaDeseoArregloFloralDao elementos, NotificacionService notificaciones) {
        super(dao);
        this.listas = dao;
        this.elementos = elementos;
        this.notificaciones = notificaciones;
    }

    /** Cada usuario tiene una lista; se crea la primera vez. */
    private ListaDeDeseos listaDe(Usuario u) {
        return listas.findByUsuario(u).orElseGet(() -> {
            ListaDeDeseos l = new ListaDeDeseos();
            l.setUsuario(u);
            l.setFechaCreacion(LocalDate.now());
            return listas.save(l);
        });
    }

    private List<ListaDeseoArregloFloral> elementosDe(Usuario u) {
        return listas.findByUsuario(u).map(elementos::findByLista).orElse(List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArregloFloral> productosDe(Usuario u) {
        return elementosDe(u).stream()
                .sorted(Comparator.comparing(ListaDeseoArregloFloral::getFechaAgregado,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(ListaDeseoArregloFloral::getArregloFloral).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> skus(Usuario u) {
        if (u == null) return Set.of();
        return elementosDe(u).stream().map(e -> e.getArregloFloral().getSku()).collect(Collectors.toSet());
    }

    @Override
    public boolean agregar(Usuario u, ArregloFloral p) {
        ListaDeDeseos lista = listaDe(u);
        boolean ya = elementos.findByLista(lista).stream().anyMatch(e -> e.getArregloFloral().equals(p));
        if (ya) return false;
        ListaDeseoArregloFloral e = new ListaDeseoArregloFloral();
        e.setId(new ListaDeseoArregloFloralId(lista.getIdLista(), p.getIdArregloFloral()));
        e.setListaDeDeseos(lista);
        e.setArregloFloral(p);
        e.setAvisado(p.isDisponible());
        e.setFechaAgregado(LocalDateTime.now());
        elementos.save(e);
        notificaciones.notificar(u, "Agregaste " + p.getNombre() + " a tu lista de deseos",
                p.isDisponible() ? "Puedes comprarlo cuando quieras." : "Te avisaremos por correo cuando vuelva a estar disponible.",
                "/cuenta/deseos", false);
        return true;
    }

    @Override
    public void quitar(Usuario u, ArregloFloral p) {
        elementosDe(u).stream().filter(e -> e.getArregloFloral().equals(p)).forEach(elementos::delete);
    }

    @Override
    public void quitarDeTodas(ArregloFloral p) {
        elementos.findByArreglo(p).forEach(elementos::delete);
    }

    @Override
    public void avisarDisponible(ArregloFloral p) {
        for (ListaDeseoArregloFloral e : elementos.findByArreglo(p)) {
            if (e.isAvisado()) continue;
            notificaciones.notificar(e.getListaDeDeseos().getUsuario(), "¡" + p.getNombre() + " ya está disponible!",
                    "El producto que guardaste en tu lista de deseos ya se puede comprar.", "/producto/" + p.getSku(), true);
            e.setAvisado(true);
        }
    }
}
