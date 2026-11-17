package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.model.ListaDeDeseos;
import com.neksbyte.porelgustoweb.service.ListaDeDeseosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas-deseos")
public class ListaDeDeseosController {
    private final ListaDeDeseosService service;

    public ListaDeDeseosController(ListaDeDeseosService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ListaDeDeseos>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaDeDeseos> findById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ListaDeDeseos> create(@RequestBody ListaDeDeseos entity) {
        ListaDeDeseos created = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaDeDeseos> update(@PathVariable Integer id, @RequestBody ListaDeDeseos entity) {
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        entity.setIdLista(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
