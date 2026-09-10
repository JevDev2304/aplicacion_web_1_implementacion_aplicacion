package com.tienda.service;

import com.tienda.domain.Producto;
import com.tienda.repository.ProductoRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto obtener(String sku) {
        return productoRepository.findById(sku)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + sku));
    }

    @Transactional
    public Producto crear(String sku, String nombre, String descripcion, double unidadesDisponibles,
            double precioUnitario, String imagenUrl) {
        if (productoRepository.existsById(sku)) {
            throw new IllegalStateException("Ya existe un producto con el SKU " + sku);
        }
        return productoRepository
                .save(new Producto(sku, nombre, descripcion, unidadesDisponibles, precioUnitario, imagenUrl));
    }

    @Transactional
    public Producto actualizar(String sku, String nombre, String descripcion, double unidadesDisponibles,
            double precioUnitario, String imagenUrl) {
        Producto producto = obtener(sku);
        producto.actualizar(nombre, descripcion, unidadesDisponibles, precioUnitario, imagenUrl);
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminar(String sku) {
        Producto producto = obtener(sku);
        producto.desactivar();
        productoRepository.save(producto);
    }

    @Transactional
    public Producto activar(String sku) {
        Producto producto = obtener(sku);
        producto.activar();
        return productoRepository.save(producto);
    }
}
