# Por el Gusto — Web

Proyecto del equipo **Neksbyte**. Esta es la **aplicación web** de Por el Gusto (la app móvil vive en otro repositorio). Hecha con **Java**, **Spring Boot**, **Hibernate (JPA)**, **JSP (con JSTL)** y **Bootstrap**.

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
- Consola de la base de datos H2: <http://localhost:8080/h2-console>
  (JDBC URL: `jdbc:h2:mem:porelgustoweb`, usuario `sa`, sin contraseña)

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

> Recuerda que la base de datos H2 es en memoria: **cada reinicio borra los datos** de prueba.

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
| `dev` (por defecto) | `application-dev.properties` | H2 en memoria (se borra al reiniciar) |
| `prod` | `application-prod.properties` (no está en git) | la real (PostgreSQL/MySQL) |

Para producción: copiar `application-prod.properties.example` a `application-prod.properties`,
rellenar los datos, añadir el driver de la base de datos al `pom.xml` y arrancar con
`--spring.profiles.active=prod`. **Nunca subas contraseñas a git.**

---

## 3. Estructura del proyecto

```
porelgusto-web/
├── pom.xml                         # dependencias y versiones (Maven)
├── mvnw, mvnw.cmd, .mvn/           # Maven Wrapper (no hace falta instalar Maven)
├── .editorconfig                   # formato común: UTF-8, LF, 4 espacios
├── .gitignore
├── README.md
└── src/
    ├── main/
    │   ├── java/com/neksbyte/porelgustoweb/
    │   │   ├── PorElGustoWebApplication.java   # punto de entrada (main)
    │   │   ├── config/        # @Configuration: beans, seguridad, etc.
    │   │   ├── controller/    # Servlets (XxxServlet): reciben peticiones y devuelven vistas JSP
    │   │   ├── service/       # @Service: lógica de negocio y @Transactional
    │   │   ├── repository/    # interfaces JpaRepository (acceso a datos sencillo)
    │   │   ├── dao/           # DAO (XxxDao): consultas propias con Hibernate/EntityManager
    │   │   ├── model/         # @Entity: entidades JPA/Hibernate (tablas)
    │   │   └── exception/     # excepciones propias y @ControllerAdvice
    │   ├── resources/
    │   │   ├── application.properties          # configuración común (prefijo/sufijo de JSP)
    │   │   ├── application-dev.properties      # configuración de desarrollo (H2, recarga de JSP)
    │   │   ├── application-prod.properties.example
    │   │   └── static/                         # archivos públicos (se sirven tal cual)
    │   │       ├── css/styles.css
    │   │       ├── js/app.js
    │   │       └── img/
    │   └── webapp/
    │       └── WEB-INF/jsp/                    # vistas JSP (no accesibles directamente por URL)
    │           ├── fragments/header.jsp        # <head>, Bootstrap y barra de navegación
    │           ├── fragments/footer.jsp        # pie de página y scripts
    │           ├── error/404.jsp               # páginas de error (404.jsp, 500.jsp...)
    │           └── index.jsp                   # página de inicio
    └── test/java/com/neksbyte/porelgustoweb/       # tests (misma estructura de paquetes)
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
3. **Repository**: interfaz que extiende `JpaRepository<Entidad, Long>` para el CRUD normal.
4. **DAO**: cuando una consulta es compleja y no basta con el repositorio, se escribe en un `XxxDao`
   usando `EntityManager`/HQL.
5. Inyección de dependencias **por constructor** (campos `private final`), nunca `@Autowired` en atributos.
6. Si el Servlet devuelve `"productos/lista"`, se muestra `src/main/webapp/WEB-INF/jsp/productos/lista.jsp`.
7. Las vistas nuevas incluyen la cabecera y el pie comunes:

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
   <%@ taglib prefix="c" uri="jakarta.tags.core" %>
   <c:set var="titulo" value="Productos"/>  <%-- o pásalo desde el Servlet con model.addAttribute --%>
   <%@ include file="/WEB-INF/jsp/fragments/header.jsp" %>

   <!-- contenido de la página con clases de Bootstrap -->
   <c:forEach items="${productos}" var="p">
       <p><c:out value="${p.nombre}"/></p>
   </c:forEach>

   <%@ include file="/WEB-INF/jsp/fragments/footer.jsp" %>
   ```

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
# porelgusto-web
