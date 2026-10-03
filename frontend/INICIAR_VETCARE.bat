@echo off
setlocal
title VetCare Web
cd /d "%~dp0"
if not exist "node_modules\@angular\cli\bin\ng.js" (
    echo Instalando dependencias...
    call npm.cmd ci
    if errorlevel 1 (
        echo No se pudieron instalar las dependencias. Revisar Node.js e internet.
        pause
        exit /b 1
    )
)
echo Iniciando VetCare. El backend debe estar ejecutandose.
call npm.cmd start -- --open
if errorlevel 1 pause