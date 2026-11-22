<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %> <%@
taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Por el Gusto</title>

        <link rel="stylesheet" href="<c:url value='/css/home.css'/>" />
        <script src="<c:url value='/js/home.js'/>" defer></script>
    </head>

    <body>
        <div class="pagina">
            <div class="anuncio_superior"></div>

            <nav class="barra_superior">
                <div class="inicio">
                    <a href="" class="titulo">POR EL GUSTO</a>
                    <a href="" class="envio_de_flores">ENVIO DE FLORES</a>
                </div>

                <button
                    class="btn_menu"
                    type="button"
                    aria-label="Abrir menú de navegación"
                    aria-expanded="false"
                    aria-controls="opciones"
                >
                    ☰
                </button>
                <div class="opciones" id="opciones">
                    <a href="">Tendencias</a>

                    <div class="catalogo">
                        <a href="#" class="enlace_catalogo">Catálogo ⛛</a>

                        <div class="opciones_catalogo">
                            <a href="">Temporada</a>
                            <a href="">Detalles</a>
                            <a href="">Floreros</a>
                            <a href="">Ramos</a>
                            <a href="">Cajas</a>
                            <a href="">Orquideas</a>
                            <a href="">Extras</a>
                        </div>
                    </div>

                    <a href="">Ofertas</a>
                    <a href="">Pedidos</a>
                    <a href="">Contacto</a>
                </div>

                <form class="busqueda" action="/catalogo" method="GET">
                    <input
                        type="search"
                        name="buscar"
                        placeholder="Buscar"
                        class="barra_busqueda"
                    />
                </form>

                <div class="perfil">
                    <a href="carrito">
                        <img
                            src="/imagenes/Carrito de compras.jpg"
                            alt="Carrito de compras"
                            class="carrito"
                        />
                    </a>

                    <a href="/login">
                        <img
                            src="/imagenes/foto_de_perfil.jpg"
                            alt="Perfil"
                            class="foto_de_perfil"
                        />
                    </a>
                </div>
            </nav>

            <main class="main">
                <div class="anuncio">
                    <h1>Coleccion primavera</h1>
                    <p>Hasta 20% de descuento</p>
                    <button class="comprar_ahora">Comprar ahora</button>
                </div>
            </main>

        </div>
        <script src="<c:url value='/webjars/bootstrap/js/bootstrap.bundle.min.js'/>"></script>
    </body>
</html>
