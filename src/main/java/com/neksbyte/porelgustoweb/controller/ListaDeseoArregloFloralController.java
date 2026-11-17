package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.model.ListaDeseoArregloFloral;
import com.neksbyte.porelgustoweb.model.ListaDeseoArregloFloralId;
import com.neksbyte.porelgustoweb.service.ListaDeseoArregloFloralService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas-deseo-arreglos")
public class ListaDeseoArregloFloralController {
    private final ListaDeseoArregloFloralService service;

    public ListaDeseoArregloFloralController(ListaDeseoArregloFloralService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ListaDeseoArregloFloral>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{idLista}/{idArregloFloral}")
    public ResponseEntity<ListaDeseoArregloFloral> findById(
            @PathVariable Integer idLista, @PathVariable Integer idArregloFloral) {
        return service.findById(new ListaDeseoArregloFloralId(idLista, idArregloFloral))
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ListaDeseoArregloFloral> create(@RequestBody ListaDeseoArregloFloral entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @PutMapping("/{idLista}/{idArregloFloral}")
    public ResponseEntity<ListaDeseoArregloFloral> update(
            @PathVariable Integer idLista, @PathVariable Integer idArregloFloral,
            @RequestBody ListaDeseoArregloFloral entity) {
        ListaDeseoArregloFloralId id = new ListaDeseoArregloFloralId(idLista, idArregloFloral);
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        entity.setId(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @DeleteMapping("/{idLista}/{idArregloFloral}")
    public ResponseEntity<Void> delete(@PathVariable Integer idLista, @PathVariable Integer idArregloFloral) {
        ListaDeseoArregloFloralId id = new ListaDeseoArregloFloralId(idLista, idArregloFloral);
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
