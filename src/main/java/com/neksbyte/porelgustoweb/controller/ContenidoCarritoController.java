package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.model.ContenidoCarrito;
import com.neksbyte.porelgustoweb.service.ContenidoCarritoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contenidos-carrito")
public class ContenidoCarritoController {
    private final ContenidoCarritoService service;

    public ContenidoCarritoController(ContenidoCarritoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ContenidoCarrito>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContenidoCarrito> findById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ContenidoCarrito> create(@RequestBody ContenidoCarrito entity) {
        ContenidoCarrito created = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContenidoCarrito> update(@PathVariable Integer id, @RequestBody ContenidoCarrito entity) {
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        entity.setIdContenido(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
