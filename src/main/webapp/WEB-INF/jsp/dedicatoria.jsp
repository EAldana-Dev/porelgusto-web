<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Un mensaje para ti" simple="true">
<div class="dedicatoria-fondo d-flex align-items-center py-5">
    <div class="container text-center" style="max-width: 680px">
        <div class="d-flex flex-column lh-1 mb-4">
            <span class="logo-titulo">POR EL GUSTO</span>
            <span class="logo-subtitulo">ENVÍO DE FLORES</span>
        </div>
        <c:choose>
            <c:when test="${pedido.dedicatoriaVencida or empty pedido.dedicatoria}">
                <p class="fs-4">Este mensaje ya no está disponible.</p>
            </c:when>
            <c:otherwise>
                <p class="etiqueta-seccion">Para <c:out value="${pedido.destinatario}"/></p>
                <i class="bi bi-flower1 display-4 texto-rosa"></i>
                <p class="dedicatoria-texto my-4">“<c:out value="${pedido.dedicatoria}"/>”</p>
                <p class="texto-gris">Con cariño, <c:out value="${fn:split(pedido.nombreCliente, ' ')[0]}"/></p>
                <div class="d-flex justify-content-center gap-2 flex-wrap mt-4">
                    <c:forEach items="${pedido.items}" var="i">
                        <img src="${ctx}${i.arregloFloral.imagenUrl}" alt="<c:out value='${i.arregloFloral.nombre}'/>" class="rounded-4 shadow-sm" style="width: 110px; height: 140px; object-fit: cover">
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
        <a href="${ctx}/" class="btn btn-outline-ciruela mt-5">Conoce Por el Gusto</a>
    </div>
</div>
</t:pagina>
