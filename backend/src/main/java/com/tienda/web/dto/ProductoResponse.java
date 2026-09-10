package com.tienda.web.dto;

import com.tienda.domain.Producto;

public record ProductoResponse(
        String sku,
        String nombre,
        String descripcion,
        double unidadesDisponibles,
        double precioUnitario) {

    public static ProductoResponse from(Producto producto) {
        return new ProductoResponse(
                producto.getSku(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getUnidadesDisponibles(),
                producto.getPrecioUnitario());
    }
}
