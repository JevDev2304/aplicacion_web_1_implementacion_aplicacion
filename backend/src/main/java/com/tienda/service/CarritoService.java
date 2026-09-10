package com.tienda.service;

import com.tienda.domain.Carrito;
import com.tienda.domain.EstadoCarrito;
import com.tienda.domain.Item;
import com.tienda.domain.Producto;
import com.tienda.domain.Usuario;
import com.tienda.repository.CarritoRepository;
import com.tienda.repository.ItemRepository;
import com.tienda.repository.ProductoRepository;
import com.tienda.repository.UsuarioRepository;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final ItemRepository itemRepository;

    public CarritoService(CarritoRepository carritoRepository, UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository, ItemRepository itemRepository) {
        this.carritoRepository = carritoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public Carrito obtenerOCrearCarrito(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseGet(() -> usuarioRepository.save(new Usuario(usuarioId)));
        return carritoRepository.findByUsuarioIdAndEstado(usuarioId, EstadoCarrito.ABIERTO)
                .orElseGet(() -> carritoRepository.save(new Carrito(usuario)));
    }

    @Transactional
    public Carrito agregarItem(UUID usuarioId, String sku, double cantidad) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        Producto producto = productoRepository.findById(sku)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + sku));
        carrito.agregarItem(producto, cantidad);
        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito eliminarItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Ítem no encontrado: " + itemId));
        Carrito carrito = item.getCarrito();
        carrito.borrarItem(item);
        return carritoRepository.save(carrito);
    }
}
