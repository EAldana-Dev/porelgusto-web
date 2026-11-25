<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${producto.nombre}</title>
</head>
<body>
    <c:forEach var="imagen" items="${producto.imagenes}">
        <img src="${pageContext.request.contextPath}${imagen.direccionImg}" alt="${producto.nombre}">
    </c:forEach>

    <h1>${producto.nombre}</h1>
    <h2>$${producto.costo}</h2>
    <p>${producto.detalles}</p>

    <br>
    <a href="${urlProducto}/QR">ir a codigo</a>
    <br>
    <form action="${pageContext.request.contextPath}/MiCarrito/buy" method="POST">
        <input type="hidden" name="productoId" value="${producto.idArregloFloral}" />


        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <button type="submit">Comprar</button>
    </form>
</body>
</html>
