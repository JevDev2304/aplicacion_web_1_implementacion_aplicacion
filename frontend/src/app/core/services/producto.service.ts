import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Producto } from '../models/producto.model';

export interface ProductoInput {
  sku: string;
  nombre: string;
  descripcion: string;
  unidadesDisponibles: number;
  precioUnitario: number;
  imagenUrl: string;
}

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private readonly baseUrl = `${environment.apiUrl}/productos`;

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.baseUrl);
  }

  crear(producto: ProductoInput): Observable<Producto> {
    return this.http.post<Producto>(this.baseUrl, producto);
  }

  actualizar(sku: string, producto: ProductoInput): Observable<Producto> {
    return this.http.put<Producto>(`${this.baseUrl}/${sku}`, producto);
  }

  eliminar(sku: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${sku}`);
  }
}
