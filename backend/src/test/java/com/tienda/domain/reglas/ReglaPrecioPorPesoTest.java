package com.tienda.domain.reglas;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReglaPrecioPorPesoTest {

    private final ReglaPrecioPorPeso regla = new ReglaPrecioPorPeso();

    @Test
    void esAplicableSoloParaSkuWE() {
        assertThat(regla.esAplicable("WE001")).isTrue();
        assertThat(regla.esAplicable("EA001")).isFalse();
        assertThat(regla.esAplicable("SP001")).isFalse();
    }

    @Test
    void convierteCantidadEnKilogramosAGramosParaElPrecioPorGramo() {
        // precioUnitario = 20 por gramo, cantidad = 1.5 kg -> 1500 gramos
        assertThat(regla.calcularTotal(1.5, 20)).isEqualTo(30000);
    }
}
