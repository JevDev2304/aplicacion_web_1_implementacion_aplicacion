package com.tienda.domain.reglas;

import org.springframework.stereotype.Component;

@Component
public class ReglaPrecioNormal implements ReglaPrecio {

    private static final String PREFIJO_SKU = "EA";

    @Override
    public boolean esAplicable(String sku) {
        return sku != null && sku.startsWith(PREFIJO_SKU);
    }

    @Override
    public double calcularTotal(double cantidad, double precioUnitario) {
        return cantidad * precioUnitario;
    }
}
