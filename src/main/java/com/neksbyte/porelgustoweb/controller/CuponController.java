package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.model.Cupon;
import com.neksbyte.porelgustoweb.service.CuponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cupones")
public class CuponController {
    private final CuponService service;

    public CuponController(CuponService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Cupon>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cupon> findById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cupon> create(@RequestBody Cupon entity) {
        Cupon created = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cupon> update(@PathVariable Integer id, @RequestBody Cupon entity) {
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        entity.setIdCupon(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
