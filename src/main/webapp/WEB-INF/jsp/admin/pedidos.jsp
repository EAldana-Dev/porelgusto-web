<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="Pedidos">
    <div class="d-flex flex-wrap gap-2 mb-3">
        <a href="${ctx}/admin/pedidos" class="chip-cat ${empty estado and not mios ? 'activo' : ''}">Todos</a>
        <c:forEach items="${estados}" var="e">
            <a href="${ctx}/admin/pedidos?estado=${e}" class="chip-cat ${estado == e ? 'activo' : ''}">${e.etiqueta}</a>
        </c:forEach>
        <c:if test="${usuarioActual.admin}"><a href="${ctx}/admin/pedidos?mios=true" class="chip-cat ${mios ? 'activo' : ''}"><i class="bi bi-person-check me-1"></i>Asignados a mí</a></c:if>
    </div>
    <c:if test="${not usuarioActual.admin}"><p class="small texto-gris">Ves los pedidos que tienes asignados.</p></c:if>
    <div class="tarjeta">
        <div class="table-responsive">
            <table class="table tabla-pg align-middle small mb-0">
                <thead><tr><th>Folio</th><th>Fecha</th><th>Cliente</th><th>Entrega</th><th>Pago</th><th>Asignado</th><th class="text-end">Total</th><th>Estado</th></tr></thead>
                <tbody>
                <c:forEach items="${pedidos}" var="o">
                    <tr>
                        <td><a href="${ctx}/admin/pedidos/${o.folio}" class="fw-semibold">${o.folio}</a></td>
                        <td class="texto-gris">${pg:fecha(o.fechaHora)}</td>
                        <td><c:out value="${o.nombreCliente}"/><br><span class="texto-gris"><c:out value="${o.telefonoCliente}"/></span></td>
                        <td>${pg:fecha(o.fechaEntrega)}<br><span class="texto-gris">${o.horarioEntrega}</span></td>
                        <td><c:out value="${empty o.formaPago ? '—' : o.formaPago.etiqueta}"/></td>
                        <td><c:out value="${empty o.empleado ? '—' : o.empleado.primerNombre}"/></td>
                        <td class="text-end fw-semibold">${pg:dinero(o.total)}</td>
                        <td><span class="badge text-bg-${o.estado.color}">${o.estado.etiqueta}</span></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty pedidos}"><tr><td colspan="8" class="text-center texto-gris py-5">No hay pedidos con este filtro.</td></tr></c:if>
                </tbody>
            </table>
        </div>
    </div>
</t:admin>
