import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class TiendaService {
  private readonly baseUrl = `${environment.apiUrl}/tienda`;

  constructor(private readonly http: HttpClient) {}

  totalVentas(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/total-ventas`);
  }
}
