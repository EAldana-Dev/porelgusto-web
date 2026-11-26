<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="Soporte">
    <div class="tarjeta">
        <div class="table-responsive">
            <table class="table tabla-pg align-middle small mb-0">
                <thead><tr><th>Asunto</th><th>Cliente</th><th>Folio</th><th>Atiende</th><th>Recibido</th><th>Estado</th></tr></thead>
                <tbody>
                <c:forEach items="${solicitudes}" var="s">
                    <tr>
                        <td><a href="${ctx}/admin/soporte/${s.idSolicitud}" class="fw-semibold"><c:out value="${s.asunto}"/></a></td>
                        <td><c:out value="${s.nombreContacto}"/><br><span class="texto-gris"><c:out value="${s.correoContacto}"/></span></td>
                        <td>${empty s.folioPedido ? '—' : s.folioPedido}</td>
                        <td><c:out value="${empty s.agente ? '—' : s.agente.primerNombre}"/></td>
                        <td>${pg:hace(s.fechaHora)}</td>
                        <td><span class="badge text-bg-${s.estado.color}">${s.estado.etiqueta}</span></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty solicitudes}"><tr><td colspan="6" class="text-center texto-gris py-5">No hay solicitudes de soporte.</td></tr></c:if>
                </tbody>
            </table>
        </div>
    </div>
</t:admin>
