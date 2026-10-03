import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, tap } from 'rxjs';

export interface Mascota {
  id: number;
  nombre: string;
  especie: string;
  raza: string;
  sexo: string;
  edad: number;
  propietario: string;
  propietarioId: number;
}

@Injectable({ providedIn: 'root' })
export class MascotasService {
  private readonly api = 'http://localhost:8080/api/mascotas';
  private readonly datos = signal<Mascota[]>([]);

  constructor(private http: HttpClient) {}

  private adaptar(x: any): Mascota {
    return {
      id: x.id,
      nombre: x.nombre,
      especie: x.especie,
      raza: x.raza,
      sexo: x.sexo,
      edad: x.edad,
      propietario: x.propietario?.nombres ?? x.propietarioNombre ?? x.propietario ?? '',
      propietarioId: x.propietario?.id ?? x.propietarioId ?? 0
    };
  }

  cargar(): Observable<Mascota[]> {
    return this.http.get<any[]>(this.api).pipe(
      map(lista => lista.map(x => this.adaptar(x))),
      tap(mascotas => this.datos.set(mascotas))
    );
  }

  obtenerTodos(): Mascota[] { return this.datos(); }

  obtenerPorId(id: number): Observable<Mascota> {
    return this.http.get<any>(`${this.api}/${id}`).pipe(map(x => this.adaptar(x)));
  }

  registrar(x: Omit<Mascota, 'id' | 'propietario'>): Observable<any> {
    return this.http.post(this.api, x);
  }

  actualizar(x: Mascota): Observable<any> {
    return this.http.put(`${this.api}/${x.id}`, {
      nombre: x.nombre,
      especie: x.especie,
      raza: x.raza,
      sexo: x.sexo,
      edad: x.edad,
      propietarioId: x.propietarioId
    });
  }
}
