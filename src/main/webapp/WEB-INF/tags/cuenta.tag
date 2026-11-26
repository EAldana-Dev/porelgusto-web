<%@ tag description="Plantilla de Mi cuenta con menú lateral" pageEncoding="UTF-8" %>
<%@ attribute name="titulo" required="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="${titulo}">
<div class="container py-4">
    <div class="row g-4">
        <aside class="col-lg-3">
            <div class="tarjeta p-3 mb-3 text-center">
                <div class="rounded-circle bg-rosa d-inline-flex align-items-center justify-content-center mb-2 titulo-serif fs-3 texto-ciruela" style="width: 64px; height: 64px">
                    <c:out value="${usuarioActual.iniciales}"/>
                </div>
                <div class="fw-semibold"><c:out value="${usuarioActual.nombre}"/></div>
                <div class="small texto-gris"><c:out value="${usuarioActual.correoElectronico}"/></div>
            </div>
            <div class="list-group menu-cuenta">
                <a href="${ctx}/cuenta" class="list-group-item list-group-item-action ${seccion == 'perfil' ? 'active' : ''}"><i class="bi bi-person-gear me-2"></i>Mi cuenta</a>
                <a href="${ctx}/cuenta/pedidos" class="list-group-item list-group-item-action ${seccion == 'pedidos' ? 'active' : ''}"><i class="bi bi-bag-check me-2"></i>Mis pedidos</a>
                <a href="${ctx}/cuenta/deseos" class="list-group-item list-group-item-action ${seccion == 'deseos' ? 'active' : ''}"><i class="bi bi-heart me-2"></i>Lista de deseos</a>
                <a href="${ctx}/cuenta/notificaciones" class="list-group-item list-group-item-action ${seccion == 'notificaciones' ? 'active' : ''}"><i class="bi bi-bell me-2"></i>Notificaciones
                    <c:if test="${notifPendientes > 0}"><span class="badge rounded-pill text-bg-light float-end">${notifPendientes}</span></c:if></a>
                <a href="${ctx}/soporte" class="list-group-item list-group-item-action"><i class="bi bi-life-preserver me-2"></i>Soporte</a>
                <c:if test="${usuarioActual.personal}"><a href="${ctx}/admin" class="list-group-item list-group-item-action fw-semibold"><i class="bi bi-speedometer2 me-2"></i>Panel de administración</a></c:if>
            </div>
        </aside>
        <div class="col-lg-9">
            <h1 class="display-6 mb-4"><c:out value="${titulo}"/></h1>
            <jsp:doBody/>
        </div>
    </div>
</div>
</t:pagina>
