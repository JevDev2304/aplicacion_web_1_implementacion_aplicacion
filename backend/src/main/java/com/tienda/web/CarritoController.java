package com.tienda.web;

import com.tienda.domain.Carrito;
import com.tienda.domain.Venta;
import com.tienda.domain.reglas.ManejadorReglas;
import com.tienda.service.CarritoService;
import com.tienda.service.TiendaService;
import com.tienda.web.dto.AgregarItemRequest;
import com.tienda.web.dto.CarritoResponse;
import com.tienda.web.dto.CheckoutRequest;
import com.tienda.web.dto.VentaResponse;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    private final TiendaService tiendaService;
    private final ManejadorReglas manejadorReglas;

    public CarritoController(CarritoService carritoService, TiendaService tiendaService,
            ManejadorReglas manejadorReglas) {
        this.carritoService = carritoService;
        this.tiendaService = tiendaService;
        this.manejadorReglas = manejadorReglas;
    }

    @GetMapping
    public CarritoResponse obtener(@RequestParam UUID usuarioId) {
        Carrito carrito = carritoService.obtenerOCrearCarrito(usuarioId);
        return CarritoResponse.from(carrito, manejadorReglas);
    }

    @PostMapping("/items")
    public CarritoResponse agregarItem(@RequestBody AgregarItemRequest request) {
        Carrito carrito = carritoService.agregarItem(request.usuarioId(), request.sku(), request.cantidad());
        return CarritoResponse.from(carrito, manejadorReglas);
    }

    @DeleteMapping("/items/{itemId}")
    public CarritoResponse eliminarItem(@PathVariable Long itemId) {
        Carrito carrito = carritoService.eliminarItem(itemId);
        return CarritoResponse.from(carrito, manejadorReglas);
    }

    @PostMapping("/checkout")
    public VentaResponse checkout(@RequestBody CheckoutRequest request) {
        Venta venta = tiendaService.finalizarCompra(request.usuarioId());
        return VentaResponse.from(venta);
    }
}
