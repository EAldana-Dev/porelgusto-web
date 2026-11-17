
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
                    <a href="">Catálogo ▲</a>

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
