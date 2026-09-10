package com.tienda.web.dto;

import java.util.UUID;

public record AgregarItemRequest(UUID usuarioId, String sku, double cantidad) {
}
