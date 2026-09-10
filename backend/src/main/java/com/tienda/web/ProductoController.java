package com.tienda.web;

import com.tienda.repository.ProductoRepository;
import com.tienda.web.dto.ProductoResponse;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public List<ProductoResponse> listar() {
        return productoRepository.findAll().stream().map(ProductoResponse::from).toList();
    }

    @GetMapping("/{sku}")
    public ProductoResponse obtener(@PathVariable String sku) {
        return productoRepository.findById(sku)
                .map(ProductoResponse::from)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + sku));
    }
}
