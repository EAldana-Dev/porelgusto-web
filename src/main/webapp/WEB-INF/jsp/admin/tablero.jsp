<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="Tablero">
    <p class="texto-gris">Hola, <c:out value="${usuarioActual.primerNombre}"/>. ${usuarioActual.admin ? 'Este es el resumen de la tienda.' : 'Estos son tus pedidos asignados.'}</p>
    <div class="row g-3 mb-4">
        <c:if test="${usuarioActual.admin}">
            <div class="col-6 col-xl"><div class="dato-card"><div class="small texto-gris">Ventas totales</div><div class="numero">${pg:dinero(totalVendido)}</div></div></div>
        </c:if>
        <c:if test="${usuarioActual.tienePermiso('ESTADISTICAS')}">
            <div class="col-6 col-xl"><a href="${ctx}/admin/estadisticas?dias=1" class="dato-card d-block text-decoration-none"><div class="small texto-gris">Visitantes hoy</div><div class="numero">${visitantesHoy}</div></a></div>
        </c:if>
        <div class="col-6 col-xl"><a href="${ctx}/admin/pedidos?estado=PENDIENTE" class="dato-card d-block text-decoration-none"><div class="small texto-gris">Por aceptar</div><div class="numero">${pendientes}</div></a></div>
        <div class="col-6 col-xl"><a href="${ctx}/admin/pedidos?estado=PREPARANDO" class="dato-card d-block text-decoration-none"><div class="small texto-gris">En preparación</div><div class="numero">${preparando}</div></a></div>
        <div class="col-6 col-xl"><a href="${ctx}/admin/pedidos?estado=EN_CAMINO" class="dato-card d-block text-decoration-none"><div class="small texto-gris">En camino</div><div class="numero">${enCamino}</div></a></div>
        <div class="col-6 col-xl"><a href="${ctx}/admin/pedidos?estado=ENTREGADO" class="dato-card d-block text-decoration-none"><div class="small texto-gris">Entregados</div><div class="numero">${entregados}</div></a></div>
    </div>

    <div class="row g-4">
        <div class="col-xl-8">
            <div class="tarjeta p-3 mb-4">
                <h2 class="h5 px-2 pt-1"><i class="bi bi-calendar-day me-2 texto-ciruela"></i>Entregas de hoy</h2>
                <c:choose>
                    <c:when test="${empty hoy}"><p class="small texto-gris px-2 mb-1">No hay entregas pendientes para hoy.</p></c:when>
                    <c:otherwise>
                        <div class="list-group list-group-flush">
                            <c:forEach items="${hoy}" var="o">
                                <a href="${ctx}/admin/pedidos/${o.folio}" class="list-group-item list-group-item-action d-flex justify-content-between">
                                    <span><strong>${o.horarioEntrega}</strong> · ${o.folio} · <c:out value="${o.destinatario}"/></span>
                                    <span class="badge text-bg-${o.estado.color}">${o.estado.etiqueta}</span>
                                </a>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="tarjeta p-3">
                <div class="d-flex justify-content-between align-items-center px-2 pt-1 mb-2">
                    <h2 class="h5 mb-0">Pedidos recientes</h2>
                    <a href="${ctx}/admin/pedidos" class="small">Ver todos</a>
                </div>
                <div class="table-responsive">
                    <table class="table tabla-pg align-middle small mb-0">
                        <thead><tr><th>Folio</th><th>Cliente</th><th>Entrega</th><th>Total</th><th>Estado</th></tr></thead>
                        <tbody>
                        <c:forEach items="${recientes}" var="o">
                            <tr>
                                <td><a href="${ctx}/admin/pedidos/${o.folio}" class="fw-semibold">${o.folio}</a></td>
                                <td><c:out value="${o.nombreCliente}"/></td>
                                <td>${pg:fecha(o.fechaEntrega)}<br><span class="texto-gris">${o.horarioEntrega}</span></td>
                                <td>${pg:dinero(o.total)}</td>
                                <td><span class="badge text-bg-${o.estado.color}">${o.estado.etiqueta}</span></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty recientes}"><tr><td colspan="5" class="text-center texto-gris py-4">Sin pedidos.</td></tr></c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-xl-4">
            <c:if test="${usuarioActual.tienePermiso('PRODUCTOS')}">
                <div class="tarjeta p-3 mb-4">
                    <h2 class="h5 px-2 pt-1"><i class="bi bi-exclamation-triangle me-2 text-warning"></i>Inventario bajo</h2>
                    <c:if test="${empty bajoInventario}"><p class="small texto-gris px-2 mb-1">Todo el inventario está bien.</p></c:if>
                    <ul class="list-group list-group-flush small">
                        <c:forEach items="${bajoInventario}" var="p" end="7">
                            <li class="list-group-item d-flex justify-content-between">
                                <a href="${ctx}/admin/productos/${p.idArregloFloral}"><c:out value="${p.nombre}"/></a>
                                <span class="badge ${p.existencias == 0 ? 'text-bg-danger' : 'text-bg-warning'}">${p.existencias}</span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>
            <c:if test="${usuarioActual.tienePermiso('SOPORTE')}">
                <a href="${ctx}/admin/soporte" class="dato-card d-block text-decoration-none">
                    <div class="small texto-gris">Solicitudes de soporte abiertas</div><div class="numero">${solicitudesAbiertas}</div>
                </a>
            </c:if>
        </div>
    </div>
</t:admin>
