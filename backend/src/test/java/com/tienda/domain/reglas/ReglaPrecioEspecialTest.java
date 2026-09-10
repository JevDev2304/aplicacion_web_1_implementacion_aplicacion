package com.tienda.domain.reglas;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReglaPrecioEspecialTest {

    private final ReglaPrecioEspecial regla = new ReglaPrecioEspecial();

    @Test
    void esAplicableSoloParaSkuSP() {
        assertThat(regla.esAplicable("SP001")).isTrue();
        assertThat(regla.esAplicable("EA001")).isFalse();
        assertThat(regla.esAplicable("WE001")).isFalse();
    }

    @Test
    void sinTramoCompletoNoAplicaDescuento() {
        assertThat(regla.calcularTotal(2, 100_000)).isEqualTo(200_000);
    }

    @Test
    void unTramoDeTresUnidadesAplicaVeintePorCiento() {
        assertThat(regla.calcularTotal(3, 100_000)).isEqualTo(3 * 100_000 * 0.8);
    }

    @Test
    void dosTramosAplicanCuarentaPorCiento() {
        assertThat(regla.calcularTotal(6, 100_000)).isEqualTo(6 * 100_000 * 0.6);
    }

    @Test
    void elDescuentoNuncaSuperaElTopeDeCincuentaPorCiento() {
        // 9 unidades = 3 tramos = 60% en teoria, pero el tope es 50%
        assertThat(regla.calcularTotal(9, 100_000)).isEqualTo(9 * 100_000 * 0.5);
        assertThat(regla.calcularTotal(30, 100_000)).isEqualTo(30 * 100_000 * 0.5);
    }
}
