package com.tienda.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private double total;

    private Instant creadoEn = Instant.now();

    protected Venta() {
    }

    public Venta(Usuario usuario, double total) {
        this.usuario = usuario;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public double getTotal() {
        return total;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }
}
