<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="WhatsApp no disponible">
<div class="container py-5 text-center" style="max-width: 560px">
    <i class="bi bi-whatsapp display-3 texto-gris"></i>
    <h1 class="display-6 mt-3">Por ahora no podemos atenderte por WhatsApp</h1>
    <p class="texto-gris">Atendemos pedidos por WhatsApp de ${tienda.whatsappDesde}:00 a ${tienda.whatsappHasta}:00 h. Puedes pagar en línea ahora mismo o dejarnos un mensaje:</p>
    <div class="tarjeta p-4 text-start d-grid gap-2 mb-4">
        <a href="https://wa.me/${tienda.whatsapp}" target="_blank" rel="noopener"><i class="bi bi-whatsapp me-2"></i>WhatsApp <c:out value="${tienda.telefono}"/></a>
        <a href="mailto:${tienda.correo}"><i class="bi bi-envelope me-2"></i><c:out value="${tienda.correo}"/></a>
    </div>
    <a href="${ctx}/checkout" class="btn btn-ciruela px-4">Pagar en línea</a>
    <a href="${ctx}/carrito" class="btn btn-link">Volver al carrito</a>
</div>
</t:pagina>
