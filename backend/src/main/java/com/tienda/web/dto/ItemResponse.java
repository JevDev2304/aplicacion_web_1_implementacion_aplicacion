package com.tienda.web.dto;

import com.tienda.domain.Item;
import com.tienda.domain.reglas.ManejadorReglas;

public record ItemResponse(
        Long id,
        String sku,
        String nombreProducto,
        double cantidad,
        double total) {

    public static ItemResponse from(Item item, ManejadorReglas manejadorReglas) {
        return new ItemResponse(
                item.getId(),
                item.getProducto().getSku(),
                item.getProducto().getNombre(),
                item.getCantidad(),
                item.calcularTotal(manejadorReglas));
    }
}
