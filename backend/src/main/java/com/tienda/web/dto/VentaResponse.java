package com.tienda.web.dto;

import com.tienda.domain.Venta;
import java.time.Instant;

public record VentaResponse(Long id, double total, Instant creadoEn) {

    public static VentaResponse from(Venta venta) {
        return new VentaResponse(venta.getId(), venta.getTotal(), venta.getCreadoEn());
    }
}
