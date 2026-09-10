package com.tienda.web.dto;

import com.tienda.domain.Carrito;
import com.tienda.domain.reglas.ManejadorReglas;
import java.util.List;

public record CarritoResponse(
        Long id,
        List<ItemResponse> items,
        double total) {

    public static CarritoResponse from(Carrito carrito, ManejadorReglas manejadorReglas) {
        List<ItemResponse> items = carrito.getItems().stream()
                .map(item -> ItemResponse.from(item, manejadorReglas))
                .toList();
        return new CarritoResponse(carrito.getId(), items, carrito.calcularTotal(manejadorReglas));
    }
}
