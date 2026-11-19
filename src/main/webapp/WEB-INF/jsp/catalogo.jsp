<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="miLiberiaParaProducto" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Mi tienda</title>

    <link rel="stylesheet" href="/css/styleCard.css">
</head>

<body>

    <h1>Catalogo de flores</h1>

    <div class="product-container">

    <c:forEach var="product" items="${products}">
        <miLiberiaParaProducto:productCard product="${product}" />
    </c:forEach>

    </div>


</body>
</html>
