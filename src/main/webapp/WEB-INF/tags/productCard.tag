<%@ tag body-content="empty" %>
<%@ attribute name="product" required="true"
    type="com.neksbyte.porelgustoweb.model.ArregloFloral" %>

<div class="product-card">
    <img src="https://placehold.co/200x150" alt="${product.nombre}">
    <h3>${product.nombre}</h3>
    <h4>${product.costo}</h4>
    <a href="${pageContext.request.contextPath}/Catalogo/${product.nombre.replace(' ', '-')}">
        <button type="button">Ver producto</button>
    </a>
</div>
