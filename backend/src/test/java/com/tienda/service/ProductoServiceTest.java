package com.tienda.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tienda.domain.Producto;
import com.tienda.repository.ProductoRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        productoService = new ProductoService(productoRepository);
    }

    @Test
    void crearFallaSiElSkuYaExiste() {
        when(productoRepository.existsById("EA001")).thenReturn(true);

        assertThatThrownBy(() -> productoService.crear("EA001", "n", "d", 1, 1, null))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void crearGuardaElProductoCuandoElSkuEsNuevo() {
        when(productoRepository.existsById("EA001")).thenReturn(false);
        when(productoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Producto creado = productoService.crear("EA001", "Cuaderno", "desc", 10, 8000, "https://example.com/img.png");

        assertThat(creado.getSku()).isEqualTo("EA001");
        assertThat(creado.getUnidadesDisponibles()).isEqualTo(10);
    }

    @Test
    void actualizarModificaLosCamposDelProductoExistente() {
        Producto existente = new Producto("EA001", "Viejo", "desc vieja", 5, 1000);
        when(productoRepository.findById("EA001")).thenReturn(Optional.of(existente));
        when(productoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Producto actualizado = productoService.actualizar("EA001", "Nuevo", "desc nueva", 20, 2000, "https://example.com/nuevo.png");

        assertThat(actualizado.getNombre()).isEqualTo("Nuevo");
        assertThat(actualizado.getUnidadesDisponibles()).isEqualTo(20);
        assertThat(actualizado.getPrecioUnitario()).isEqualTo(2000);
    }

    @Test
    void eliminarFallaSiElProductoNoExiste() {
        when(productoRepository.existsById("XX999")).thenReturn(false);

        assertThatThrownBy(() -> productoService.eliminar("XX999"))
                .isInstanceOf(java.util.NoSuchElementException.class);
    }
}
