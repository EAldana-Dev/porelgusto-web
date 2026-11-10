<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Por el Gusto</title>
            <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/css/bootstrap.min.css'/>">
            <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
        </head>

        <body>

            <header>
                <nav class="navbar navbar-expand-lg">
                    <div class="container">
                        <a class="navbar-brand d-flex flex-column lh-1" href="#">
                            <span class="fs-3 fw-bold"> POR EL GUSTO</span>
                            <span class="small">ENVÍO DE FLORES</span>
                        </a>
                        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menu"
                            aria-controls="menu" aria-expanded="false" aria-label="Abrir menú">
                            <span class="navbar-toggler-icon"></span>
                        </button>
                        <div class="collapse navbar-collapse" id="menu">
                            <ul class="navbar-nav ms-auto small text-uppercase">
                                <li class="nav-item border-end"><a class="nav-link px-3" href="#">Inicio</a></li>
                                <li class="nav-item border-end"><a class="nav-link px-3"
                                        href="/catalogo">Colecciones</a></li>
                                <li class="nav-item border-end"><a class="nav-link px-3" href="#">Nosotros</a></li>
                                <li class="nav-item"><a class="nav-link px-3" href="#">Contacto</a></li>
                            </ul>
                            <form class="ms-lg-3 my-2 my-lg-0" role="search">
                                <div class="input-group">
                                    <label for="buscar" class="visually-hidden">Buscar</label>
                                    <input type="search" id="buscar" class="form-control rounded-start-pill"
                                        placeholder="BUSCAR">
                                    <button class="btn btn-outline-secondary rounded-end-pill" type="submit"
                                        aria-label="Buscar">
                                        <i class="bi bi-search"></i>
                                    </button>
                                </div>
                            </form>
                            <a href="#" class="ms-lg-3 fs-4 text-reset" aria-label="Mi cuenta"><i
                                    class="bi bi-person"></i></a>
                            <a href="#" class="ms-3 fs-4 text-reset" aria-label="Carrito"><i
                                    class="bi bi-cup-hot"></i></a>
                        </div>
                    </div>
                </nav>
            </header>

            <footer>

            </footer>

            <script src="<c:url value='/webjars/bootstrap/js/bootstrap.bundle.min.js'/>"></script>
            <script src="<c:url value='/js/app.js'/>"></script>
        </body>

        </html>
