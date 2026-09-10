import { Routes } from '@angular/router';
import { CatalogoComponent } from './features/catalogo/catalogo.component';
import { CarritoComponent } from './features/carrito/carrito.component';
import { ProductosAdminComponent } from './features/negocio/productos/productos.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'tienda/catalogo' },
  { path: 'tienda', pathMatch: 'full', redirectTo: 'tienda/catalogo' },
  { path: 'tienda/catalogo', component: CatalogoComponent },
  { path: 'tienda/carrito', component: CarritoComponent },
  { path: 'negocio', pathMatch: 'full', redirectTo: 'negocio/productos' },
  { path: 'negocio/productos', component: ProductosAdminComponent },
];
