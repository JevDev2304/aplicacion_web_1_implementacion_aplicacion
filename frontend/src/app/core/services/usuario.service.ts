import { Injectable } from '@angular/core';

const CLAVE_USUARIO_ID = 'tienda.usuarioId';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private readonly usuarioId: string;

  constructor() {
    let id = localStorage.getItem(CLAVE_USUARIO_ID);
    if (!id) {
      id = crypto.randomUUID();
      localStorage.setItem(CLAVE_USUARIO_ID, id);
    }
    this.usuarioId = id;
  }

  obtenerUsuarioId(): string {
    return this.usuarioId;
  }
}
