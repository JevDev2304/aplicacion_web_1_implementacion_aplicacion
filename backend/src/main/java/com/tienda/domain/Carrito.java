package com.tienda.domain;

import com.tienda.domain.reglas.ManejadorReglas;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private EstadoCarrito estado = EstadoCarrito.ABIERTO;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    protected Carrito() {
    }

    public Carrito(Usuario usuario) {
        this.usuario = usuario;
    }

    public Item agregarItem(Producto producto, double cantidad) {
        if (!producto.tieneUnidades(cantidad)) {
            throw new IllegalStateException("No hay unidades suficientes disponibles del producto " + producto.getSku());
        }
        Optional<Item> itemExistente = items.stream()
                .filter(item -> item.getProducto().getSku().equals(producto.getSku()))
                .findFirst();
        if (itemExistente.isPresent()) {
            itemExistente.get().incrementarCantidad(cantidad);
            return itemExistente.get();
        }
        Item item = new Item(this, producto, cantidad);
        items.add(item);
        return item;
    }

    public void borrarItem(Item item) {
        items.remove(item);
    }

    public double calcularTotal(ManejadorReglas manejadorReglas) {
        return items.stream().mapToDouble(item -> item.calcularTotal(manejadorReglas)).sum();
    }

    public void cerrar() {
        this.estado = EstadoCarrito.CERRADO;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public EstadoCarrito getEstado() {
        return estado;
    }

    public List<Item> getItems() {
        return items;
    }
}
