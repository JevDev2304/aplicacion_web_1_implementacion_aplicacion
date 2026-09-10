package com.tienda.domain.reglas;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ManejadorReglas {

    private final List<ReglaPrecio> reglas;

    public ManejadorReglas(List<ReglaPrecio> reglas) {
        this.reglas = reglas;
    }

    public ReglaPrecio obtenerRegla(String sku) {
        return reglas.stream()
                .filter(regla -> regla.esAplicable(sku))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No hay una regla de precio aplicable para el SKU: " + sku));
    }
}
