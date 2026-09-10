# Aplicación de comercio

Implementación del caso de estudio de la tienda (backend Spring Boot + frontend Angular), a partir del diseño orientado a objetos planteado en el OVA de principios de diseño de software.

Ver [docs/SPEC.md](docs/SPEC.md) para los requisitos, [docs/ARQUITECTURA.md](docs/ARQUITECTURA.md) para el diseño y [docs/DECISIONES.md](docs/DECISIONES.md) para las decisiones de implementación (guion del video).

## Requisitos

- Java 21+, Maven
- Node.js 20+, Angular CLI (`npm i -g @angular/cli`)
- Acceso a una base de datos PostgreSQL

## Backend

```bash
cd backend
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
# editar application-local.properties con los datos reales de tu Postgres
mvn spring-boot:run
```

Queda escuchando en `http://localhost:8080`. Al arrancar por primera vez, siembra automáticamente productos de ejemplo (uno de cada tipo: `EA`, `WE`, `SP`).

Tests:

```bash
mvn test
```

## Frontend

```bash
cd frontend
npm install
npm start
```

Queda en `http://localhost:4200`, apuntando al backend en `http://localhost:8080` (ver `src/environments/environment.ts`).

Tests:

```bash
npm test -- --watch=false --browsers=ChromeHeadless
```

## Flujo de uso

La aplicación tiene dos apartados, con navegación en la barra superior:

**Tienda** (`/tienda`, de cara al cliente):

1. Abrir `http://localhost:4200` (redirige a `/tienda/catalogo`).
2. Agregar productos al carrito indicando cantidad (kilogramos para los productos `WE*`, unidades para el resto).
3. Ir a `/tienda/carrito` para ver los totales, eliminar ítems o confirmar la compra.
4. Al confirmar, se descuenta el inventario y se acumula el total de ventas de la tienda.

**Panel del negocio** (`/negocio/productos`, de cara al negocio):

5. Crear, editar o eliminar productos del catálogo (incluida la foto).
6. Ver el total de ventas acumulado de la tienda.
