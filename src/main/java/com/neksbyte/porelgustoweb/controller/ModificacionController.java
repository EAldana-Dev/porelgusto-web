package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.model.Modificacion;
import com.neksbyte.porelgustoweb.service.ModificacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modificaciones")
public class ModificacionController {
    private final ModificacionService service;

    public ModificacionController(ModificacionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Modificacion>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Modificacion> findById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Modificacion> create(@RequestBody Modificacion entity) {
        Modificacion created = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Modificacion> update(@PathVariable Integer id, @RequestBody Modificacion entity) {
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        entity.setIdModificacion(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
