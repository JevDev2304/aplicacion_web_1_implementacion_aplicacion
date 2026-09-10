# Decisiones de implementación (guion para el video)

Notas de por qué se tomó cada decisión, para explicar en el video y para sustentar las conclusiones finales sobre la utilidad del diseño OO del OVA.

## 1. Stack elegido y por qué

- **Spring Boot** se eligió explícitamente para poner a prueba el patrón Strategy del diagrama (`ReglaPrecio` + `ManejadorReglas`) contra la inyección de dependencias de un framework real, no como una implementación "a mano". Es el punto más fuerte a favor del diseño OO: **el open/closed que pide el enunciado ("agregar reglas sin tocar código existente") se obtiene gratis** con `List<ReglaPrecio>` inyectado por Spring — cada regla nueva es solo una clase `@Component` más.
- **Angular** para el frontend, consumiendo la API REST del backend — el frontend no tiene lógica de negocio, solo presenta el catálogo y el carrito.
- **PostgreSQL real** (no en memoria) para que el carrito sobreviva a refrescos de página y reinicios del backend, igual que una tienda real necesitaría.

## 2. Ajustes del diagrama al implementarlo con el framework

- **`ManejadorReglas` recibe el colaborador por parámetro, no por inyección directa en la entidad.** `Item.calcularTotal(ManejadorReglas)` y `Carrito.calcularTotal(ManejadorReglas)` reciben el manejador como argumento en vez de tenerlo como campo `@Autowired`. Mezclar beans de Spring con entidades gestionadas por Hibernate es frágil (Spring no inyecta en objetos creados por el proveedor de persistencia sin trucos adicionales). El diagrama pone el comportamiento en `Item`/`Carrito`; eso se conservó — solo cambió *cómo* llega el colaborador.
- **`Tienda` no se convirtió en una entidad persistida.** No existe "una fila tienda" en un modelo relacional; se implementó como `TiendaService`, un servicio de orquestación. `total_ventas` se volvió un **valor calculado** (`SUM(Venta.total)`) en lugar de un contador mutable — evita que un fallo a mitad de una operación deje el contador desincronizado del historial real de ventas.
- **Sin autenticación real.** El enunciado no la pide, y agregarla habría sido inventar funcionalidad no solicitada (el enunciado es explícito en no asumir eso). Se generó un `usuarioId` (UUID) en el navegador, guardado en `localStorage`; el backend crea el `Usuario` la primera vez que lo ve. Es lo mínimo necesario para que "cada usuario tenga su carrito".
- **Unidad de inventario para productos de peso.** El enunciado no aclara en qué unidad se controla el stock de un producto de peso (`WE*`). Se asumió que `unidadesDisponibles` está en la misma unidad que la `cantidad` que ingresa el usuario al carrito (kilogramos para `WE*`, piezas para `EA*`/`SP*`) — es la interpretación más simple que no inventa una conversión adicional no pedida.

## 3. Un bug real que surgió al integrar framework + diseño OO

Al probar el flujo completo se encontró un `LazyInitializationException` al consultar un carrito ya existente (`GET /carrito`): con `spring.jpa.open-in-view=false` (buena práctica para no ocultar cargas perezosas en la capa web), la colección `items` de `Carrito` no quedaba inicializada fuera de la transacción del service, y el mapeo a DTO en el controller fallaba. Se corrigió con un `JOIN FETCH` en el repositorio (`CarritoRepository`) en vez de reactivar `open-in-view`. Vale la pena mencionarlo en el video: es un ejemplo concreto de fricción entre el modelo de objetos "puro" del diagrama (donde `carrito.getItems()` simplemente funciona) y las reglas de una capa de persistencia real (sesiones, transacciones, carga perezosa) que el diagrama no modela.

## 4. Panel de negocio, fotos de producto e identidad visual

- **Separar `/tienda` de `/negocio`.** El diagrama y el enunciado se enfocan solo en el flujo de compra; no dicen nada sobre cómo se dan de alta los productos. Pero un catálogo vacío no permite demostrar nada, así que se agregó un panel de administración (crear/editar/eliminar producto, ver total de ventas). Es una extensión pragmática, no una interpretación del diseño OO del OVA — se documenta aparte para que quede claro qué vino del enunciado y qué se agregó por necesidad práctica de la demo.
- **Identidad visual propia.** Se tomó la paleta y tipografía del sitio personal del autor (jevdev2304.com — crema/oliva, Fraunces + Inter) en vez de un estilo genérico de framework, para que la app se sienta como un producto real. Ver detalle en [ARQUITECTURA.md](./ARQUITECTURA.md#sistema-de-diseño-del-frontend).
- **Fotos de producto.** Se agregó `imagenUrl` a `Producto` (nullable, retrocompatible) para que el catálogo del cliente se vea como un e-commerce real y no una tabla de datos. Es un cambio menor al modelo del diagrama (un atributo más), no una desviación de diseño.

## 5. Borrador de conclusiones sobre la utilidad del diseño OO

(Para desarrollar en el video con más detalle; puntos de partida)

- El patrón Strategy del diagrama **sí se traduce muy bien** a Spring Boot — de hecho, el framework refuerza exactamente la propiedad que el diagrama buscaba (extensibilidad sin modificar código existente), gracias a la inyección de dependencias.
- Las entidades ricas en comportamiento (`Producto`, `Carrito`, `Item`) conviven razonablemente bien con JPA/Hibernate, pero no sin fricción: hay que tener cuidado con el ciclo de vida de la sesión de persistencia (el bug de lazy loading) y evitar mezclar beans inyectados dentro de entidades.
- No todo elemento del diagrama se traduce 1:1: `Tienda` como "objeto" no tiene un lugar natural en una base de datos relacional — se vuelve un servicio. Esto sugiere que el diagrama modela bien el *comportamiento de negocio*, pero no anticipa las decisiones de persistencia que un framework real impone.
- Conclusión tentativa: el diseño OO **fue útil como guía del vocabulario y las responsabilidades** (qué sabe hacer cada clase), pero la implementación final no es una traducción mecánica del diagrama — requirió varias decisiones de adaptación al framework que el diagrama, por diseño, no cubre.
