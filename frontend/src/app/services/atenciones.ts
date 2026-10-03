import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, tap } from 'rxjs';
export interface Atencion{id:number;fecha:string;hora:string;mascota:string;mascotaId:number;propietario:string;veterinario:string;veterinarioId:number;motivo:string;observaciones:string;indicaciones:string;}
@Injectable({providedIn:'root'})
export class AtencionesService{
 private api='http://localhost:8080/api/atenciones'; private datos=signal<Atencion[]>([]);
 constructor(private http:HttpClient){}
 private adaptar(x:any):Atencion{return {id:x.id,fecha:x.fecha,hora:(x.hora??'').substring(0,5),mascota:x.mascota?.nombre??x.mascotaNombre??x.mascota??'',mascotaId:x.mascota?.id??x.mascotaId??0,propietario:x.mascota?.propietario?.nombres??x.propietario??x.propietarioNombre??'',veterinario:x.veterinario?.nombres??x.veterinarioNombre??x.veterinario??'',veterinarioId:x.veterinario?.id??x.veterinarioId??0,motivo:x.motivo,observaciones:x.observaciones,indicaciones:x.indicaciones};}
 cargar():Observable<Atencion[]>{return this.http.get<any[]>(this.api).pipe(map(a=>a.map(x=>this.adaptar(x))),tap(x=>this.datos.set(x)));}
 obtenerTodas():Atencion[]{return this.datos();}
 obtenerPorMascotaId(id:number):Observable<Atencion[]>{return this.http.get<any[]>(`${this.api}/mascota/${id}`).pipe(map(a=>a.map(x=>this.adaptar(x))));}
 registrar(x:Atencion):Observable<any>{return this.http.post(this.api,{fecha:x.fecha,hora:x.hora.length===5?`${x.hora}:00`:x.hora,mascotaId:x.mascotaId,veterinarioId:x.veterinarioId,motivo:x.motivo,observaciones:x.observaciones,indicaciones:x.indicaciones});}
}
