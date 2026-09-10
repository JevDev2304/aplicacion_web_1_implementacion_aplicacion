export interface Producto {
  sku: string;
  nombre: string;
  descripcion: string;
  unidadesDisponibles: number;
  precioUnitario: number;
  imagenUrl: string | null;
  activo: boolean;
}
