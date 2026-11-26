<%@ tag description="Tarjeta de producto del catálogo" pageEncoding="UTF-8" %>
<%@ attribute name="v" required="true" type="com.neksbyte.porelgustoweb.dto.ProductoVista" %>
<%@ attribute name="enDeseos" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="p" value="${v.producto}"/>
<article class="producto-card h-100 d-flex flex-column ${p.disponible ? '' : 'agotado'}">
    <div class="insignias">
        <c:if test="${v.conDescuento}"><span class="insignia insignia-oferta">-<c:out value="${v.promocion.descuentoTexto}"/></span></c:if>
        <c:if test="${p.nuevo}"><span class="insignia insignia-nuevo">Nuevo</span></c:if>
        <c:if test="${not p.disponible}"><span class="insignia insignia-agotado">No disponible</span></c:if>
    </div>
    <form action="${ctx}/deseos/${p.sku}" method="post">
        <input type="hidden" name="_csrf" value="${csrfToken}">
        <input type="hidden" name="accion" value="${enDeseos ? 'quitar' : 'agregar'}">
        <button class="btn-corazon" aria-label="${enDeseos ? 'Quitar de' : 'Agregar a'} mi lista de deseos" title="${enDeseos ? 'Quitar de' : 'Guardar en'} mi lista de deseos">
            <i class="bi ${enDeseos ? 'bi-heart-fill' : 'bi-heart'}"></i>
        </button>
    </form>
    <a href="${ctx}/producto/${p.sku}" class="foto">
        <img src="${ctx}${p.imagenUrl}" alt="<c:out value='${p.nombre}'/>" loading="lazy" width="720" height="960">
    </a>
    <div class="p-3 d-flex flex-column flex-grow-1">
        <span class="etiqueta-seccion mb-1"><c:out value="${p.categoria}"/></span>
        <a href="${ctx}/producto/${p.sku}" class="nombre mb-2"><c:out value="${p.nombre}"/></a>
        <div class="mt-auto d-flex align-items-baseline gap-2">
            <span class="precio ${v.conDescuento ? 'texto-ciruela' : ''}">${pg:dinero(v.precioFinal)}</span>
            <c:if test="${v.conDescuento}"><span class="precio-antes">${pg:dinero(p.costo)}</span></c:if>
            <span class="ms-auto small texto-gris">${p.sku}</span>
        </div>
    </div>
</article>
