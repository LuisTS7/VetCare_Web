import { Component, OnInit, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Cita, CitasService } from '../../services/citas';
import { AtencionesService } from '../../services/atenciones';

@Component({ selector:'app-dashboard', imports:[], templateUrl:'./dashboard.html', styleUrl:'./dashboard.css' })
export class Dashboard implements OnInit {
  private readonly totalPropietariosSignal = signal(0);
  private readonly totalMascotasSignal = signal(0);

  constructor(
    private http: HttpClient,
    private citasService: CitasService,
    private atencionesService: AtencionesService
  ) {}

  ngOnInit() {
    this.http.get<any>('http://localhost:8080/api/reportes/indicadores').subscribe(r => {
      this.totalPropietariosSignal.set(r.totalPropietarios ?? 0);
      this.totalMascotasSignal.set(r.totalMascotas ?? 0);
    });
    this.citasService.cargar().subscribe();
    this.atencionesService.cargar().subscribe();
  }

  get totalPropietarios() { return this.totalPropietariosSignal(); }
  get totalMascotas() { return this.totalMascotasSignal(); }

  private hoy() {
    const d = new Date();
    return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`;
  }

  get citasDeHoy(): Cita[] {
    const h = this.hoy();
    return this.citasService.obtenerTodas()
      .filter(c => c.fecha === h && c.estado !== 'Cancelada' && c.estado !== 'Atendida')
      .sort((a,b) => a.hora.localeCompare(b.hora));
  }

  get totalCitasHoy() { return this.citasDeHoy.length; }

  get totalAtencionesHoy() {
    const h = this.hoy();
    return this.atencionesService.obtenerTodas().filter(a => a.fecha === h).length;
  }
}
