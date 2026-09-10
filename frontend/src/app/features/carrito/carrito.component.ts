import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CarritoService } from '../../core/services/carrito.service';
import { Carrito } from '../../core/models/carrito.model';

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './carrito.component.html',
  styleUrl: './carrito.component.scss',
})
export class CarritoComponent implements OnInit {
  carrito: Carrito | null = null;
  mensaje = '';
  error = '';
  cargando = true;

  constructor(
    private readonly carritoService: CarritoService,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.carritoService.obtenerCarrito().subscribe({
      next: (carrito) => {
        this.carrito = carrito;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo cargar el carrito.';
        this.cargando = false;
      },
    });
  }

  eliminarItem(itemId: number): void {
    this.carritoService.eliminarItem(itemId).subscribe({
      next: (carrito) => (this.carrito = carrito),
      error: () => (this.error = 'No se pudo eliminar el ítem.'),
    });
  }

  confirmarCompra(): void {
    this.mensaje = '';
    this.error = '';
    this.carritoService.checkout().subscribe({
      next: (venta) => {
        this.mensaje = `Compra confirmada. Total: ${venta.total}`;
        this.cargar();
      },
      error: (err) => {
        this.error = err?.error?.mensaje ?? 'No se pudo completar la compra.';
      },
    });
  }

  volverAlCatalogo(): void {
    this.router.navigate(['/catalogo']);
  }
}
