package com.tienda.domain.reglas;

public interface ReglaPrecio {

    boolean esAplicable(String sku);

    double calcularTotal(double cantidad, double precioUnitario);
}
