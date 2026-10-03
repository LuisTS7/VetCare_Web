import {
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';

import {
  ActivatedRoute,
  Router
} from '@angular/router';

import {
  Atencion,
  AtencionesService
} from '../../services/atenciones';

import {
  Mascota,
  MascotasService
} from '../../services/mascotas';


@Component({
  selector: 'app-historial-mascota',
  imports: [],
  templateUrl: './historial-mascota.html',
  styleUrl: './historial-mascota.css'
})
export class HistorialMascota implements OnInit {

  mascotas: Mascota[] = [];

  historial: Atencion[] = [];

  mascotaId = 0;

  nombreMascota = '';


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private mascotasService: MascotasService,
    private atencionesService: AtencionesService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {

    this.cargarMascotas();

  }


  private cargarMascotas(): void {

    this.mascotasService
      .cargar()
      .subscribe({

        next: (mascotas) => {

          this.mascotas = mascotas;

          const parametro =
            this.route.snapshot.paramMap
              .get('mascota');

          if (parametro) {

            const id =
              Number(parametro);

            if (
              Number.isFinite(id) &&
              id > 0
            ) {

              this.seleccionarMascota(id);

            }

          }

          this.cdr.detectChanges();

        },


        error: (error) => {

          console.error(
            'Error cargando mascotas:',
            error
          );

          this.mascotas = [];

          this.cdr.detectChanges();

        }

      });

  }


  cambiarMascota(
    event: Event
  ): void {

    const select =
      event.target as HTMLSelectElement;

    const id =
      Number(select.value);


    if (!id) {

      this.mascotaId = 0;

      this.nombreMascota = '';

      this.historial = [];

      this.router.navigate([
        '/historial'
      ]);

      return;

    }


    this.router.navigate(
      [
        '/historial',
        id
      ]
    ).then(() => {

      this.seleccionarMascota(id);

    });

  }


  private seleccionarMascota(
    id: number
  ): void {

    this.mascotaId = id;


    const mascota =
      this.mascotas.find(
        item =>
          item.id === id
      );


    this.nombreMascota =
      mascota?.nombre ??
      'Mascota';


    this.atencionesService
      .obtenerPorMascotaId(id)
      .subscribe({

        next: (historial) => {

          this.historial =
            historial;

          this.cdr.detectChanges();

        },


        error: (error) => {

          console.error(
            'Error cargando historial:',
            error
          );

          this.historial = [];

          this.cdr.detectChanges();

        }

      });

  }

}