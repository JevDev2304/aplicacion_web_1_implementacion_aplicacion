# Arquitectura

## Stack

- **Backend:** Java 21 + Spring Boot 4.1.1 (Maven).
- **Frontend:** Angular 19 (standalone components, sin NgModules).
- **Persistencia:** PostgreSQL (Railway), vía Spring Data JPA / Hibernate.

## Mapeo del diagrama de clases del OVA a código

El diagrama define un patrón **Strategy** para el cálculo de precio (`ReglaPrecio` + implementaciones) resuelto por un `ManejadorReglas`. Ese es el corazón del ejercicio: el resto de clases (`Producto`, `Usuario`, `Carrito`, `Item`, `Tienda`) son el modelo de dominio alrededor de ese patrón.

| Clase del diagrama | Implementación | Nota |
|---|---|---|
| `Producto` | `domain/Producto.java` (`@Entity`) | Igual al diagrama: `tieneUnidades`, `descontarUnidades`. |
| `Usuario` | `domain/Usuario.java` (`@Entity`) | Simplificado: solo `id` (UUID), ver decisión sobre autenticación. |
| `Carrito` | `domain/Carrito.java` (`@Entity`) | `agregarItem`, `borrarItem`, `calcularTotal`. |
| `Item` | `domain/Item.java` (`@Entity`) | `calcularTotal(ManejadorReglas)` — recibe el manejador como parámetro, no inyectado. |
| `ReglaPrecio` | `domain/reglas/ReglaPrecio.java` (interface) | Igual al diagrama. |
| `ReglaPrecioNormal/PorPeso/Especial` | `domain/reglas/*.java` (`@Component`) | Cada una es un bean de Spring. |
| `ManejadorReglas` | `domain/reglas/ManejadorReglas.java` (`@Component`) | **Recibe `List<ReglaPrecio>` por constructor** — Spring inyecta automáticamente todos los `@Component` que implementan la interfaz. |
| `Tienda` | `service/TiendaService.java` | No es una entidad persistida (no hay "una fila tienda"): es un servicio que orquesta checkout y el total de ventas. |

## Por qué Spring Boot: la inyección de dependencias como Open/Closed "gratis"

El requisito explícito del enunciado es que las reglas de precio se puedan **modificar o agregar sin tocar el código existente**. Con `ManejadorReglas` recibiendo `List<ReglaPrecio> reglas` en su constructor, Spring autodetecta cualquier bean `@Component` que implemente `ReglaPrecio` y lo agrega a esa lista automáticamente, en tiempo de arranque, vía classpath scanning:

```java
@Component
public class ManejadorReglas {
    private final List<ReglaPrecio> reglas;
    public ManejadorReglas(List<ReglaPrecio> reglas) { this.reglas = reglas; }
    public ReglaPrecio obtenerRegla(String sku) {
        return reglas.stream().filter(r -> r.esAplicable(sku)).findFirst()
            .orElseThrow(...);
    }
}
```

Agregar una regla nueva (por ejemplo, `SKU` que empiece por `2X1`) es **una clase nueva** anotada `@Component` que implemente `ReglaPrecio` — cero cambios en `ManejadorReglas` ni en las reglas existentes. Sin el framework, esto requeriría un registro manual (una lista armada a mano, o un `switch`/`if-else` sobre el prefijo del SKU) que sí obligaría a tocar código existente cada vez.

## Dónde el framework empuja una desviación del diagrama

- **Entidades ricas, pero sin beans inyectados dentro de ellas.** `Item.calcularTotal()` y `Carrito.calcularTotal()` reciben `ManejadorReglas` **como parámetro del método**, no como un campo inyectado por Spring. Mezclar el ciclo de vida de un bean de Spring con el de una entidad JPA gestionada por Hibernate es un anti-patrón (Spring no inyecta en objetos instanciados por Hibernate salvo con AspectJ weaving, que agrega complejidad innecesaria aquí). El comportamiento queda donde el diagrama lo puso; el colaborador simplemente entra por parámetro en vez de por inyección.
- **`Tienda` no es una entidad.** El diagrama la modela con estado (`total_ventas`) y comportamiento de orquestación. En un backend con base de datos relacional no tiene sentido una fila "la tienda"; se traduce naturalmente a un `@Service` sin estado propio, y `total_ventas` pasa a ser un valor calculado (`SUM(Venta.total)`) en vez de un contador mutable — ver [DECISIONES.md](./DECISIONES.md).

## Dos apartados en el frontend: tienda vs. negocio

El frontend está dividido en dos secciones con navegación propia, cada una pensada para una audiencia distinta:

- **`/tienda`** (cliente): `catalogo` (tarjetas de producto con foto, estilo e-commerce) y `carrito` (items, totales, checkout).
- **`/negocio`** (administración): `productos` — formulario para crear/editar productos (incluida la foto) y tabla con acciones de editar/eliminar, más el total de ventas acumulado como métrica destacada.

Esta separación no estaba en el diagrama del OVA ni en el enunciado original; se agregó porque sin un lugar para dar de alta productos la aplicación no se puede probar de punta a punta con datos reales, y porque conceptualmente son dos usuarios distintos del sistema (el comprador y el negocio que administra el catálogo).

## Estructura de carpetas

```
backend/src/main/java/com/tienda/
  domain/            entidades JPA (Producto, Usuario, Carrito, Item, Venta, EstadoCarrito)
  domain/reglas/      ReglaPrecio + implementaciones + ManejadorReglas (Strategy + DI)
  repository/         Spring Data JPA
  service/             CarritoService, TiendaService, ProductoService (casos de uso)
  web/                 controllers REST + manejador de errores
  web/dto/             DTOs (records) de request/response
  config/              CorsConfig
  seed/                DatosSeed (productos de ejemplo al arrancar, con fotos reales)

frontend/src/app/
  core/models/         interfaces TS (Producto, Carrito, ItemCarrito)
  core/services/        ProductoService, CarritoService, TiendaService, UsuarioService (HttpClient)
  features/catalogo/    (tienda) tarjetas de producto + agregar al carrito
  features/carrito/     (tienda) items, totales, eliminar, checkout
  features/negocio/productos/  (negocio) alta/edición/eliminación de productos + total de ventas
```

## API

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/productos` | Catálogo |
| POST | `/productos` | Crea un producto (panel de negocio) |
| PUT | `/productos/{sku}` | Edita un producto existente (panel de negocio) |
| DELETE | `/productos/{sku}` | Elimina un producto (falla si tiene carritos/ventas asociadas) |
| GET | `/carrito?usuarioId=` | Carrito abierto del usuario (lo crea si no existe) |
| POST | `/carrito/items` | `{usuarioId, sku, cantidad}` → agrega/incrementa ítem |
| DELETE | `/carrito/items/{itemId}` | Elimina un ítem |
| POST | `/carrito/checkout` | `{usuarioId}` → descuenta inventario, registra venta, cierra el carrito |
| GET | `/tienda/total-ventas` | Total acumulado de ventas de la tienda |

## Modelo de datos (Postgres)

- `productos(sku PK, nombre, descripcion, unidades_disponibles, precio_unitario, imagen_url)`
- `usuarios(id PK uuid)`
- `carritos(id PK, usuario_id FK, estado)`
- `carrito_items(id PK, carrito_id FK, producto_sku FK, cantidad)`
- `ventas(id PK, usuario_id FK, total, creado_en)`

## Sistema de diseño del frontend

La paleta y tipografía se tomaron del sitio personal [jevdev2304.com](https://www.jevdev2304.com/) para que la aplicación tuviera una identidad propia en vez de verse como una plantilla genérica:

- **Tipografía:** `Fraunces` (serif) para títulos, `Inter` (sans-serif) para el resto — cargadas desde Google Fonts.
- **Paleta:** fondo crema (`#f5f2e9`), texto oliva oscuro (`#22261a`), acento oliva (`#4f5d2f`), bordes tostados (`#d9d1b5`).
- **Componentes compartidos** (`styles.scss` global): `.boton` (primario/secundario/peligro), `.tarjeta`, `.tabla`, `.mensaje`, `.pagina` — reutilizados por las tres pantallas para mantener consistencia sin duplicar CSS.
- **Iconos:** `@angular/material` se instaló únicamente por `MatIconModule` (fuente Material Icons) para los íconos de las acciones (carrito, editar, eliminar, etc.) — no se usa el theming ni los componentes visuales de Material, todo el resto de la UI son estilos propios.
