# Por el Gusto — Web

Proyecto del equipo **Neksbyte**. Esta es la **aplicación web** de Por el Gusto (la app móvil vive en otro repositorio). Hecha con **Java**, **Spring Boot**, **Hibernate (JPA)**, **JSP (con JSTL)** y **Bootstrap**.


## 0. Qué incluye y cómo probarlo

Tienda en línea completa sobre el modelo de datos del equipo (`sql/db.sql`: `ArregloFloral`, `Usuario`,
`CarritoDeCompra`, `Pedido`, `Cupon`, `Ticket`, `Factura`, `ListaDeDeseos`, `Contacto`, `Modificacion`...).

| Módulo | Qué se puede hacer | Dónde |
|---|---|---|
| **Catálogo** | Buscador con sugerencias (sin importar acentos), filtros (precio, flores, categorías, colecciones, temporadas, disponibilidad, ofertas) y orden; ficha con personalización y costo adicional, fecha/horario de entrega, QR del producto y tarjeta QR para imprimir en PDF; **Recomendados para ti** (temporada, novedad, popularidad, ventas y cookies) | `/`, `/catalogo`, `/producto/{sku}` |
| **Compra** | Carrito (cantidades, cupones, envío, IVA desglosado), pago con **tarjeta (Stripe)** o **PayPal**, **continuar por WhatsApp**, envío por distancia desde la florería, confirmación con folio, ticket digital, **dedicatoria con QR** y solicitud de **factura** | `/carrito`, `/checkout`, `/pedido/{folio}` |
| **Cuenta** | Registro, inicio de sesión (correo o nombre), recuperar contraseña con código, Mi cuenta, direcciones guardadas, Mis pedidos con seguimiento (línea de tiempo y ubicación del repartidor), lista de deseos con aviso de disponibilidad, notificaciones y soporte | `/registro`, `/login`, `/cuenta`, `/seguimiento`, `/soporte` |
| **Administración** | Roles y permisos (`TipoUsuario`), productos e inventario (con imagen), categorías, grupos, temporadas y opciones de personalización, promociones/ofertas/eventos y cupones (con aviso por correo), pedidos y etapas, asignación y **envío del pedido al empleado por WhatsApp** (sin precios), incidencias, soporte, generador de QR, **estadísticas de visitas** y **pagos y envíos** (a qué cuentas llega el dinero y punto de partida) | `/admin` |

**Cuentas iniciales** (se crean con `data.sql`; el login acepta correo o nombre). Las contraseñas **no están en el
repositorio**: se entregan aparte. Cámbialas desde *Mi cuenta* al entrar por primera vez.

| Rol | Correo | Acceso |
|---|---|---|
| Administrador | `admin@porelgusto.mx` | todo el panel |
| Empleado | `empleado@porelgusto.mx` | tablero, pedidos asignados, soporte y QR |
| Repartidor | `repartidor@porelgusto.mx` | tablero y pedidos asignados (envía su ubicación) |
| Cliente de ejemplo | `cliente@porelgusto.mx` | tienda y Mi cuenta (tiene 2 pedidos de ejemplo) |

**Cupones de prueba:** `BIENVENIDA10` (10 % desde $500), `REGALO150` ($150 desde $1,000), `VERANO25` (vencido).
La colección **Primavera** tiene 20 % de descuento automático.

### Pagos

Las llaves se capturan en el panel: **Pagos y envíos** (`/admin/configuracion`). Ahí se decide a qué cuenta de
**Stripe** (tarjeta) y de **PayPal** llega el dinero; se guardan cifradas con `AUTH_TOKEN_SECRET` (si esa variable
cambia, hay que volver a capturarlas) y el botón **Probar conexión** revisa que funcionen.

| Estado | Qué pasa |
|---|---|
| Sin llaves | Esa forma de pago **no se ofrece** a los clientes (si no hay ninguna, se invita a comprar por WhatsApp). Solo con el perfil `h2` se simula: tarjeta `4242 4242 4242 4242` aprobada, `4000 0000 0000 0002` rechazada, `4000 0000 0000 9995` sin fondos |
| `sk_test_…` / PayPal Sandbox | Pasarela real en modo de prueba (no se cobra dinero) |
| `sk_live_…` / PayPal Live | **Cobros reales**. La cuenta de Stripe o PayPal debe estar verificada como negocio |

- Stripe no acepta cobros menores a **$10 MXN**. Para probar un pago real hay un **producto de prueba** de $10, oculto del
  catálogo y sin envío: `/producto/PG-000` (en el panel se marca con «Producto de prueba»).
- Las variables `STRIPE_SECRET_KEY`, `PAYPAL_CLIENT_ID` y `PAYPAL_CLIENT_SECRET` siguen funcionando si en el panel no hay llaves.

### Envío

Se calcula en **línea recta** desde el punto de partida (por defecto San Bernabé, Azcapotzalco; se cambia en el panel):

| Distancia (dentro de la CDMX) | Compra menor a $1,500 | Compra desde $1,500 |
|---|---|---|
| Hasta 10 km | $100 | Gratis |
| Hasta 15 km | $150 | Gratis |
| Hasta 20 km | $200 | Gratis |
| Más de 20 km, o fuera de la CDMX (C.P. que no empieza en 01–16) | $200 | $200 |

Las direcciones se ubican con OpenStreetMap (Nominatim, gratis y sin llave); si una no se encuentra se cobra $200.
Tarifas y distancias: `app.envio.*` en `application.properties`.

### Estadísticas del sitio

`/admin/estadisticas`: visitantes, páginas vistas, de dónde llegan (Google, Facebook, WhatsApp, campañas `utm_source`),
país y ciudad, dispositivo y navegador, y cuántos visitantes compran. No se guardan IPs (huella anónima diaria).
País y ciudad salen de la base gratuita de DB-IP, un archivo local sin API ni cuenta: descárgala con
`scripts/descargar-geoip.sh` (queda en `geoip/`, fuera de git) y reinicia; conviene actualizarla cada mes.

### Correos

Para que los correos lleguen (pedidos, avisos y códigos para recuperar la contraseña) define el servidor SMTP antes de arrancar:

```bash
export MAIL_HOST=smtp.gmail.com MAIL_PORT=587
export MAIL_USERNAME=cuenta@gmail.com MAIL_PASSWORD='contraseña de aplicación'
```

Sin `MAIL_HOST` los correos solo se guardan. En ambos casos el administrador ve los últimos 50 en el panel: **Correos enviados**.

### API (JSON)

La documentación interactiva (Swagger / OpenAPI) está en **<http://localhost:8080/swagger-ui.html>** y la
especificación en `/v3/api-docs`. Solo la ven los **administradores** con sesión iniciada (enlace «API (Swagger)» en el panel).
Documenta únicamente las rutas `/api/**`.


Además de los CRUD de `/api/...` y `/api/auth` (app móvil):

| Método y ruta | Para qué |
|---|---|
| `GET /api/productos?q=` | sugerencias del buscador |
| `POST /api/telemetria` `{tracking, latitud, longitud}` | el repartidor (sesión del personal) envía su ubicación |
| `GET /api/rastreo/{guia}` | estado y ubicación del envío (el seguimiento se actualiza solo) |
| `POST /api/qr/{token}/entrega` `{recibio}` | confirmar la entrega escaneando el QR del pedido |
| `GET /checkout/envio?calle=&colonia=&codigoPostal=` | costo de envío y total del carrito para esa dirección |

---

## 1. Requisitos (versiones)

| Programa | Versión requerida | Comprobar con |
|---|---|---|
| **JDK (OpenJDK)** | **25 o superior** (recomendado 27) | `java -version` |
| **Maven** | **3.9 o superior** (recomendado 3.10) — opcional, el proyecto trae `./mvnw` | `mvn -v` |
| **Git** | 2.40 o superior | `git --version` |
| Navegador | cualquiera actual | — |

Versiones que usa el proyecto (no hay que instalarlas: Maven las descarga solo):

| Librería | Versión |
|---|---|
| Spring Boot | 4.1.2 |
| Hibernate ORM | 7.4.x (lo fija Spring Boot) |
| JSP / JSTL | Jasper de Tomcat 11 / JSTL 3.0 (lo fija Spring Boot) |
| Bootstrap | 5.3.8 (como WebJar, sin CDN) |
| H2 (BD de desarrollo) | 2.4.x |

> **Importante:** Spring Boot 4 / Hibernate 7 usan los paquetes **`jakarta.*`**
> (`jakarta.persistence`, `jakarta.validation`, ...). **Nunca** importes `javax.persistence`,
> `javax.validation` ni `javax.servlet`: son de versiones antiguas y no funcionan.

### IDE

Cualquier IDE con soporte de Java y Maven sirve (IntelliJ IDEA, VS Code con *Extension Pack for Java*
y *Spring Boot Tools*, Eclipse/STS o Neovim con `jdtls`). Abre siempre la **carpeta raíz del proyecto**
(la que contiene `pom.xml`) para que el IDE reconozca el proyecto. No subas archivos de tu IDE al repositorio
(ya están en `.gitignore`).

---

## 2. Cómo ejecutarlo en local (Windows, Linux y macOS)

El proyecto trae **Maven Wrapper** (`mvnw` / `mvnw.cmd`): la primera vez descarga la versión correcta
de Maven y todas las dependencias. Lo único que necesitas instalado es el **JDK 25+** y **Git**.

### 2.1 Instalar el JDK (una sola vez)

| Sistema | Comando |
|---|---|
| Windows | `winget install EclipseAdoptium.Temurin.25.JDK` (o el instalador de <https://adoptium.net>) |
| macOS | `brew install openjdk` |
| Linux (Arch) | `sudo pacman -S jdk-openjdk` |
| Linux (Ubuntu/Debian) | `sudo apt install openjdk-25-jdk` |

Comprueba con `java -version`. Si `mvnw` dice que no encuentra Java, define la variable `JAVA_HOME`
apuntando a la carpeta del JDK (en Windows el instalador de Adoptium tiene una casilla para hacerlo).

### 2.2 Clonar y arrancar

```bash
git clone <URL-del-repositorio>
cd porelgusto-web
```

| Sistema / terminal | Arrancar la aplicación |
|---|---|
| macOS / Linux | `./mvnw spring-boot:run` |
| Windows (PowerShell) | `.\mvnw.cmd spring-boot:run` |
| Windows (CMD) | `mvnw.cmd spring-boot:run` |
| Cualquiera con Maven instalado | `mvn spring-boot:run` |

Cuando aparezca `Started PorElGustoWebApplication`, abre:

- Aplicación: <http://localhost:8080>

Por defecto usa **MySQL** local (`application-dev.properties`): la base `porElGusto` se crea sola, Hibernate agrega
las tablas y columnas que falten y `data.sql` carga el catálogo y las cuentas de prueba.
**¿No tienes MySQL?** Arranca con el perfil `h2` (base en memoria, se borra al reiniciar):

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2
```

Para detenerla: `Ctrl + C` en la terminal.

> En macOS/Linux, si sale `permission denied: ./mvnw`, ejecuta una vez `chmod +x mvnw`.

### 2.3 Ver los cambios mientras trabajas

Deja la aplicación corriendo con `spring-boot:run` y trabaja normalmente:

| Qué cambias | Qué hacer para verlo |
|---|---|
| Vistas `.jsp` (`webapp/WEB-INF/jsp/`), CSS, JS, imágenes (`static/`) | solo **recargar el navegador** (F5) |
| Código Java (`.java`) | **compilar**: la app se reinicia sola en 1–2 s (Spring DevTools) |
| `pom.xml` o `application*.properties` | detener (`Ctrl + C`) y volver a arrancar |

Cómo "compilar" el código Java según tu editor:

- **IntelliJ IDEA:** `Ctrl+F9` / `Cmd+F9` (Build Project), o activa *Settings → Build → Compiler →
  Build project automatically* y *Advanced Settings → Allow auto-make to start even if developed application is currently running*.
- **VS Code / Eclipse / Neovim (jdtls):** compilan solos al guardar el archivo.
- **Cualquier editor:** en **otra terminal** ejecuta `./mvnw compile` (Windows: `.\mvnw.cmd compile`).

> Con el perfil `h2` la base de datos es en memoria: **cada reinicio borra los datos** de prueba.

### 2.4 Otros comandos útiles

(En Windows cambia `./mvnw` por `.\mvnw.cmd`.)

| Comando | Para qué |
|---|---|
| `./mvnw test` | ejecutar los tests |
| `./mvnw verify` | compilar + tests (**obligatorio antes de abrir un Pull Request**) |
| `./mvnw clean` | borrar `target/` si algo se comporta raro |
| `./mvnw package` | generar `target/porelgusto-web-0.0.1-SNAPSHOT.war` |
| `java -jar target/porelgusto-web-0.0.1-SNAPSHOT.war` | ejecutar el `.war` generado (como en producción; trae Tomcat incluido) |

**El puerto 8080 está ocupado:** arranca en otro puerto:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

En PowerShell pon el argumento entre comillas:
`.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"`

### 2.5 Saltos de línea entre sistemas

El repositorio ya trae `.gitattributes` y `.editorconfig` para que Windows, Linux y macOS no generen
cambios falsos por los saltos de línea. Si usas Windows, ejecuta una vez:

```bash
git config --global core.autocrlf true
```

### Perfiles y base de datos

| Perfil | Archivo | Base de datos |
|---|---|---|
| `dev` (por defecto) | `application-dev.properties` | MySQL local; Hibernate actualiza las tablas y `data.sql` carga los datos de ejemplo |
| `h2` | `application-h2.properties` | H2 en memoria en modo MySQL (se borra al reiniciar); lo usan los tests |
| `prod` | `application-prod.properties` (no está en git) | MySQL real; Hibernate solo **valida** el esquema |

- **Base nueva:** ejecuta `sql/db.sql` (ya incluye las tablas y columnas de la tienda en línea).
- **Base creada con la versión anterior de `db.sql`:** ejecuta una vez `sql/migracion-tienda-en-linea.sql` antes de arrancar con `prod`
  (en `dev` no hace falta). Al arrancar, `data.sql` reemplaza una sola vez los 28 arreglos de prueba por el catálogo real
  (los registros sin SKU) y completa los pedidos anteriores (folio, cliente e importes).

Para producción: `configVarEnv/config-env.sh` (o `.cmd`) pide los datos de conexión y activa `prod`;
también puedes copiar `application-prod.properties.example` a `application-prod.properties`. **Nunca subas contraseñas a git.**

---

## Autenticación

- `POST /api/auth/registro`: recibe `nombre`, `direccion`, `correoElectronico`, `telefono` y `password` (mínimo 8 caracteres). Asigna el tipo de usuario configurado y devuelve un JWT.
- `POST /api/auth/login`: recibe `correoElectronico` y `password`; la respuesta contiene `token`, `tokenType` (`Bearer`), `expiresIn` y un resumen del usuario.
- Las contraseñas se almacenan con BCrypt. Configura `APP_AUTH_TOKEN_SECRET` con un secreto aleatorio de al menos 32 bytes antes de desplegar; el valor del perfil `dev` es solo para desarrollo.
- `APP_AUTH_DEFAULT_USER_TYPE_ID` define el ID de `TipoUsuario` asignado al registrarse (por defecto `1`); asegúrate de que exista en la base de datos.

Ejemplo de registro:

```json
{
  "nombre": "Ana López",
  "direccion": "Calle 1",
  "correoElectronico": "ana@example.com",
  "telefono": "5551234567",
  "password": "una-clave-segura"
}
```

El JWT se entrega al cliente para autenticación de sesión. Las rutas CRUD existentes aún no validan ese token.

El sitio web usa sesión (cookie): `/login` acepta correo o nombre, `/cuenta` requiere sesión y `/admin` revisa los permisos del rol
(`model/Permisos.java`). Todos los formularios `POST` llevan token CSRF.

## 3. Estructura del proyecto

```
porelgusto-web/
├── pom.xml                         # dependencias y versiones (Maven)
├── mvnw, mvnw.cmd, .mvn/           # Maven Wrapper (no hace falta instalar Maven)
├── configVarEnv/                   # scripts que piden los datos de MySQL y activan el perfil prod
├── sql/db.sql                      # esquema completo de MySQL
├── sql/migracion-tienda-en-linea.sql  # columnas/tablas nuevas para bases creadas antes
└── src/
    ├── main/
    │   ├── java/com/neksbyte/porelgustoweb/
    │   │   ├── PorElGustoWebApplication.java   # punto de entrada (main)
    │   │   ├── config/        # sesión, permisos por rol, CSRF, datos de la tienda, formatos para JSP
    │   │   ├── controller/    # controladores del sitio (+ CRUD JSON /api/...) y GlobalAdvice
    │   │   │   └── admin/     # controladores del panel de administración
    │   │   ├── dto/           # formularios con validación y objetos para las vistas
    │   │   ├── service/       # interfaces de servicio (+ impl/): lógica de negocio y @Transactional
    │   │   │   └── pago/      # EstrategiaPago: tarjeta (Stripe) y PayPal
    │   │   ├── dao/           # DAO (XxxDao + impl/): consultas sobre los repositorios
    │   │   ├── repository/    # interfaces JpaRepository
    │   │   ├── model/         # @Entity (tablas de sql/db.sql) y enums
    │   │   └── exception/     # ReglaNegocioException, NoEncontradoException
    │   ├── resources/
    │   │   ├── application.properties          # configuración común (tienda, pagos, WhatsApp, envío, IVA)
    │   │   ├── application-dev.properties      # MySQL local
    │   │   ├── application-h2.properties       # H2 en memoria (sin instalar MySQL)
    │   │   ├── data.sql                        # datos de ejemplo (catálogo de 88 arreglos, cuentas, cupones, pedidos)
    │   │   └── static/                         # archivos públicos (css/styles.css, js/app.js, img/productos, imagenes/)
    │   └── webapp/WEB-INF/
    │       ├── pg.tld                          # funciones para JSP: pg:dinero, pg:fecha...
    │       ├── tags/                           # plantillas: pagina.tag, admin.tag, cuenta.tag, producto.tag
    │       └── jsp/                            # vistas (auth/, cuenta/, admin/ y las del sitio)
    └── test/java/com/neksbyte/porelgustoweb/       # tests (corren con el perfil h2)
```

### Arquitectura por capas

```
Navegador ──► Servlet ──► Service ──► Repository / DAO ──► Base de datos
   ▲         (controller)  (negocio)      (JPA / Hibernate)
   └──── vista JSP ◄──┘
```

Reglas:

1. **Servlet** (`controller/`) solo recibe la petición, valida (`@Valid`), llama al Service y devuelve el
   nombre de la vista JSP. Sin lógica de negocio ni acceso directo a la base de datos.
   Son clases `@Controller` de Spring MVC, pero el equipo las nombra **`XxxServlet`**.
2. **Service** contiene la lógica y las transacciones (`@Transactional`). Es el único que usa repositorios y DAOs.
3. **Repository**: interfaz que extiende `JpaRepository<Entidad, Integer>` para el CRUD normal.
4. **DAO**: cuando una consulta es compleja y no basta con el repositorio, se escribe en un `XxxDao`
   usando `EntityManager`/HQL.
5. Inyección de dependencias **por constructor** (campos `private final`), nunca `@Autowired` en atributos.
6. Si el Servlet devuelve `"productos/lista"`, se muestra `src/main/webapp/WEB-INF/jsp/productos/lista.jsp`.
7. Las vistas usan una **plantilla** (`WEB-INF/tags/pagina.tag`) que ya trae el `<head>`, Bootstrap, el menú,
   los mensajes y el pie de página. Cada JSP solo escribe su contenido:

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
   <%@ taglib prefix="c" uri="jakarta.tags.core" %>
   <%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
   <%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
   <t:pagina titulo="Productos">
       <div class="container py-4">
           <c:forEach items="${productos}" var="p">
               <p><c:out value="${p.nombre}"/> · ${pg:dinero(p.costo)}</p>
           </c:forEach>
       </div>
   </t:pagina>
   ```

   El panel usa `<t:admin>` y Mi cuenta `<t:cuenta>`. Bootstrap se sirve desde el proyecto (WebJar).
   Todo formulario `POST` lleva el token CSRF: `<input type="hidden" name="_csrf" value="${csrfToken}">`.

   Usa siempre `<c:out>` (o `${...}` dentro de etiquetas JSTL) para mostrar datos del usuario y
   `<c:url>` para los enlaces. No escribas código Java (`<% ... %>`) en los JSP.

### Convenciones de nombres

| Elemento | Convención | Ejemplo |
|---|---|---|
| Entidad | singular, PascalCase | `Producto` |
| Repositorio | `<Entidad>Repository` | `ProductoRepository` |
| Servicio | `<Entidad>Service` | `ProductoService` |
| Servlet (controlador) | `<Entidad>Servlet` | `ProductoServlet` |
| DAO | `<Entidad>Dao` | `ProductoDao` |
| Tabla | plural, snake_case | `productos` |
| Vistas | carpeta por entidad, minúsculas | `WEB-INF/jsp/productos/lista.jsp` |
| URLs | plural, minúsculas, con guiones | `/productos`, `/productos/nuevo` |

Código, nombres de clases y variables: en **español** de forma consistente (o en inglés, pero no mezclar dentro de una misma clase).

---

## 4. Modelo de trabajo

### Ramas (Git Flow simplificado)

| Rama | Uso | ¿Quién escribe? |
|---|---|---|
| `main` | versión estable / entregable | solo por Pull Request desde `develop` |
| `develop` | integración de lo terminado | solo por Pull Request |
| `feature/<descripcion>` | una tarea nueva | cada desarrollador |
| `fix/<descripcion>` | corrección de un error | cada desarrollador |

**Nunca hagas commit directo en `main` ni en `develop`.**

### Flujo para cada tarea

```bash
# 1. Partir siempre de develop actualizado
git switch develop
git pull

# 2. Crear tu rama
git switch -c feature/crud-productos

# 3. Trabajar y hacer commits pequeños
git add .
git commit -m "feat: agregar entidad Producto y su repositorio"

# 4. Antes de subir, traer lo último de develop y comprobar que todo compila
git pull origin develop
./mvnw verify

# 5. Subir y abrir Pull Request hacia develop
git push -u origin feature/crud-productos
```

### Mensajes de commit (Conventional Commits)

Formato: `tipo: descripción corta en presente`

| Tipo | Cuándo |
|---|---|
| `feat` | funcionalidad nueva |
| `fix` | corrección de error |
| `refactor` | cambio de código sin cambiar comportamiento |
| `style` | formato, CSS, vistas sin lógica |
| `test` | añadir o corregir tests |
| `docs` | documentación |
| `chore` | dependencias, configuración, build |

Ejemplos: `feat: formulario de registro de usuarios`, `fix: validación de email vacío`.

### Pull Requests

- Un PR = una tarea. Pequeño y fácil de revisar.
- Antes de abrirlo: `./mvnw verify` debe pasar sin errores.
- Describe **qué** cambia y **cómo probarlo**; añade captura si cambia una vista.
- Necesita **al menos 1 aprobación** de otro compañero antes de hacer merge.
- Quien abre el PR resuelve los conflictos; después del merge se borra la rama.

### Tareas

- Cada tarea se registra (issue / tablero) antes de empezar y se asigna a una persona.
- Estados: **Por hacer → En progreso → En revisión (PR abierto) → Hecho (merge en develop)**.
- Si una tarea toma más de 2–3 días, divídela en partes más pequeñas.

### Reglas generales

- No subir contraseñas, `.env`, `target/` ni archivos del IDE.
- Si agregas una dependencia al `pom.xml`, avísalo en el PR.
- Si cambias una entidad, revisa que no rompas datos/tablas de los demás.
- Respeta el `.editorconfig` (UTF-8, saltos de línea LF, 4 espacios).
