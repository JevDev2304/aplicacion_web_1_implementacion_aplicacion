package com.tienda.web;

import com.tienda.service.ProductoService;
import com.tienda.web.dto.ProductoRequest;
import com.tienda.web.dto.ProductoResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoResponse> listar() {
        return productoService.listar().stream().map(ProductoResponse::from).toList();
    }

    @GetMapping("/{sku}")
    public ProductoResponse obtener(@PathVariable String sku) {
        return ProductoResponse.from(productoService.obtener(sku));
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@RequestBody ProductoRequest request) {
        var producto = productoService.crear(request.sku(), request.nombre(), request.descripcion(),
                request.unidadesDisponibles(), request.precioUnitario(), request.imagenUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoResponse.from(producto));
    }

    @PutMapping("/{sku}")
    public ProductoResponse actualizar(@PathVariable String sku, @RequestBody ProductoRequest request) {
        var producto = productoService.actualizar(sku, request.nombre(), request.descripcion(),
                request.unidadesDisponibles(), request.precioUnitario(), request.imagenUrl());
        return ProductoResponse.from(producto);
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> eliminar(@PathVariable String sku) {
        productoService.eliminar(sku);
        return ResponseEntity.noContent().build();
    }
}
