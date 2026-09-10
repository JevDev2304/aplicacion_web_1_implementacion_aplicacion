package com.tienda.repository;

import com.tienda.domain.Carrito;
import com.tienda.domain.EstadoCarrito;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    @Query("select distinct c from Carrito c left join fetch c.items where c.usuario.id = :usuarioId and c.estado = :estado")
    Optional<Carrito> findByUsuarioIdAndEstado(@Param("usuarioId") UUID usuarioId, @Param("estado") EstadoCarrito estado);
}
