import {
  Component,
  OnInit
} from '@angular/core';

import { FormsModule } from '@angular/forms';

import {
  Router,
  RouterLink
} from '@angular/router';

import {
  Cita,
  CitasService
} from '../../services/citas';

import {
  Atencion,
  AtencionesService
} from '../../services/atenciones';

import {
  AuthService
} from '../../services/auth';

@Component({
  selector: 'app-atenciones',
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './atenciones.html',
  styleUrl: './atenciones.css'
})
export class Atenciones implements OnInit {

  citaSeleccionadaId:
    number | null = null;

  atencion: Atencion =
    this.vacia();

  constructor(
    private citasService:
      CitasService,

    private atencionesService:
      AtencionesService,

    private auth:
      AuthService,

    private router:
      Router
  ) {}

  ngOnInit(): void {

    this.citasService
      .cargar()
      .subscribe();
  }

  get citasAgenda(): Cita[] {

    const u =
      this.auth.obtenerUsuario();

    return this.citasService
      .obtenerTodas()
      .filter(
        c =>
          c.estado !== 'Cancelada' &&
          c.estado !== 'Atendida' &&
          (
            u?.rol !==
              'Médico veterinario' ||
            c.veterinarioId === u.id
          )
      )
      .sort(
        (a, b) =>
          a.fecha.localeCompare(b.fecha) ||
          a.hora.localeCompare(b.hora)
      );
  }

  seleccionarCita(
    c: Cita
  ): void {

    this.citaSeleccionadaId =
      c.id;

    this.atencion = {
      id: 0,
      fecha: c.fecha,
      hora: c.hora,
      mascota: c.mascota,
      mascotaId: c.mascotaId,
      propietario: c.propietario,
      veterinario: c.veterinario,
      veterinarioId:
        c.veterinarioId,
      motivo: c.motivo,
      observaciones: '',
      indicaciones: ''
    };
  }

  formatearFecha(
    fecha: string
  ): string {

    if (!fecha) {
      return '';
    }

    const partes =
      fecha.split('-');

    if (partes.length !== 3) {
      return fecha;
    }

    return `${partes[2]}/${partes[1]}/${partes[0]}`;
  }

  guardar(): void {

    if (
      this.citaSeleccionadaId === null
    ) {

      alert(
        'Seleccione una cita para registrar la atención.'
      );

      return;
    }

    if (
      !this.atencion.observaciones.trim() ||
      !this.atencion.indicaciones.trim()
    ) {

      alert(
        'Ingrese las observaciones e indicaciones de la atención.'
      );

      return;
    }

    const id =
      this.atencion.mascotaId;

    const nombre =
      this.atencion.mascota;

    this.atencionesService
      .registrar(this.atencion)
      .subscribe({

        next: () => {

          alert(
            'Atención registrada correctamente.'
          );

          this.router.navigate(
            ['/historial', id],
            {
              queryParams: {
                nombre
              }
            }
          );
        },

        error: e =>

          alert(
            e.error?.mensaje ??
            'No se pudo registrar la atención.'
          )
      });
  }

  limpiar(): void {

    this.citaSeleccionadaId =
      null;

    this.atencion =
      this.vacia();
  }

  private vacia(): Atencion {

    return {
      id: 0,
      fecha: '',
      hora: '',
      mascota: '',
      mascotaId: 0,
      propietario: '',
      veterinario: '',
      veterinarioId: 0,
      motivo: '',
      observaciones: '',
      indicaciones: ''
    };
  }
}