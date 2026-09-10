import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Carrito } from '../models/carrito.model';
import { UsuarioService } from './usuario.service';

@Injectable({ providedIn: 'root' })
export class CarritoService {
  private readonly baseUrl = `${environment.apiUrl}/carrito`;

  constructor(
    private readonly http: HttpClient,
    private readonly usuarioService: UsuarioService,
  ) {}

  obtenerCarrito(): Observable<Carrito> {
    const usuarioId = this.usuarioService.obtenerUsuarioId();
    return this.http.get<Carrito>(this.baseUrl, { params: { usuarioId } });
  }

  agregarItem(sku: string, cantidad: number): Observable<Carrito> {
    return this.http.post<Carrito>(`${this.baseUrl}/items`, {
      usuarioId: this.usuarioService.obtenerUsuarioId(),
      sku,
      cantidad,
    });
  }

  eliminarItem(itemId: number): Observable<Carrito> {
    return this.http.delete<Carrito>(`${this.baseUrl}/items/${itemId}`);
  }

  checkout(): Observable<{ id: number; total: number; creadoEn: string }> {
    return this.http.post<{ id: number; total: number; creadoEn: string }>(`${this.baseUrl}/checkout`, {
      usuarioId: this.usuarioService.obtenerUsuarioId(),
    });
  }
}
