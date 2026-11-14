
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:ital,opsz,wght@0,9..40,100..1000;1,9..40,100..1000&display=swap" rel="stylesheet">
    <title>Por el Gusto</title>

    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>

<body>
    <div class="container">
        <div class="anuncio_superior">
            <p class="envio_gratis">
                Envios gratis en compras desde $1500. Entrega en el mismo dia 🚚
            </p>
        </div>
        
        <div class="barra_superior">
            <div class="inicio">
                <a href="" class="titulo">POR EL GUSTO</a>
                <a href="" class="envio_de_flores">ENVIO DE FLORES</a>
            </div>

            <div class="opciones">

                <a href="">
                    Tendencias
                </a>

                <div class="catalogo">
                    <a href="">Catálogo</a>

                    <div class="opciones_catalogo">
                        <a href="">Pasen</a>
                        <a href="">la lista</a>
                        <a href="">d q vamos a vender</a>
                    </div>
                </div>

                <a href="">
                    Ofertas
                </a>

                <a href="">
                    Pedidos
                </a>

                <a href="">
                    Contacto
                </a>
            
            </div>

            <form class="busqueda">
                <input type="search" name="buscar" placeholder="Buscar" class="barra_busqueda">
                <button type="submit" class="btn_busqueda">Buscar</button>
            </form>

            <div class="perfil">
                <img src="/imagenes/Carrito de compras.jpg" alt="Foto de perfil" class="carrito">
                <img src="/imagenes/foto_de_perfil.jpg" alt="Foto de perfil" class="foto_de_perfil">
            </div>

        </div>

    </div>
</body>
</html>
