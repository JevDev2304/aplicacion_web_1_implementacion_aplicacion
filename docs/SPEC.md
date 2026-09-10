# SPEC — Aplicación de comercio

## Descripción del caso de estudio

Aplicación web para una tienda que vende varios productos. Cada producto tiene:

- `sku`: código que codifica el tipo de producto.
  - `EA*` → producto **normal**.
  - `WE*` → producto **de peso**.
  - `SP*` → producto con **descuento especial**.
- `nombre`, `descripcion`.
- `unidadesDisponibles`: inventario.
- `precioUnitario`: precio base (su significado depende del tipo, ver reglas de precio).

## Reglas de cálculo de precio

| Tipo | Regla |
|---|---|
| Normal (`EA`) | `total = precioUnitario * cantidad` |
| Peso (`WE`) | `precioUnitario` está dado **por gramo**; `cantidad` se ingresa en **kilogramos** → `total = precioUnitario * 1000 * cantidad` |
| Especial (`SP`) | 20% de descuento por cada 3 unidades completas, hasta un tope de 50% de descuento |

Las reglas deben poder **modificarse o ampliarse sin cambiar el código existente** (open/closed). Ver [ARQUITECTURA.md](./ARQUITECTURA.md) para cómo se logra esto con Strategy + inyección de dependencias.

## Casos de uso

### De cara al cliente (`/tienda`)

1. **Listar catálogo** — ver todos los productos disponibles, con su foto.
2. **Agregar producto al carrito** — indicando producto y cantidad; se valida que haya inventario suficiente; se muestra el total del ítem y el total acumulado del carrito.
3. **Eliminar un ítem del carrito**.
4. **Concretar la compra (checkout)** — descuenta el inventario vendido y acumula el valor de la venta en el total de la tienda.

### De cara al negocio (`/negocio`)

El enunciado original no pedía esta parte, pero sin ella no hay forma de que existan productos que vender: alguien del negocio necesita poder darlos de alta. Se agregó un panel de administración con:

5. **Crear un producto** — sku, nombre, descripción, unidades disponibles, precio unitario y una foto (URL de imagen).
6. **Editar un producto** — mismos campos, excepto el sku (identificador inmutable).
7. **Eliminar un producto** — rechazado si el producto ya tiene carritos o ventas asociadas (integridad referencial).
8. **Ver el total de ventas acumulado** de la tienda.

## Reglas de validación e integridad

- No se puede agregar al carrito una cantidad mayor a la disponible en inventario (error 400).
- El checkout falla si el carrito está vacío, o si el usuario no tiene un carrito abierto (error 400).
- Al hacer checkout, el carrito se cierra: no puede reutilizarse para agregar más ítems.

## Fuera de alcance (explícitamente, según el enunciado)

- No hay autenticación de usuarios real — el enunciado no la pide. Ver la decisión de `usuarioId` implícito en [DECISIONES.md](./DECISIONES.md).
- No se modelan restricciones de productos reales (impuestos, envíos, medios de pago, etc.) — el enunciado indica explícitamente no asumir funcionalidad adicional.
- El panel de negocio no tiene autenticación tampoco: es una extensión pragmática para poder demostrar la aplicación de punta a punta (sin productos no hay nada que comprar), no una funcionalidad "de seguridad" pedida por el enunciado.

## Entregables de la tarea

1. Repositorio con la implementación funcional (backend + frontend) — este repo.
2. Video explicando las decisiones de implementación y las conclusiones sobre la utilidad del diseño orientado a objetos — guion en [DECISIONES.md](./DECISIONES.md).
