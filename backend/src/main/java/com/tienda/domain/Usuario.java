package com.tienda.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    private UUID id;

    protected Usuario() {
    }

    public Usuario(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
