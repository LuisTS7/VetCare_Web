import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, tap } from 'rxjs';
export interface Cita{id:number;fecha:string;hora:string;mascota:string;mascotaId:number;propietario:string;motivo:string;veterinario:string;veterinarioId:number;estado:string;}
@Injectable({providedIn:'root'})
export class CitasService{
 private api='http://localhost:8080/api/citas'; private datos=signal<Cita[]>([]);
 constructor(private http:HttpClient){}
 private adaptar(x:any):Cita{return {id:x.id,fecha:x.fecha,hora:(x.hora??'').substring(0,5),mascota:x.mascota?.nombre??x.mascotaNombre??x.mascota??'',mascotaId:x.mascota?.id??x.mascotaId??0,propietario:x.mascota?.propietario?.nombres??x.propietario??x.propietarioNombre??'',motivo:x.motivo,veterinario:x.veterinario?.nombres??x.veterinarioNombre??x.veterinario??'',veterinarioId:x.veterinario?.id??x.veterinarioId??0,estado:x.estado};}
 cargar():Observable<Cita[]>{return this.http.get<any[]>(this.api).pipe(map(a=>a.map(x=>this.adaptar(x))),tap(x=>this.datos.set(x)));}
 obtenerTodas():Cita[]{return this.datos();}
 obtenerPorId(id:number):Observable<Cita>{return this.http.get<any>(`${this.api}/${id}`).pipe(map(x=>this.adaptar(x)));}
 registrar(x:Cita):Observable<any>{return this.http.post(this.api,{fecha:x.fecha,hora:this.horaApi(x.hora),mascotaId:x.mascotaId,motivo:x.motivo,veterinarioId:x.veterinarioId,estado:x.estado});}
 actualizar(x:Cita):Observable<any>{return this.http.put(`${this.api}/${x.id}`,{fecha:x.fecha,hora:this.horaApi(x.hora),mascotaId:x.mascotaId,motivo:x.motivo,veterinarioId:x.veterinarioId,estado:x.estado});}
 cancelar(x:Cita):Observable<any>{return this.http.put(`${this.api}/${x.id}`,{fecha:x.fecha,hora:this.horaApi(x.hora),mascotaId:x.mascotaId,motivo:x.motivo,veterinarioId:x.veterinarioId,estado:'Cancelada'});}
 private horaApi(h:string){return h.length===5?`${h}:00`:h;}
}
