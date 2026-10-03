import {
  HttpInterceptorFn
} from '@angular/common/http';

export const authInterceptor:
  HttpInterceptorFn = (request, next) => {

    /*
     * El endpoint de login es público.
     * No necesita JWT.
     */
    if (
      request.url.includes(
        '/api/auth/login'
      )
    ) {
      return next(request);
    }

    /*
     * Solo agregamos JWT a las
     * peticiones dirigidas al backend
     * de VetCare.
     */
    if (
      !request.url.startsWith(
        'http://localhost:8080/api/'
      )
    ) {
      return next(request);
    }

    const datosSesion =
      sessionStorage.getItem(
        'vetcare_usuario'
      );

    if (!datosSesion) {
      return next(request);
    }

    try {

      const usuario =
        JSON.parse(datosSesion);

      const token =
        usuario?.token;

      if (!token) {
        return next(request);
      }

      const requestAutenticado =
        request.clone({
          setHeaders: {
            Authorization:
              `Bearer ${token}`
          }
        });

      return next(
        requestAutenticado
      );

    } catch {

      return next(request);
    }
  };