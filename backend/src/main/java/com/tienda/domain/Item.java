package com.tienda.domain;

import com.tienda.domain.reglas.ManejadorReglas;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carrito_items")
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "producto_sku", nullable = false)
    private Producto producto;

    private double cantidad;

    protected Item() {
    }

    public Item(Carrito carrito, Producto producto, double cantidad) {
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public double calcularTotal(ManejadorReglas manejadorReglas) {
        return manejadorReglas.obtenerRegla(producto.getSku())
                .calcularTotal(cantidad, producto.getPrecioUnitario());
    }

    void incrementarCantidad(double cantidad) {
        this.cantidad += cantidad;
    }

    public Long getId() {
        return id;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public double getCantidad() {
        return cantidad;
    }
}
