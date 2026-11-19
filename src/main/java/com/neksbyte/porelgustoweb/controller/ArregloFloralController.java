package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.service.ArregloFloralService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arreglos-florales")
public class ArregloFloralController {
    private final ArregloFloralService service;

    public ArregloFloralController(ArregloFloralService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ArregloFloral>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArregloFloral> findById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArregloFloral> create(@RequestBody ArregloFloral entity) {
        ArregloFloral created = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArregloFloral> update(@PathVariable Integer id, @RequestBody ArregloFloral entity) {
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        entity.setIdArregloFloral(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
