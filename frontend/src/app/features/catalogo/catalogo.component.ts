import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { CarritoService } from '../../core/services/carrito.service';
import { ProductoService } from '../../core/services/producto.service';
import { Producto } from '../../core/models/producto.model';

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './catalogo.component.html',
  styleUrl: './catalogo.component.scss',
})
export class CatalogoComponent implements OnInit {
  productos: Producto[] = [];
  cantidades: Record<string, number> = {};
  mensaje = '';
  error = '';
  cargando = true;

  constructor(
    private readonly productoService: ProductoService,
    private readonly carritoService: CarritoService,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    this.productoService.listar().subscribe({
      next: (productos) => {
        this.productos = productos;
        productos.forEach((producto) => (this.cantidades[producto.sku] = this.cantidadInicial(producto)));
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo cargar el catálogo. Verifica que el backend esté corriendo.';
        this.cargando = false;
      },
    });
  }

  cantidadInicial(producto: Producto): number {
    return producto.sku.startsWith('WE') ? 0.5 : 1;
  }

  paso(producto: Producto): number {
    return producto.sku.startsWith('WE') ? 0.1 : 1;
  }

  unidad(producto: Producto): string {
    return producto.sku.startsWith('WE') ? 'kg' : 'unidades';
  }

  agregarAlCarrito(producto: Producto): void {
    this.mensaje = '';
    this.error = '';
    const cantidad = this.cantidades[producto.sku];
    this.carritoService.agregarItem(producto.sku, cantidad).subscribe({
      next: () => {
        this.mensaje = `Agregaste ${cantidad} ${this.unidad(producto)} de ${producto.nombre} al carrito.`;
      },
      error: (err) => {
        this.error = err?.error?.mensaje ?? 'No se pudo agregar el producto al carrito.';
      },
    });
  }

  irAlCarrito(): void {
    this.router.navigate(['/tienda/carrito']);
  }
}
