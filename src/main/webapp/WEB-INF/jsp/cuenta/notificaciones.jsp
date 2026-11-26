<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:cuenta titulo="Notificaciones">
    <c:if test="${notifPendientes > 0}">
        <form action="${ctx}/cuenta/notificaciones/leidas" method="post" class="text-end mb-2">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button class="btn btn-link btn-sm">Marcar todas como leídas</button>
        </form>
    </c:if>
    <c:choose>
        <c:when test="${empty notificaciones}">
            <div class="tarjeta p-5 text-center texto-gris"><i class="bi bi-bell display-5 texto-rosa"></i><p class="mt-3 mb-0">No tienes notificaciones.</p></div>
        </c:when>
        <c:otherwise>
            <div class="list-group">
                <c:forEach items="${notificaciones}" var="n">
                    <a href="${ctx}/cuenta/notificaciones/${n.idNotificacion}" class="list-group-item list-group-item-action d-flex gap-3 py-3 ${n.leida ? '' : 'bg-rosa-suave'}">
                        <i class="bi ${n.leida ? 'bi-bell' : 'bi-bell-fill texto-ciruela'} fs-5"></i>
                        <div class="flex-grow-1">
                            <div class="${n.leida ? '' : 'fw-bold'}"><c:out value="${n.titulo}"/></div>
                            <div class="small texto-gris"><c:out value="${n.cuerpo}"/></div>
                        </div>
                        <span class="small texto-gris text-nowrap">${pg:hace(n.fechaHora)}</span>
                    </a>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</t:cuenta>
