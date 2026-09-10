package com.tienda.seed;

import com.tienda.domain.Producto;
import com.tienda.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatosSeed implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    public DatosSeed(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {
        if (productoRepository.count() > 0) {
            return;
        }
        productoRepository.save(new Producto("EA001", "Cuaderno cuadriculado",
                "Cuaderno de 100 hojas cuadriculadas", 100, 8000));
        productoRepository.save(new Producto("EA002", "Esfero azul",
                "Esfero de tinta azul punta fina", 200, 2000));
        productoRepository.save(new Producto("WE001", "Café en grano",
                "Café colombiano en grano, precio dado por gramo", 15, 20));
        productoRepository.save(new Producto("SP001", "Audífonos inalámbricos",
                "Audífonos con descuento por volumen de compra", 50, 100000));
    }
}
