package com.tienda.domain.reglas;

import org.springframework.stereotype.Component;

@Component
public class ReglaPrecioEspecial implements ReglaPrecio {

    private static final String PREFIJO_SKU = "SP";
    private static final int UNIDADES_POR_TRAMO_DESCUENTO = 3;
    private static final double DESCUENTO_POR_TRAMO = 0.20;
    private static final double DESCUENTO_MAXIMO = 0.50;

    @Override
    public boolean esAplicable(String sku) {
        return sku != null && sku.startsWith(PREFIJO_SKU);
    }

    @Override
    public double calcularTotal(double cantidad, double precioUnitario) {
        int tramos = (int) (cantidad / UNIDADES_POR_TRAMO_DESCUENTO);
        double descuento = Math.min(tramos * DESCUENTO_POR_TRAMO, DESCUENTO_MAXIMO);
        return cantidad * precioUnitario * (1 - descuento);
    }
}
