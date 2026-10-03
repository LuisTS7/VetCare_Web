import { Component, OnInit, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CitasService } from '../../services/citas';

@Component({ selector:'app-reportes', imports:[], templateUrl:'./reportes.html', styleUrl:'./reportes.css' })
export class Reportes implements OnInit {
  private readonly indicadores = signal({ totalPropietarios:0, totalMascotas:0, totalCitas:0, totalAtenciones:0 });

  constructor(private http: HttpClient, private citasService: CitasService) {}

  ngOnInit() {
    this.http.get<any>('http://localhost:8080/api/reportes/indicadores').subscribe(r => {
      this.indicadores.set({
        totalPropietarios: r.totalPropietarios ?? 0,
        totalMascotas: r.totalMascotas ?? 0,
        totalCitas: r.totalCitas ?? 0,
        totalAtenciones: r.totalAtenciones ?? 0
      });
    });
    this.citasService.cargar().subscribe();
  }

  get totalPropietarios(){ return this.indicadores().totalPropietarios; }
  get totalMascotas(){ return this.indicadores().totalMascotas; }
  get totalCitas(){ return this.indicadores().totalCitas; }
  get totalAtenciones(){ return this.indicadores().totalAtenciones; }
  private contar(e:string){ return this.citasService.obtenerTodas().filter(c=>c.estado===e).length; }
  get citasConfirmadas(){return this.contar('Confirmada');}
  get citasPendientes(){return this.contar('Pendiente');}
  get citasAtendidas(){return this.contar('Atendida');}
  get citasCanceladas(){return this.contar('Cancelada');}
  get citasActivas(){return this.citasConfirmadas+this.citasPendientes;}
  porcentaje(n:number){return this.totalCitas?Math.round(n/this.totalCitas*100):0;}
  get porcentajeConfirmadas(){return this.porcentaje(this.citasConfirmadas);}
  get porcentajePendientes(){return this.porcentaje(this.citasPendientes);}
  get porcentajeAtendidas(){return this.porcentaje(this.citasAtendidas);}
  get porcentajeCanceladas(){return this.porcentaje(this.citasCanceladas);}
}
