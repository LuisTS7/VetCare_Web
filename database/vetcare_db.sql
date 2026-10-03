/* ============================================================
   VETCARE WEB
   Script de creación de base de datos para SQL Server

   Proyecto:
   VetCare Web - Gestión Veterinaria

   Base de datos:
   vetcare_db

   IMPORTANTE:
   - Incluye credenciales de demostración local, no contraseñas privadas.
   - Los usuarios incluidos son únicamente usuarios de prueba
     de la aplicación VetCare.
   - Contraseña inicial de los usuarios de prueba: 123456
   ============================================================ */

USE master;
GO

/* ============================================================
   1. CREACIÓN DE BASE DE DATOS
   ============================================================ */

IF DB_ID('vetcare_db') IS NULL
BEGIN
    CREATE DATABASE vetcare_db;
END
GO

/* Acceso de demostración local. Ejecutar como administrador.
   SQL Server debe permitir autenticación mixta y TCP/IP en puerto 1433. */
IF NOT EXISTS (SELECT 1 FROM sys.server_principals WHERE name = N'vetcare_demo')
BEGIN
    CREATE LOGIN vetcare_demo
        WITH PASSWORD = 'VetCare_Demo2026!', CHECK_POLICY = ON;
END
GO

USE vetcare_db;
GO

IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = N'vetcare_demo')
BEGIN
    CREATE USER vetcare_demo FOR LOGIN vetcare_demo;
END
GO

IF IS_ROLEMEMBER(N'db_datareader', N'vetcare_demo') <> 1
    ALTER ROLE db_datareader ADD MEMBER vetcare_demo;
IF IS_ROLEMEMBER(N'db_datawriter', N'vetcare_demo') <> 1
    ALTER ROLE db_datawriter ADD MEMBER vetcare_demo;
IF IS_ROLEMEMBER(N'db_ddladmin', N'vetcare_demo') <> 1
    ALTER ROLE db_ddladmin ADD MEMBER vetcare_demo;
GO


/* ============================================================
   2. TABLA USUARIOS
   ============================================================ */

IF OBJECT_ID('dbo.usuarios', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.usuarios
    (
        id BIGINT IDENTITY(1,1) NOT NULL,
        nombres NVARCHAR(120) NOT NULL,
        correo NVARCHAR(120) NOT NULL,
        password NVARCHAR(100) NOT NULL,
        rol NVARCHAR(40) NOT NULL,
        estado NVARCHAR(20) NOT NULL,

        CONSTRAINT pk_usuarios
            PRIMARY KEY (id),

        CONSTRAINT uk_usuario_correo
            UNIQUE (correo)
    );
END
GO


/* ============================================================
   3. TABLA PROPIETARIOS
   ============================================================ */

IF OBJECT_ID('dbo.propietarios', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.propietarios
    (
        id BIGINT IDENTITY(1,1) NOT NULL,
        dni NVARCHAR(12) NOT NULL,
        nombres NVARCHAR(120) NOT NULL,
        telefono NVARCHAR(20) NOT NULL,
        correo NVARCHAR(120) NULL,

        CONSTRAINT pk_propietarios
            PRIMARY KEY (id),

        CONSTRAINT uk_propietario_dni
            UNIQUE (dni)
    );
END
GO


/* ============================================================
   4. TABLA MASCOTAS
   ============================================================ */

IF OBJECT_ID('dbo.mascotas', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.mascotas
    (
        id BIGINT IDENTITY(1,1) NOT NULL,
        nombre NVARCHAR(80) NOT NULL,
        especie NVARCHAR(50) NOT NULL,
        raza NVARCHAR(80) NULL,
        sexo NVARCHAR(20) NOT NULL,
        edad INT NOT NULL,
        propietario_id BIGINT NOT NULL,

        CONSTRAINT pk_mascotas
            PRIMARY KEY (id),

        CONSTRAINT fk_mascota_propietario
            FOREIGN KEY (propietario_id)
            REFERENCES dbo.propietarios(id),

        CONSTRAINT ck_mascota_edad
            CHECK (edad >= 0)
    );
END
GO


/* ============================================================
   5. TABLA CITAS
   ============================================================ */

IF OBJECT_ID('dbo.citas', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.citas
    (
        id BIGINT IDENTITY(1,1) NOT NULL,
        fecha DATE NOT NULL,
        hora TIME NOT NULL,
        mascota_id BIGINT NOT NULL,
        motivo NVARCHAR(250) NOT NULL,
        veterinario_id BIGINT NOT NULL,
        estado NVARCHAR(20) NOT NULL,

        CONSTRAINT pk_citas
            PRIMARY KEY (id),

        CONSTRAINT fk_cita_mascota
            FOREIGN KEY (mascota_id)
            REFERENCES dbo.mascotas(id),

        CONSTRAINT fk_cita_veterinario
            FOREIGN KEY (veterinario_id)
            REFERENCES dbo.usuarios(id)
    );
END
GO


/* ============================================================
   6. TABLA ATENCIONES
   ============================================================ */

IF OBJECT_ID('dbo.atenciones', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.atenciones
    (
        id BIGINT IDENTITY(1,1) NOT NULL,
        fecha DATE NOT NULL,
        hora TIME NOT NULL,
        mascota_id BIGINT NOT NULL,
        veterinario_id BIGINT NOT NULL,
        motivo NVARCHAR(250) NOT NULL,
        observaciones NVARCHAR(2000) NULL,
        indicaciones NVARCHAR(2000) NULL,

        CONSTRAINT pk_atenciones
            PRIMARY KEY (id),

        CONSTRAINT fk_atencion_mascota
            FOREIGN KEY (mascota_id)
            REFERENCES dbo.mascotas(id),

        CONSTRAINT fk_atencion_veterinario
            FOREIGN KEY (veterinario_id)
            REFERENCES dbo.usuarios(id)
    );
END
GO


/* ============================================================
   7. ÍNDICES
   ============================================================ */

IF NOT EXISTS
(
    SELECT 1
    FROM sys.indexes
    WHERE name = 'idx_cita_fecha'
      AND object_id = OBJECT_ID('dbo.citas')
)
BEGIN
    CREATE INDEX idx_cita_fecha
        ON dbo.citas(fecha);
END
GO


IF NOT EXISTS
(
    SELECT 1
    FROM sys.indexes
    WHERE name = 'idx_cita_veterinario'
      AND object_id = OBJECT_ID('dbo.citas')
)
BEGIN
    CREATE INDEX idx_cita_veterinario
        ON dbo.citas(veterinario_id);
END
GO


IF NOT EXISTS
(
    SELECT 1
    FROM sys.indexes
    WHERE name = 'idx_atencion_fecha'
      AND object_id = OBJECT_ID('dbo.atenciones')
)
BEGIN
    CREATE INDEX idx_atencion_fecha
        ON dbo.atenciones(fecha);
END
GO


IF NOT EXISTS
(
    SELECT 1
    FROM sys.indexes
    WHERE name = 'idx_atencion_mascota'
      AND object_id = OBJECT_ID('dbo.atenciones')
)
BEGIN
    CREATE INDEX idx_atencion_mascota
        ON dbo.atenciones(mascota_id);
END
GO


IF NOT EXISTS
(
    SELECT 1
    FROM sys.indexes
    WHERE name = 'idx_atencion_veterinario'
      AND object_id = OBJECT_ID('dbo.atenciones')
)
BEGIN
    CREATE INDEX idx_atencion_veterinario
        ON dbo.atenciones(veterinario_id);
END
GO


/* ============================================================
   8. USUARIOS INICIALES DE VETCARE

   Contraseña de prueba para los tres usuarios:
   123456

   La contraseña se almacena mediante BCrypt.
   ============================================================ */

IF NOT EXISTS
(
    SELECT 1
    FROM dbo.usuarios
    WHERE correo = 'admin@vetcare.com'
)
BEGIN
    INSERT INTO dbo.usuarios
    (
        nombres,
        correo,
        password,
        rol,
        estado
    )
    VALUES
    (
        N'Administrador VetCare',
        N'admin@vetcare.com',
        N'$2a$10$wk0EK.MIYAm0KT9uOWi/L.xVwd.uuQ.nrGggFigyv8Jo7uVN9PF66',
        N'Administrador',
        N'Activo'
    );
END
GO


IF NOT EXISTS
(
    SELECT 1
    FROM dbo.usuarios
    WHERE correo = 'recepcion@vetcare.com'
)
BEGIN
    INSERT INTO dbo.usuarios
    (
        nombres,
        correo,
        password,
        rol,
        estado
    )
    VALUES
    (
        N'María López García',
        N'recepcion@vetcare.com',
        N'$2a$10$wk0EK.MIYAm0KT9uOWi/L.xVwd.uuQ.nrGggFigyv8Jo7uVN9PF66',
        N'Recepcionista',
        N'Activo'
    );
END
GO


IF NOT EXISTS
(
    SELECT 1
    FROM dbo.usuarios
    WHERE correo = 'ana.torres@vetcare.com'
)
BEGIN
    INSERT INTO dbo.usuarios
    (
        nombres,
        correo,
        password,
        rol,
        estado
    )
    VALUES
    (
        N'Ana Torres López',
        N'ana.torres@vetcare.com',
        N'$2a$10$wk0EK.MIYAm0KT9uOWi/L.xVwd.uuQ.nrGggFigyv8Jo7uVN9PF66',
        N'Médico veterinario',
        N'Activo'
    );
END
GO


/* ============================================================
   9. DATOS DE DEMOSTRACIÓN
   ============================================================ */

IF NOT EXISTS
(
    SELECT 1
    FROM dbo.propietarios
    WHERE dni = '12345678'
)
BEGIN
    INSERT INTO dbo.propietarios
    (
        dni,
        nombres,
        telefono,
        correo
    )
    VALUES
    (
        N'12345678',
        N'Carlos Mendoza Ruiz',
        N'987654321',
        N'carlos.mendoza@gmail.com'
    );
END
GO


/* Mascota de demostración */

DECLARE @PropietarioId BIGINT;

SELECT @PropietarioId = id
FROM dbo.propietarios
WHERE dni = '12345678';

IF @PropietarioId IS NOT NULL
   AND NOT EXISTS
   (
       SELECT 1
       FROM dbo.mascotas
       WHERE nombre = N'Max'
         AND propietario_id = @PropietarioId
   )
BEGIN
    INSERT INTO dbo.mascotas
    (
        nombre,
        especie,
        raza,
        sexo,
        edad,
        propietario_id
    )
    VALUES
    (
        N'Max',
        N'Canino',
        N'Labrador',
        N'Macho',
        4,
        @PropietarioId
    );
END
GO


/* ============================================================
   10. VERIFICACIÓN
   ============================================================ */

PRINT '===============================================';
PRINT 'VetCare Web';
PRINT 'Base de datos configurada correctamente.';
PRINT '===============================================';
PRINT 'Usuarios de prueba de la aplicación:';
PRINT 'Administrador: admin@vetcare.com';
PRINT 'Recepcionista: recepcion@vetcare.com';
PRINT 'Veterinario: ana.torres@vetcare.com';
PRINT 'Contraseña inicial: 123456';
PRINT '===============================================';
GO

SELECT
    id,
    nombres,
    correo,
    rol,
    estado
FROM dbo.usuarios
ORDER BY id;
GO