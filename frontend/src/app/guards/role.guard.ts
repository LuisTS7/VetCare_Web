import { inject } from '@angular/core';

import {
  CanActivateFn,
  Router
} from '@angular/router';

import {
  AuthService,
  RolUsuario
} from '../services/auth';

export const roleGuard: CanActivateFn = (
  route
) => {

  const authService =
    inject(AuthService);

  const router =
    inject(Router);

  if (!authService.estaAutenticado()) {

    return router.createUrlTree([
      '/login'
    ]);
  }

  const rolesPermitidos =
    route.data['roles'] as RolUsuario[];

  if (
    rolesPermitidos &&
    authService.tieneRol(
      ...rolesPermitidos
    )
  ) {
    return true;
  }

  return router.createUrlTree([
    '/dashboard'
  ]);
};