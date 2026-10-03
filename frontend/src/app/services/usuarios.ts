import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, firstValueFrom, tap } from 'rxjs';

export type RolUsuario =
  | 'Administrador'
  | 'Recepcionista'
  | 'Médico veterinario';

export type EstadoUsuario =
  | 'Activo'
  | 'Inactivo';

export interface Usuario {
  id: number;
  nombres: string;
  correo: string;
  rol: RolUsuario;
  estado: EstadoUsuario;
}

export interface NuevoUsuario {
  nombres: string;
  correo: string;
  password: string;
  rol: RolUsuario;
  estado: EstadoUsuario;
}

export interface ActualizarUsuario extends NuevoUsuario {
  id: number;
  password: string;
}

export interface LoginResponse extends Usuario {
  autenticado: boolean;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class UsuariosService {

  private readonly apiUrl =
    'http://localhost:8080/api/usuarios';

  private readonly authUrl =
    'http://localhost:8080/api/auth/login';

  private usuarios = signal<Usuario[]>([]);

  constructor(
    private http: HttpClient
  ) {}

  cargar(): Observable<Usuario[]> {

    return this.http
      .get<Usuario[]>(this.apiUrl)
      .pipe(
        tap(usuarios =>
          this.usuarios.set(usuarios)
        )
      );
  }

  obtenerTodos(): Usuario[] {
    return this.usuarios();
  }

  obtenerPorId(id: number): Observable<Usuario> {

    return this.http.get<Usuario>(
      `${this.apiUrl}/${id}`
    );
  }

  /*
   * Obtiene únicamente los médicos veterinarios
   * activos mediante el endpoint específico.
   *
   * Esto evita que Recepcionista necesite acceso
   * al listado completo de usuarios.
   */
  obtenerVeterinarios(): Observable<Usuario[]> {

    return this.http.get<Usuario[]>(
      `${this.apiUrl}/veterinarios`
    );
  }

  registrar(
    datos: NuevoUsuario
  ): Observable<Usuario> {

    return this.http
      .post<Usuario>(
        this.apiUrl,
        datos
      )
      .pipe(
        tap(() =>
          this.cargar().subscribe()
        )
      );
  }

  actualizar(
    datos: ActualizarUsuario
  ): Observable<Usuario> {

    const body: any = {
      nombres: datos.nombres,
      correo: datos.correo,
      rol: datos.rol,
      estado: datos.estado
    };

    /*
     * En edición la contraseña es opcional.
     * Si queda vacía, no se envía al backend.
     */
    if (datos.password) {
      body.password = datos.password;
    }

    return this.http
      .put<Usuario>(
        `${this.apiUrl}/${datos.id}`,
        body
      )
      .pipe(
        tap(() =>
          this.cargar().subscribe()
        )
      );
  }

  existeCorreo(
    correo: string,
    idExcluir?: number
  ): boolean {

    const correoNormalizado =
      correo.trim().toLowerCase();

    return this.usuarios().some(
      usuario =>
        usuario.correo.toLowerCase() ===
          correoNormalizado &&
        usuario.id !== idExcluir
    );
  }

  async validarAcceso(
    correo: string,
    password: string
  ): Promise<LoginResponse | null> {

    try {

      const respuesta =
        await firstValueFrom(
          this.http.post<any>(
            this.authUrl,
            {
              correo,
              password
            }
          )
        );

      return {
        id:
          respuesta.id ??
          respuesta.usuarioId,

        nombres:
          respuesta.nombres ??
          respuesta.nombre ??
          '',

        correo:
          respuesta.correo ??
          correo,

        rol:
          respuesta.rol,

        estado:
          respuesta.estado ??
          'Activo',

        autenticado:
          respuesta.autenticado ??
          true,

        token:
          respuesta.token ??
          ''
      };

    } catch {

      return null;
    }
  }
}