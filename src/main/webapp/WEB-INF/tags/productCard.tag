<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="product" required="true"
type="com.neksbyte.porelgustoweb.model.ArregloFloral" %>

<article class="product-card">
    <div class="product-card__media">
        <c:choose>
            <c:when test="${not empty product.imagenes}">
                <img src="${pageContext.request.contextPath}${product.imagenes[0].direccionImg}"
                     alt="${product.nombre}" loading="lazy">
            </c:when>
            <c:otherwise>
                <div class="product-card__placeholder" aria-label="Imagen no disponible">🌷</div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="product-card__content">
        <h2 class="product-card__title">${product.nombre}</h2>
        <p class="product-card__price">$${product.costo}</p>
        <a class="product-card__link"
           href="${pageContext.request.contextPath}/Catalogo/${product.nombre.replace(' ', '-')}">
            Ver producto
        </a>
    </div>
</article>
