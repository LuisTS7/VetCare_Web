import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Cita, CitasService } from '../../services/citas';

@Component({
  selector: 'app-citas',
  imports: [FormsModule, RouterLink],
  templateUrl: './citas.html',
  styleUrl: './citas.css'
})
export class Citas implements OnInit {

  fechaFiltro = '';
  veterinarioFiltro = '';
  estadoFiltro = '';

  constructor(
    private citasService: CitasService
  ) {}

  ngOnInit(): void {
    this.recargar();
  }

  private recargar(): void {
    this.citasService.cargar().subscribe();
  }

  get veterinarios(): string[] {

    return [
      ...new Set(
        this.citasService
          .obtenerTodas()
          .map(c => c.veterinario)
          .filter(Boolean)
      )
    ];
  }

  get citas(): Cita[] {

    const fechaBackend =
      this.convertirFechaFiltro();

    return this.citasService
      .obtenerTodas()
      .filter(c =>
        (
          !this.fechaFiltro ||
          c.fecha === fechaBackend
        ) &&
        (
          !this.veterinarioFiltro ||
          c.veterinario ===
            this.veterinarioFiltro
        ) &&
        (
          !this.estadoFiltro ||
          c.estado ===
            this.estadoFiltro
        )
      )
      .sort(
        (a, b) =>
          a.fecha.localeCompare(b.fecha) ||
          a.hora.localeCompare(b.hora)
      );
  }

  formatearFechaFiltro(): void {

    let valor =
      this.fechaFiltro.replace(/\D/g, '');

    valor = valor.substring(0, 8);

    if (valor.length > 4) {

      this.fechaFiltro =
        valor.substring(0, 2) +
        '/' +
        valor.substring(2, 4) +
        '/' +
        valor.substring(4);

    } else if (valor.length > 2) {

      this.fechaFiltro =
        valor.substring(0, 2) +
        '/' +
        valor.substring(2);

    } else {

      this.fechaFiltro = valor;
    }
  }

  seleccionarFechaFiltro(
    fecha: string
  ): void {

    if (!fecha) {
      return;
    }

    const partes = fecha.split('-');

    if (partes.length !== 3) {
      return;
    }

    this.fechaFiltro =
      `${partes[2]}/${partes[1]}/${partes[0]}`;
  }

  private convertirFechaFiltro(): string {

    if (!this.fechaFiltro) {
      return '';
    }

    const partes =
      this.fechaFiltro.split('/');

    if (partes.length !== 3) {
      return '';
    }

    const dia = partes[0];
    const mes = partes[1];
    const anio = partes[2];

    if (
      dia.length !== 2 ||
      mes.length !== 2 ||
      anio.length !== 4
    ) {
      return '';
    }

    return `${anio}-${mes}-${dia}`;
  }

  formatearFecha(
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

  cancelarCita(
    id: number
  ): void {

    const c =
      this.citasService
        .obtenerTodas()
        .find(x => x.id === id);

    if (!c) {
      return;
    }

    if (c.estado === 'Atendida') {

      alert(
        'Una cita atendida no puede cancelarse.'
      );

      return;
    }

    if (
      c.estado === 'Cancelada' ||
      !confirm('¿Desea cancelar esta cita?')
    ) {
      return;
    }

    this.citasService
      .cancelar(c)
      .subscribe({

        next: () => {

          alert(
            'Cita cancelada correctamente.'
          );

          this.recargar();
        },

        error: e =>

          alert(
            e.error?.mensaje ??
            'No se pudo cancelar la cita.'
          )
      });
  }
}