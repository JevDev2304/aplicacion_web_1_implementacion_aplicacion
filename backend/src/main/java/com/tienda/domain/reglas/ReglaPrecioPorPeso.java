package com.tienda.domain.reglas;

import org.springframework.stereotype.Component;

@Component
public class ReglaPrecioPorPeso implements ReglaPrecio {

    private static final String PREFIJO_SKU = "WE";
    private static final double GRAMOS_POR_KILOGRAMO = 1000;

    @Override
    public boolean esAplicable(String sku) {
        return sku != null && sku.startsWith(PREFIJO_SKU);
    }

    @Override
    public double calcularTotal(double cantidad, double precioUnitario) {
        return precioUnitario * GRAMOS_POR_KILOGRAMO * cantidad;
    }
}
