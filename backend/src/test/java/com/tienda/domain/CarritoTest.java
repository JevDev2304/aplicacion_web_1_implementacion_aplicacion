package com.tienda.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tienda.domain.reglas.ManejadorReglas;
import com.tienda.domain.reglas.ReglaPrecioEspecial;
import com.tienda.domain.reglas.ReglaPrecioNormal;
import com.tienda.domain.reglas.ReglaPrecioPorPeso;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CarritoTest {

    private final ManejadorReglas manejadorReglas = new ManejadorReglas(
            List.of(new ReglaPrecioNormal(), new ReglaPrecioPorPeso(), new ReglaPrecioEspecial()));

    @Test
    void agregarItemRechazaCuandoNoHayStockSuficiente() {
        Producto producto = new Producto("EA001", "Cuaderno", "desc", 2, 8000);
        Carrito carrito = new Carrito(new Usuario(UUID.randomUUID()));

        assertThatThrownBy(() -> carrito.agregarItem(producto, 5))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void agregarElMismoProductoDosVecesAcumulaLaCantidadEnUnSoloItem() {
        Producto producto = new Producto("EA001", "Cuaderno", "desc", 10, 8000);
        Carrito carrito = new Carrito(new Usuario(UUID.randomUUID()));

        carrito.agregarItem(producto, 2);
        carrito.agregarItem(producto, 3);

        assertThat(carrito.getItems()).hasSize(1);
        assertThat(carrito.getItems().get(0).getCantidad()).isEqualTo(5);
    }

    @Test
    void calcularTotalSumaElTotalDeCadaItemSegunSuRegla() {
        Producto normal = new Producto("EA001", "Cuaderno", "desc", 10, 8000);
        Producto especial = new Producto("SP001", "Audifonos", "desc", 10, 100_000);
        Carrito carrito = new Carrito(new Usuario(UUID.randomUUID()));

        carrito.agregarItem(normal, 2); // 16000
        carrito.agregarItem(especial, 6); // 40% desc -> 360000

        assertThat(carrito.calcularTotal(manejadorReglas)).isEqualTo(16000 + 360_000);
    }

    @Test
    void borrarItemLoQuitaDelCarrito() {
        Producto producto = new Producto("EA001", "Cuaderno", "desc", 10, 8000);
        Carrito carrito = new Carrito(new Usuario(UUID.randomUUID()));
        Item item = carrito.agregarItem(producto, 2);

        carrito.borrarItem(item);

        assertThat(carrito.getItems()).isEmpty();
    }
}
