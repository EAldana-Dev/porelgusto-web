<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="${solicitud.asunto}">
<div class="container py-4" style="max-width: 800px">
    <a href="${ctx}/soporte" class="small"><i class="bi bi-arrow-left me-1"></i>Soporte</a>
    <div class="d-flex justify-content-between align-items-start mt-2 mb-4">
        <div>
            <h1 class="display-6 mb-0"><c:out value="${solicitud.asunto}"/></h1>
            <c:if test="${not empty solicitud.folioPedido}"><p class="small texto-gris mb-0">Pedido <a href="${ctx}/pedido/${solicitud.folioPedido}">${solicitud.folioPedido}</a></p></c:if>
        </div>
        <span class="badge text-bg-${solicitud.estado.color} fs-6">${solicitud.estado.etiqueta}</span>
    </div>
    <div class="d-grid gap-3 mb-4">
        <c:forEach items="${solicitud.mensajes}" var="m">
            <div class="tarjeta p-3 ${m.dePersonal ? 'bg-rosa-suave ms-lg-5' : 'me-lg-5'}">
                <div class="small mb-1"><strong><c:out value="${m.nombreAutor}"/></strong>${m.dePersonal ? ' · Por el Gusto' : ''} <span class="texto-gris">· ${pg:fechaHora(m.fechaHora)}</span></div>
                <div style="white-space: pre-line"><c:out value="${m.contenido}"/></div>
            </div>
        </c:forEach>
    </div>
    <c:if test="${solicitud.estado != 'CERRADO'}">
        <form action="${ctx}/soporte/${solicitud.idSolicitud}/responder" method="post" class="tarjeta p-3">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <label for="mensaje" class="form-label">Responder</label>
            <textarea id="mensaje" name="mensaje" rows="3" class="form-control mb-3" required></textarea>
            <button class="btn btn-ciruela px-4">Enviar</button>
        </form>
    </c:if>
</div>
</t:pagina>
