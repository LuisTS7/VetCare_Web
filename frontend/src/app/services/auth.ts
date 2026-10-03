import { Injectable } from '@angular/core';

import {
  UsuariosService,
  type RolUsuario
} from './usuarios';

export type { RolUsuario };

export interface UsuarioSesion {
  id: number;
  nombre: string;
  correo: string;
  rol: RolUsuario;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly CLAVE_SESION = 'vetcare_usuario';

  constructor(
    private usuariosService: UsuariosService
  ) {}

  async iniciarSesion(
    correo: string,
    password: string
  ): Promise<boolean> {

    const usuario =
      await this.usuariosService.validarAcceso(
        correo,
        password
      );

    if (
      !usuario ||
      !usuario.autenticado ||
      usuario.estado !== 'Activo' ||
      !usuario.token
    ) {
      return false;
    }

    const sesion: UsuarioSesion = {
      id: usuario.id,
      nombre: usuario.nombres,
      correo: usuario.correo,
      rol: usuario.rol,
      token: usuario.token
    };

    sessionStorage.setItem(
      this.CLAVE_SESION,
      JSON.stringify(sesion)
    );

    return true;
  }

  cerrarSesion(): void {
    sessionStorage.removeItem(this.CLAVE_SESION);
  }

  obtenerUsuario(): UsuarioSesion | null {

    const datos =
      sessionStorage.getItem(this.CLAVE_SESION);

    if (!datos) {
      return null;
    }

    try {
      return JSON.parse(datos) as UsuarioSesion;

    } catch {
      this.cerrarSesion();
      return null;
    }
  }

  estaAutenticado(): boolean {
    return this.obtenerUsuario() !== null;
  }

  obtenerRol(): RolUsuario | null {
    return this.obtenerUsuario()?.rol ?? null;
  }

  obtenerToken(): string | null {
    return this.obtenerUsuario()?.token ?? null;
  }

  tieneRol(...roles: RolUsuario[]): boolean {

    const rol = this.obtenerRol();

    return rol !== null &&
           roles.includes(rol);
  }
}