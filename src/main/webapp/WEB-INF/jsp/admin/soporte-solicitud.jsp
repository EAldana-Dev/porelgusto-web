<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="${solicitud.asunto}">
    <a href="${ctx}/admin/soporte" class="small"><i class="bi bi-arrow-left me-1"></i>Soporte</a>
    <div class="row g-4 mt-1">
        <div class="col-xl-8">
            <div class="d-grid gap-3 mb-4">
                <c:forEach items="${solicitud.mensajes}" var="m">
                    <div class="tarjeta p-3 ${m.dePersonal ? 'bg-rosa-suave ms-lg-5' : 'me-lg-5'}">
                        <div class="small mb-1"><strong><c:out value="${m.nombreAutor}"/></strong> <span class="texto-gris">· ${pg:fechaHora(m.fechaHora)}</span></div>
                        <div style="white-space: pre-line"><c:out value="${m.contenido}"/></div>
                    </div>
                </c:forEach>
            </div>
            <c:if test="${solicitud.estado != 'CERRADO'}">
                <form action="${ctx}/admin/soporte/${solicitud.idSolicitud}/responder" method="post" class="tarjeta p-3">
                    <input type="hidden" name="_csrf" value="${csrfToken}">
                    <label for="mensaje" class="form-label">Responder al cliente (se envía por correo)</label>
                    <textarea id="mensaje" name="mensaje" rows="4" class="form-control mb-3" required></textarea>
                    <button class="btn btn-ciruela px-4">Enviar respuesta</button>
                </form>
            </c:if>
        </div>
        <div class="col-xl-4">
            <div class="tarjeta p-4">
                <span class="badge text-bg-${solicitud.estado.color} mb-2">${solicitud.estado.etiqueta}</span>
                <p class="small mb-1"><strong><c:out value="${solicitud.nombreContacto}"/></strong></p>
                <p class="small mb-1"><a href="mailto:${solicitud.correoContacto}"><c:out value="${solicitud.correoContacto}"/></a></p>
                <c:if test="${not empty solicitud.folioPedido}"><p class="small mb-1">Pedido: <a href="${ctx}/admin/pedidos/${solicitud.folioPedido}">${solicitud.folioPedido}</a></p></c:if>
                <p class="small mb-1">Responder antes de: ${pg:fechaHora(solicitud.fechaLimite)}</p>
                <p class="small mb-3">Atiende: <c:out value="${empty solicitud.agente ? 'Sin asignar' : solicitud.agente.nombre}"/></p>
                <c:if test="${solicitud.estado != 'CERRADO'}">
                    <div class="d-grid gap-2">
                        <c:if test="${solicitud.agente.idUsuario != usuarioActual.idUsuario}">
                            <form action="${ctx}/admin/soporte/${solicitud.idSolicitud}/tomar" method="post"><input type="hidden" name="_csrf" value="${csrfToken}"><button class="btn btn-outline-ciruela w-100">Atender esta solicitud</button></form>
                        </c:if>
                        <form action="${ctx}/admin/soporte/${solicitud.idSolicitud}/cerrar" method="post" data-confirmar="¿Cerrar la solicitud?"><input type="hidden" name="_csrf" value="${csrfToken}"><button class="btn btn-outline-secondary w-100">Cerrar solicitud</button></form>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</t:admin>
