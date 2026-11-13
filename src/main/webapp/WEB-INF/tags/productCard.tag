<%@ tag body-content="empty" %>
<%@ attribute name="product" required="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="product-card">
    <img src="https://placehold.co/200x150" alt="Producto">
    <h3>${product}</h3>
    <button>Ver producto</button>
</div>
