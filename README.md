# VetCare Web

Proyecto académico de gestión veterinaria con Angular, Spring Boot y Microsoft SQL Server.

## Requisitos

- **SQL Server 2019 o posterior** (Developer o Express) y **SQL Server Management Studio (SSMS)**.
- **JDK 21**, disponible al ejecutar `java -version`.
- **Node.js 24.15.0 o posterior de la rama 24**, con npm incluido. Angular 22 requiere una versión compatible, indicada en `frontend/package-lock.json` y en la [documentación de Angular](https://angular.dev/installation).
- **Visual Studio Code** para abrir las carpetas y terminales (opcional).
- Internet en el primer inicio para descargar las dependencias.

SQL Server debe estar iniciado con **autenticación mixta (SQL Server y Windows)** y **TCP/IP en el puerto 1433**. Es una configuración por equipo: si ya existe, no se repite. SSMS por sí solo no instala el motor. No hace falta instalar Maven ni Angular CLI globalmente.

## Ejecución

Descargar **Code → Download ZIP** desde el repositorio de GitHub, descomprimir y abrir la carpeta `VetCare_Web-main` en Visual Studio Code.

### 1. Base de datos

En SSMS, conectarse a la instancia local con una cuenta administradora, abrir `database/vetcare_db.sql` y ejecutar **todo el archivo**.

El script crea la base, tablas, datos de prueba y el login/usuario del backend. No hay que crear accesos SQL por separado ni configurar variables de Windows. Puede volver a ejecutarse sin borrar los datos existentes.

### 2. Backend

En una terminal PowerShell desde la carpeta principal:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Esperar el mensaje `Started` de Spring Boot y dejar la terminal abierta. API: http://localhost:8080.

### 3. Frontend

Abrir **otra terminal** desde la carpeta principal:

```powershell
cd frontend
npm.cmd ci
npm.cmd start
```

`npm.cmd ci` solo se necesita al descargar el proyecto o cuando cambie `package-lock.json`. Dejar la terminal abierta y entrar a **http://localhost:4200**.

Para detener las aplicaciones: `Ctrl+C` en cada terminal.

## Usuarios para probar las interfaces

| Rol | Correo | Contraseña |
| --- | --- | --- |
| Administrador | admin@vetcare.com | 123456 |
| Recepcionista | recepcion@vetcare.com | 123456 |
| Médico veterinario | ana.torres@vetcare.com | 123456 |

Probar propietarios, mascotas y citas con Recepcionista; atenciones e historial con Médico veterinario; panel, reportes y usuarios con Administrador. Comprobar la validación intentando crear dos citas para el mismo veterinario en la misma fecha y hora.

Swagger: http://localhost:8080/swagger-ui/index.html.

## Si SQL Server no conecta

Solo si la instalación aún no cumple los requisitos:

1. En SSMS: clic derecho en el servidor → **Propiedades → Seguridad** → **Modo de autenticación de SQL Server y Windows**.
2. En **SQL Server Configuration Manager**: habilitar **TCP/IP** para la instancia. En sus propiedades → **Direcciones IP → IPAll**, dejar **Puertos TCP dinámicos** vacío y establecer **Puerto TCP** en `1433`.
3. Reiniciar el servicio de esa instancia y volver a iniciar el backend. Si hay varias instancias, solo una debe usar el puerto `1433`; ejecutar el script en esa misma instancia.

Para otro servidor o puerto, editar los valores predeterminados de `backend/src/main/resources/application.properties`. Si Java falta o tiene otra versión, seleccionar el JDK 21 y volver a abrir la terminal.

## Configuración de demostración

Los valores incluidos son servidor `localhost:1433`, base `vetcare_db`, login `vetcare_demo`, contraseña `VetCare_Demo2026!` y una clave JWT de demostración. El script SQL y el backend ya coinciden. Estas credenciales compartidas son para evaluación académica local; deben sustituirse para un despliegue público.

Las variables `VETCARE_DB_HOST`, `VETCARE_DB_PORT`, `VETCARE_DB_NAME`, `VETCARE_DB_USER`, `VETCARE_DB_PASSWORD` y `VETCARE_JWT_SECRET` son opcionales. Si ya existen variables `VETCARE_*` antiguas en el equipo, tienen prioridad: retirarlas de esa configuración o hacerlas coincidir y volver a abrir Visual Studio Code.

**Grupo 02 — Universidad Privada del Norte — Soluciones Web y Aplicaciones Distribuidas — 2026.**