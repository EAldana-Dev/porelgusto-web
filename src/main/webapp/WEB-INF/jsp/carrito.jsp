<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${titulo}</title>

    <link rel="stylesheet" href="<c:url value='/css/carrito.css'/>">
</head>

<body>
    <div class="container">

        <div class="encabezado">
            <a href="/home" class="titulo">POR EL GUSTO</a>
            <a href="/Catalogo" class="seguir_comprando">Seguir comprando</a>
        </div>

        <h1 class="mi_carrito">Mi carrito</h1>

        <c:if test="${empty productos}">
            <p class="vacio">Tu carrito está vacío 🌷</p>
        </c:if>

        <c:if test="${not empty productos}">
            <div class="contenido">

                <div class="lista_productos">
                    <c:forEach var="item" items="${productos}">
                        <div class="producto">
                            <c:if test="${not empty item.arregloFloral.imagenes}">
                                <img src="${item.arregloFloral.imagenes[0].direccionImg}" alt="${item.arregloFloral.nombre}" class="imagen">
                            </c:if>

                            <div class="datos">
                                <h3>${item.arregloFloral.nombre}</h3>
                                <p>${item.arregloFloral.detalles}</p>
                                <p>Cantidad: ${item.cantidad}</p>
                            </div>

                            <h3 class="precio">$${item.precioUnitario}</h3>
                        </div>
                    </c:forEach>
                </div>

                <div class="resumen">
                    <h2>Resumen</h2>
                    <p class="total">Total: $${total}</p>

                    <a href="/compra">
                        <button type="button" class="btn_comprar">Comprar</button>
                    </a>
                </div>

            </div>
        </c:if>

    </div>
</body>
</html>
