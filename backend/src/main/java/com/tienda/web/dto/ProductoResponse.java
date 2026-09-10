package com.tienda.web.dto;

import com.tienda.domain.Producto;

public record ProductoResponse(
        String sku,
        String nombre,
        String descripcion,
        double unidadesDisponibles,
        double precioUnitario,
        String imagenUrl,
        boolean activo) {

    public static ProductoResponse from(Producto producto) {
        return new ProductoResponse(
                producto.getSku(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getUnidadesDisponibles(),
                producto.getPrecioUnitario(),
                producto.getImagenUrl(),
                producto.isActivo());
    }
}
