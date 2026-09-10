package com.tienda.web.dto;

public record ProductoRequest(
        String sku,
        String nombre,
        String descripcion,
        double unidadesDisponibles,
        double precioUnitario,
        String imagenUrl) {
}
