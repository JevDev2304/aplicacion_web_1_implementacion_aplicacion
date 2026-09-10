import { Routes } from '@angular/router';
import { CatalogoComponent } from './features/catalogo/catalogo.component';
import { CarritoComponent } from './features/carrito/carrito.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'catalogo' },
  { path: 'catalogo', component: CatalogoComponent },
  { path: 'carrito', component: CarritoComponent },
];
