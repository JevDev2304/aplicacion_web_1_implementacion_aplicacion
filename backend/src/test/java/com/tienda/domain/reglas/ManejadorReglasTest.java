package com.tienda.domain.reglas;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class ManejadorReglasTest {

    private final ManejadorReglas manejador = new ManejadorReglas(
            List.of(new ReglaPrecioNormal(), new ReglaPrecioPorPeso(), new ReglaPrecioEspecial()));

    @Test
    void obtieneLaReglaCorrectaSegunElPrefijoDelSku() {
        assertThat(manejador.obtenerRegla("EA001")).isInstanceOf(ReglaPrecioNormal.class);
        assertThat(manejador.obtenerRegla("WE001")).isInstanceOf(ReglaPrecioPorPeso.class);
        assertThat(manejador.obtenerRegla("SP001")).isInstanceOf(ReglaPrecioEspecial.class);
    }

    @Test
    void fallaSiNingunaReglaEsAplicable() {
        assertThatThrownBy(() -> manejador.obtenerRegla("XX001"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
