package com.tienda.repository;

import com.tienda.domain.Carrito;
import com.tienda.domain.EstadoCarrito;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUsuarioIdAndEstado(UUID usuarioId, EstadoCarrito estado);
}
