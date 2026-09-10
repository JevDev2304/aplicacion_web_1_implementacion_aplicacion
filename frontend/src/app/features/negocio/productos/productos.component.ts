import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { ProductoInput, ProductoService } from '../../../core/services/producto.service';
import { TiendaService } from '../../../core/services/tienda.service';
import { Producto } from '../../../core/models/producto.model';

const FORMULARIO_VACIO: ProductoInput = {
  sku: '',
  nombre: '',
  descripcion: '',
  unidadesDisponibles: 0,
  precioUnitario: 0,
  imagenUrl: '',
};

@Component({
  selector: 'app-productos-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './productos.component.html',
  styleUrl: './productos.component.scss',
})
export class ProductosAdminComponent implements OnInit {
  productos: Producto[] = [];
  totalVentas = 0;
  cargando = true;
  mensaje = '';
  error = '';

  editandoSku: string | null = null;
  formulario: ProductoInput = { ...FORMULARIO_VACIO };

  constructor(
    private readonly productoService: ProductoService,
    private readonly tiendaService: TiendaService,
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
    this.tiendaService.totalVentas().subscribe({
      next: (total) => (this.totalVentas = total),
    });
  }

  cargarProductos(): void {
    this.cargando = true;
    this.productoService.listar().subscribe({
      next: (productos) => {
        this.productos = productos;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo cargar el catálogo.';
        this.cargando = false;
      },
    });
  }

  tipoDe(sku: string): string {
    if (sku.startsWith('EA')) return 'Normal';
    if (sku.startsWith('WE')) return 'Por peso';
    if (sku.startsWith('SP')) return 'Descuento especial';
    return 'Desconocido';
  }

  editar(producto: Producto): void {
    this.editandoSku = producto.sku;
    this.formulario = {
      sku: producto.sku,
      nombre: producto.nombre,
      descripcion: producto.descripcion,
      unidadesDisponibles: producto.unidadesDisponibles,
      precioUnitario: producto.precioUnitario,
      imagenUrl: producto.imagenUrl ?? '',
    };
    this.mensaje = '';
    this.error = '';
  }

  cancelarEdicion(): void {
    this.editandoSku = null;
    this.formulario = { ...FORMULARIO_VACIO };
  }

  guardar(): void {
    this.mensaje = '';
    this.error = '';

    const operacion = this.editandoSku
      ? this.productoService.actualizar(this.editandoSku, this.formulario)
      : this.productoService.crear(this.formulario);

    operacion.subscribe({
      next: (producto) => {
        this.mensaje = this.editandoSku
          ? `Producto ${producto.sku} actualizado.`
          : `Producto ${producto.sku} creado.`;
        this.cancelarEdicion();
        this.cargarProductos();
      },
      error: (err) => {
        this.error = err?.error?.mensaje ?? 'No se pudo guardar el producto.';
      },
    });
  }

  eliminar(producto: Producto): void {
    this.mensaje = '';
    this.error = '';
    this.productoService.eliminar(producto.sku).subscribe({
      next: () => {
        this.mensaje = `Producto ${producto.sku} eliminado.`;
        if (this.editandoSku === producto.sku) {
          this.cancelarEdicion();
        }
        this.cargarProductos();
      },
      error: (err) => {
        this.error = err?.error?.mensaje ?? 'No se pudo eliminar el producto.';
      },
    });
  }
}
