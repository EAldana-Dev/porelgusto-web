
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Por el Gusto</title>

    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>

<body>

    <header>
        <nav class="padresita">


                <h1 class="titulo">POR EL GUSTO</h1>
                <span class="pie_pagina">ENVÍO DE FLORES</span>


            <div class="botones">
                <p>Inicio</p>
                <p>Colecciones</p>
                <p>Nosotros</p>
                <p>Contacto</p>
            </div>

            <form action="<c:url value='/catalogo'/>" method="get">
                <input type="search" name="buscar" placeholder="Buscar">
                <button type="submit">Buscar</button>
            </form>

            <div>
                <p>Cuenta</p>
                <p>Carrito</p>
            </div>

        </nav>
    </header>

    <main>

    </main>

    <footer>

    </footer>

</body>
</html>
