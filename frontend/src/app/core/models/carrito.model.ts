export interface ItemCarrito {
  id: number;
  sku: string;
  nombreProducto: string;
  cantidad: number;
  total: number;
}

export interface Carrito {
  id: number;
  items: ItemCarrito[];
  total: number;
}
