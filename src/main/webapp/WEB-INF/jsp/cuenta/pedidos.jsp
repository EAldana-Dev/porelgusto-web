<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:cuenta titulo="Mis pedidos">
    <c:choose>
        <c:when test="${empty pedidos}">
            <div class="tarjeta p-5 text-center">
                <i class="bi bi-bag display-4 texto-rosa"></i>
                <h2 class="h4 mt-3">No hay pedidos en proceso</h2>
                <p class="texto-gris">Cuando compres, aquí verás el estado de tus pedidos, tus tickets y comprobantes.</p>
                <a href="${ctx}/catalogo" class="btn btn-ciruela px-4">Ver catálogo</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="d-grid gap-3">
                <c:forEach items="${pedidos}" var="o">
                    <a href="${ctx}/pedido/${o.folio}" class="tarjeta p-3 text-decoration-none text-reset d-flex flex-wrap align-items-center gap-3">
                        <div class="d-flex">
                            <c:forEach items="${o.items}" var="i" end="2">
                                <img src="${ctx}${i.arregloFloral.imagenUrl}" alt="" class="rounded-3 border border-white border-2" style="width: 56px; height: 70px; object-fit: cover; margin-right: -14px">
                            </c:forEach>
                        </div>
                        <div class="flex-grow-1 ms-3">
                            <div class="fw-bold">${o.folio}</div>
                            <div class="small texto-gris">${pg:fecha(o.fechaHora)} · ${o.totalPiezas} productos · Entrega ${pg:fecha(o.fechaEntrega)}</div>
                        </div>
                        <span class="badge rounded-pill text-bg-${o.estado.color}">${o.estado.etiqueta}</span>
                        <span class="fw-bold texto-ciruela">${pg:dinero(o.total)}</span>
                        <i class="bi bi-chevron-right texto-gris"></i>
                    </a>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</t:cuenta>
