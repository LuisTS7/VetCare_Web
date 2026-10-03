import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface Propietario {
  id: number;
  dni: string;
  nombres: string;
  telefono: string;
  correo: string;
}

@Injectable({ providedIn: 'root' })
export class PropietariosService {
  private readonly api = 'http://localhost:8080/api/propietarios';
  private readonly datos = signal<Propietario[]>([]);

  constructor(private http: HttpClient) {}

  cargar(): Observable<Propietario[]> {
    return this.http.get<Propietario[]>(this.api).pipe(
      tap(propietarios => this.datos.set(propietarios))
    );
  }

  obtenerTodos(): Propietario[] {
    return this.datos();
  }

  obtenerPorId(id: number): Observable<Propietario> {
    return this.http.get<Propietario>(`${this.api}/${id}`);
  }

  registrar(propietario: Omit<Propietario, 'id'>): Observable<Propietario> {
    return this.http.post<Propietario>(this.api, propietario);
  }

  actualizar(propietario: Propietario): Observable<Propietario> {
    return this.http.put<Propietario>(`${this.api}/${propietario.id}`, {
      dni: propietario.dni,
      nombres: propietario.nombres,
      telefono: propietario.telefono,
      correo: propietario.correo
    });
  }
}
