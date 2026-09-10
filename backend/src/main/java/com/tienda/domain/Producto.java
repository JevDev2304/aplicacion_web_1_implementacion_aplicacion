package com.tienda.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    private String sku;

    private String nombre;

    private String descripcion;

    private double unidadesDisponibles;

    private double precioUnitario;

    protected Producto() {
    }

    public Producto(String sku, String nombre, String descripcion, double unidadesDisponibles, double precioUnitario) {
        this.sku = sku;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.unidadesDisponibles = unidadesDisponibles;
        this.precioUnitario = precioUnitario;
    }

    public boolean tieneUnidades(double cantidad) {
        return unidadesDisponibles >= cantidad;
    }

    public void descontarUnidades(double cantidad) {
        if (!tieneUnidades(cantidad)) {
            throw new IllegalStateException("No hay unidades suficientes disponibles del producto " + sku);
        }
        unidadesDisponibles -= cantidad;
    }

    public String getSku() {
        return sku;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getUnidadesDisponibles() {
        return unidadesDisponibles;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }
}
