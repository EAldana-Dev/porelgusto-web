@echo off
rem Ejecutar desde una ventana CMD con: call configVarEnv\config-env.cmd
rem Luego iniciar la aplicacion en esa misma ventana.
setlocal

set "DEFAULT_DB_URL=jdbc:mysql://localhost:3306/porElGusto"
set "DEFAULT_DB_USERNAME=root"
set "DEFAULT_SECRET=dev-only-change-this-token-secret-32-bytes-minimum"

set /p "DB_URL=URL de BD [%DEFAULT_DB_URL%]: "
if "%DB_URL%"=="" set "DB_URL=%DEFAULT_DB_URL%"

set /p "DB_USERNAME=Usuario de BD [%DEFAULT_DB_USERNAME%]: "
if "%DB_USERNAME%"=="" set "DB_USERNAME=%DEFAULT_DB_USERNAME%"

set /p "DB_PASSWORD=Contrasena de BD: "

set /p "AUTH_TOKEN_SECRET=Secreto auth (min 32 chars) [%DEFAULT_SECRET%]: "
if "%AUTH_TOKEN_SECRET%"=="" set "AUTH_TOKEN_SECRET=%DEFAULT_SECRET%"

set "SPRING_PROFILES_ACTIVE=prod"

endlocal & (
  set "DB_URL=%DB_URL%"
  set "DB_USERNAME=%DB_USERNAME%"
  set "DB_PASSWORD=%DB_PASSWORD%"
  set "SPRING_DATASOURCE_URL=%DB_URL%"
  set "SPRING_DATASOURCE_USERNAME=%DB_USERNAME%"
  set "SPRING_DATASOURCE_PASSWORD=%DB_PASSWORD%"
  set "AUTH_TOKEN_SECRET=%AUTH_TOKEN_SECRET%"
  set "SPRING_PROFILES_ACTIVE=prod"
)

echo.
echo Variables configuradas en esta sesion de CMD.
echo Ejecuta mvnw.cmd spring-boot:run en esta misma ventana.
