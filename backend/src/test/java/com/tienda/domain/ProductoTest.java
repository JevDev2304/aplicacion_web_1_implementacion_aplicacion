package com.tienda.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ProductoTest {

    @Test
    void tieneUnidadesEsFalsoSiLaCantidadSuperaElStock() {
        Producto producto = new Producto("EA001", "Cuaderno", "desc", 5, 8000);

        assertThat(producto.tieneUnidades(5)).isTrue();
        assertThat(producto.tieneUnidades(6)).isFalse();
    }

    @Test
    void descontarUnidadesReduceElStockDisponible() {
        Producto producto = new Producto("EA001", "Cuaderno", "desc", 5, 8000);

        producto.descontarUnidades(3);

        assertThat(producto.getUnidadesDisponibles()).isEqualTo(2);
    }

    @Test
    void descontarUnidadesFallaSiNoHaySuficienteStock() {
        Producto producto = new Producto("EA001", "Cuaderno", "desc", 2, 8000);

        assertThatThrownBy(() -> producto.descontarUnidades(3))
                .isInstanceOf(IllegalStateException.class);
    }
}
