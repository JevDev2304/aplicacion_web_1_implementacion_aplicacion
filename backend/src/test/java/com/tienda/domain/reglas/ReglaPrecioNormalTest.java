package com.tienda.domain.reglas;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReglaPrecioNormalTest {

    private final ReglaPrecioNormal regla = new ReglaPrecioNormal();

    @Test
    void esAplicableSoloParaSkuEA() {
        assertThat(regla.esAplicable("EA001")).isTrue();
        assertThat(regla.esAplicable("WE001")).isFalse();
        assertThat(regla.esAplicable("SP001")).isFalse();
    }

    @Test
    void calculaPrecioUnitarioPorCantidad() {
        assertThat(regla.calcularTotal(3, 8000)).isEqualTo(24000);
    }
}
