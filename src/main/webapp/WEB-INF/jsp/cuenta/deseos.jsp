<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:cuenta titulo="Mi lista de deseos">
    <c:choose>
        <c:when test="${empty deseos}">
            <div class="tarjeta p-5 text-center">
                <i class="bi bi-heart display-4 texto-rosa"></i>
                <h2 class="h4 mt-3">No hay productos agregados</h2>
                <p class="texto-gris">Guarda los arreglos que te gustan con el corazón ♡. Si no están disponibles, te avisamos cuando regresen.</p>
                <a href="${ctx}/" class="btn btn-ciruela px-4">Ir a la página principal</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="d-grid gap-3">
                <c:forEach items="${deseos}" var="v">
                    <c:set var="p" value="${v.producto}"/>
                    <div class="tarjeta p-3 d-flex flex-wrap align-items-center gap-3">
                        <a href="${ctx}/producto/${p.sku}"><img src="${ctx}${p.imagenUrl}" alt="" class="rounded-3" style="width: 70px; height: 88px; object-fit: cover"></a>
                        <div class="flex-grow-1">
                            <a href="${ctx}/producto/${p.sku}" class="titulo-serif fs-5 text-reset text-decoration-none"><c:out value="${p.nombre}"/></a>
                            <div class="small">
                                <span class="fw-bold">${pg:dinero(v.precioFinal)}</span>
                                <c:choose>
                                    <c:when test="${p.disponible}"><span class="ms-2" style="color: var(--pg-exito)"><i class="bi bi-check-circle-fill"></i> Disponible</span></c:when>
                                    <c:otherwise><span class="ms-2 text-danger"><i class="bi bi-hourglass-split"></i> No disponible · te avisaremos</span></c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <c:if test="${p.disponible}">
                            <form action="${ctx}/cuenta/deseos/${p.sku}/carrito" method="post">
                                <input type="hidden" name="_csrf" value="${csrfToken}">
                                <button class="btn btn-ciruela btn-sm"><i class="bi bi-bag-plus me-1"></i>Agregar al carrito</button>
                            </form>
                        </c:if>
                        <form action="${ctx}/deseos/${p.sku}" method="post">
                            <input type="hidden" name="_csrf" value="${csrfToken}">
                            <input type="hidden" name="accion" value="quitar">
                            <button class="btn btn-link btn-sm text-danger"><i class="bi bi-trash me-1"></i>Eliminar</button>
                        </form>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</t:cuenta>
