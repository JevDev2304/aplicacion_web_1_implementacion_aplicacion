package com.tienda.service;

import com.tienda.domain.Carrito;
import com.tienda.domain.EstadoCarrito;
import com.tienda.domain.Item;
import com.tienda.domain.Venta;
import com.tienda.domain.reglas.ManejadorReglas;
import com.tienda.repository.CarritoRepository;
import com.tienda.repository.VentaRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TiendaService {

    private final CarritoRepository carritoRepository;
    private final VentaRepository ventaRepository;
    private final ManejadorReglas manejadorReglas;

    public TiendaService(CarritoRepository carritoRepository, VentaRepository ventaRepository,
            ManejadorReglas manejadorReglas) {
        this.carritoRepository = carritoRepository;
        this.ventaRepository = ventaRepository;
        this.manejadorReglas = manejadorReglas;
    }

    @Transactional
    public Venta finalizarCompra(UUID usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioIdAndEstado(usuarioId, EstadoCarrito.ABIERTO)
                .orElseThrow(() -> new IllegalStateException("No hay un carrito abierto para el usuario " + usuarioId));
        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        double total = carrito.calcularTotal(manejadorReglas);
        for (Item item : carrito.getItems()) {
            item.getProducto().descontarUnidades(item.getCantidad());
        }
        carrito.cerrar();
        carritoRepository.save(carrito);

        return ventaRepository.save(new Venta(carrito.getUsuario(), total));
    }

    public double totalVentas() {
        return ventaRepository.totalVentas();
    }
}
