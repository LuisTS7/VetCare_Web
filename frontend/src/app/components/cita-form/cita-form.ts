import {
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';

import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';

import {
  Cita,
  CitasService
} from '../../services/citas';

import {
  Mascota,
  MascotasService
} from '../../services/mascotas';

import {
  Usuario,
  UsuariosService
} from '../../services/usuarios';

@Component({
  selector: 'app-cita-form',
  imports: [FormsModule],
  templateUrl: './cita-form.html',
  styleUrl: './cita-form.css'
})
export class CitaForm implements OnInit {

  esReprogramacion = false;

  mascotas: Mascota[] = [];
  veterinarios: Usuario[] = [];

  fechaVisual = '';

  cita: Cita = {
    id: 0,
    fecha: '',
    hora: '',
    mascota: '',
    mascotaId: 0,
    propietario: '',
    motivo: '',
    veterinario: '',
    veterinarioId: 0,
    estado: 'Pendiente'
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private citasService: CitasService,
    private mascotasService: MascotasService,
    private usuariosService: UsuariosService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    this.cargarDatos();

    const id =
      this.route.snapshot.paramMap.get('id');

    if (id) {

      this.citasService
        .obtenerPorId(Number(id))
        .subscribe({
          next: (cita) => {

            this.esReprogramacion = true;
            this.cita = { ...cita };

            this.fechaVisual =
              this.convertirFechaParaMostrar(
                this.cita.fecha
              );

            this.cdr.detectChanges();
          },

          error: () => {
            this.router.navigate(['/citas']);
          }
        });
    }
  }

  private cargarDatos(): void {

    forkJoin({
      mascotas: this.mascotasService.cargar(),
      veterinarios:
        this.usuariosService.obtenerVeterinarios()
    }).subscribe({
      next: (resultado) => {

        this.mascotas = resultado.mascotas;
        this.veterinarios =
          resultado.veterinarios;

        this.cdr.detectChanges();
      },

      error: (error) => {

        console.error(
          'Error cargando datos de la cita:',
          error
        );

        alert(
          'No se pudieron cargar las mascotas o veterinarios.'
        );
      }
    });
  }

  seleccionarMascota(): void {

    const mascota =
      this.mascotas.find(
        m => m.nombre === this.cita.mascota
      );

    if (mascota) {

      this.cita.mascotaId = mascota.id;
      this.cita.propietario =
        mascota.propietario;

    } else {

      this.cita.mascotaId = 0;
      this.cita.propietario = '';
    }
  }

  formatearFechaVisual(): void {

    let valor =
      this.fechaVisual.replace(/\D/g, '');

    valor = valor.substring(0, 8);

    if (valor.length > 4) {

      this.fechaVisual =
        valor.substring(0, 2) +
        '/' +
        valor.substring(2, 4) +
        '/' +
        valor.substring(4);

    } else if (valor.length > 2) {

      this.fechaVisual =
        valor.substring(0, 2) +
        '/' +
        valor.substring(2);

    } else {

      this.fechaVisual = valor;
    }
  }

  seleccionarFechaCalendario(
    fecha: string
  ): void {

    if (!fecha) {
      return;
    }

    this.fechaVisual =
      this.convertirFechaParaMostrar(fecha);
  }

  private convertirFechaParaMostrar(
    fecha: string
  ): string {

    if (!fecha) {
      return '';
    }

    const partes = fecha.split('-');

    if (partes.length !== 3) {
      return fecha;
    }

    return `${partes[2]}/${partes[1]}/${partes[0]}`;
  }

  private convertirFechaParaBackend(
    fecha: string
  ): string | null {

    const partes = fecha.split('/');

    if (partes.length !== 3) {
      return null;
    }

    const dia = Number(partes[0]);
    const mes = Number(partes[1]);
    const anio = Number(partes[2]);

    if (
      !Number.isInteger(dia) ||
      !Number.isInteger(mes) ||
      !Number.isInteger(anio) ||
      anio < 1900 ||
      anio > 2100
    ) {
      return null;
    }

    const fechaPrueba =
      new Date(anio, mes - 1, dia);

    if (
      fechaPrueba.getFullYear() !== anio ||
      fechaPrueba.getMonth() !== mes - 1 ||
      fechaPrueba.getDate() !== dia
    ) {
      return null;
    }

    const diaTexto =
      String(dia).padStart(2, '0');

    const mesTexto =
      String(mes).padStart(2, '0');

    return `${anio}-${mesTexto}-${diaTexto}`;
  }

  guardar(): void {

    const fechaBackend =
      this.convertirFechaParaBackend(
        this.fechaVisual
      );

    if (!fechaBackend) {

      alert(
        'Ingrese una fecha válida en formato DD/MM/AAAA.'
      );

      return;
    }

    this.cita.fecha = fechaBackend;

    const veterinario =
      this.veterinarios.find(
        v =>
          v.nombres ===
          this.cita.veterinario
      );

    if (veterinario) {
      this.cita.veterinarioId =
        veterinario.id;
    }

    if (
      !this.cita.fecha ||
      !this.cita.hora ||
      !this.cita.mascotaId ||
      !this.cita.motivo.trim() ||
      !this.cita.veterinarioId
    ) {

      alert(
        'Complete todos los campos obligatorios.'
      );

      return;
    }

    const operacion =
      this.esReprogramacion
        ? this.citasService.actualizar(
            this.cita
          )
        : this.citasService.registrar(
            this.cita
          );

    operacion.subscribe({
      next: () => {

        alert(
          this.esReprogramacion
            ? 'Cita reprogramada correctamente.'
            : 'Cita programada correctamente.'
        );

        this.router.navigate(['/citas']);
      },

      error: (error) => {

        alert(
          error.error?.mensaje ??
          'No se pudo guardar la cita.'
        );
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/citas']);
  }
}