package com.tienda.repository;

import com.tienda.domain.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("select coalesce(sum(v.total), 0) from Venta v")
    double totalVentas();
}
