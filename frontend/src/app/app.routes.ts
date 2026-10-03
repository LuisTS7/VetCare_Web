import { Routes } from '@angular/router';

import { Login } from './components/login/login';
import { Layout } from './components/layout/layout';
import { Dashboard } from './components/dashboard/dashboard';

import { Propietarios } from './components/propietarios/propietarios';
import { PropietarioForm } from './components/propietario-form/propietario-form';

import { Mascotas } from './components/mascotas/mascotas';
import { MascotaForm } from './components/mascota-form/mascota-form';

import { Citas } from './components/citas/citas';
import { CitaForm } from './components/cita-form/cita-form';

import { Atenciones } from './components/atenciones/atenciones';
import { HistorialMascota } from './components/historial-mascota/historial-mascota';

import { Reportes } from './components/reportes/reportes';

import { Administracion } from './components/administracion/administracion';
import { UsuarioForm } from './components/usuario-form/usuario-form';

import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';


export const routes: Routes = [

  {
    path: 'login',
    component: Login
  },

  {
    path: '',
    component: Layout,
    canActivate: [authGuard],

    children: [

      {
        path: 'dashboard',
        component: Dashboard
      },

      {
        path: 'propietarios',
        component: Propietarios,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador',
            'Recepcionista'
          ]
        }
      },

      {
        path: 'propietarios/nuevo',
        component: PropietarioForm,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador',
            'Recepcionista'
          ]
        }
      },

      {
        path: 'propietarios/editar/:id',
        component: PropietarioForm,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador',
            'Recepcionista'
          ]
        }
      },

      {
        path: 'mascotas',
        component: Mascotas,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador',
            'Recepcionista'
          ]
        }
      },

      {
        path: 'mascotas/nueva',
        component: MascotaForm,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador',
            'Recepcionista'
          ]
        }
      },

      {
        path: 'mascotas/editar/:id',
        component: MascotaForm,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador',
            'Recepcionista'
          ]
        }
      },

      {
        path: 'citas',
        component: Citas,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador',
            'Recepcionista'
          ]
        }
      },

      {
        path: 'citas/nueva',
        component: CitaForm,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador',
            'Recepcionista'
          ]
        }
      },

      {
        path: 'citas/reprogramar/:id',
        component: CitaForm,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador',
            'Recepcionista'
          ]
        }
      },

      {
        path: 'atenciones',
        component: Atenciones,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador',
            'Médico veterinario'
          ]
        }
      },

      {
        path: 'historial',
        component: HistorialMascota,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador',
            'Médico veterinario'
          ]
        }
      },

      {
        path: 'historial/:mascota',
        component: HistorialMascota,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador',
            'Médico veterinario'
          ]
        }
      },

      {
        path: 'reportes',
        component: Reportes,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador'
          ]
        }
      },

      {
        path: 'administracion',
        component: Administracion,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador'
          ]
        }
      },

      {
        path: 'administracion/usuarios/nuevo',
        component: UsuarioForm,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador'
          ]
        }
      },

      {
        path: 'administracion/usuarios/editar/:id',
        component: UsuarioForm,
        canActivate: [roleGuard],
        data: {
          roles: [
            'Administrador'
          ]
        }
      },

      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }

    ]
  },

  {
    path: '**',
    redirectTo: ''
  }

];