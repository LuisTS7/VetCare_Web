import { Component } from '@angular/core';

import {
  Router,
  RouterLink,
  RouterLinkActive,
  RouterOutlet
} from '@angular/router';

import {
  AuthService,
  UsuarioSesion
} from '../../services/auth';

@Component({
  selector: 'app-layout',
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
})
export class Layout {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  get usuario(): UsuarioSesion | null {
    return this.authService.obtenerUsuario();
  }

  get inicialUsuario(): string {

    const usuario = this.usuario;

    if (
      !usuario ||
      !usuario.nombre
    ) {
      return '';
    }

    return usuario.nombre
      .trim()
      .charAt(0)
      .toUpperCase();
  }

  get esAdministrador(): boolean {

    return this.authService.tieneRol(
      'Administrador'
    );
  }

  get esRecepcionista(): boolean {

    return this.authService.tieneRol(
      'Recepcionista'
    );
  }

  get esVeterinario(): boolean {

    return this.authService.tieneRol(
      'Médico veterinario'
    );
  }

  cerrarSesion(): void {

    this.authService.cerrarSesion();

    this.router.navigate([
      '/login'
    ]);
  }
}