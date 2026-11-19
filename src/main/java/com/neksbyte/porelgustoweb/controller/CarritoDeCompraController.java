package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.model.CarritoDeCompra;
import com.neksbyte.porelgustoweb.service.CarritoDeCompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carritos")
public class CarritoDeCompraController {
    private final CarritoDeCompraService service;

    public CarritoDeCompraController(CarritoDeCompraService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CarritoDeCompra>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoDeCompra> findById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CarritoDeCompra> create(@RequestBody CarritoDeCompra entity) {
        CarritoDeCompra created = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarritoDeCompra> update(@PathVariable Integer id, @RequestBody CarritoDeCompra entity) {
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        entity.setIdCarritoDeCompra(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!service.existsById(id)) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
