<%@ tag body-content="empty" %>
<%@ attribute name="product" required="true"
type="com.neksbyte.porelgustoweb.model.ArregloFloral" %>

<div class="product-card">
    <c:if test="${not empty product.imagenes}">
        <img src="${product.imagenes[0].direccionImg}" alt="${product.nombre}">
    </c:if>

    <h3>${product.nombre}</h3>
    <h4>$${product.costo}</h4>
        <a href="${pageContext.request.contextPath}/Catalogo/${product.nombre.replace(' ', '-')}">
        <button type="button">Ver producto</button>
    </a>
</div>
