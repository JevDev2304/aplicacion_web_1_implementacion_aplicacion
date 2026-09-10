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
                "Cuaderno de 100 hojas cuadriculadas", 100, 8000,
                "https://www.scribe.com.co/cdn/shop/files/C30214563_020203df-9003-4712-b93d-6b6d959ee99a.jpg?v=1736999051"));
        productoRepository.save(new Producto("EA002", "Lapicero azul",
                "Lapicero de tinta azul punta fina", 200, 2000,
                "https://pgrancentral.com/cdn/shop/files/ESFEROOFFIESCO0.7MMSEMIGELCOLORES_3.png?v=1731680407"));
        productoRepository.save(new Producto("EA003", "Marcador borrable",
                "Marcador para tablero blanco", 40, 3500,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSB1-hQyB3q-I1FunavCaSRNvJGc0x0ox3LjFnSS7HJF5nVULLIUIvkx-g&s=10"));
        productoRepository.save(new Producto("WE001", "Café en grano",
                "Café colombiano en grano, precio dado por gramo", 15, 20,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQpu7Yr_aOC6jv3ueQ3LY6MjPjPbqPANUdYdas9du6AbYmDKNsjvKSe0m-w&s=10"));
        productoRepository.save(new Producto("SP001", "Audífonos Sony inalámbricos",
                "Audífonos con descuento por volumen de compra", 50, 100000,
                "https://www.sony.com.co/image/38ea5815d12ab90a45b9b1a35520b794?fmt=png-alpha"));
    }
}
