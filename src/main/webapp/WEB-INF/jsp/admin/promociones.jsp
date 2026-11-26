<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="Promociones, ofertas, eventos y cupones">
    <div class="d-flex flex-wrap gap-2 mb-3">
        <a href="${ctx}/admin/promociones/nueva?tipo=PROMOCION" class="btn btn-ciruela"><i class="bi bi-plus-lg me-1"></i>Nueva promoción</a>
        <a href="${ctx}/admin/promociones/nueva?tipo=OFERTA" class="btn btn-outline-ciruela">Oferta</a>
        <a href="${ctx}/admin/promociones/nueva?tipo=EVENTO" class="btn btn-outline-ciruela">Evento</a>
        <a href="${ctx}/admin/cupones/nuevo" class="btn btn-outline-ciruela">Cupón</a>
    </div>

    <h2 class="h5 mt-4 mb-2">Promociones, ofertas y eventos <span class="small texto-gris fw-normal">(se aplican solas al precio)</span></h2>
    <div class="tarjeta mb-5">
        <div class="table-responsive">
            <table class="table tabla-pg align-middle small mb-0">
                <thead><tr><th>Nombre</th><th>Tipo</th><th>Descuento</th><th>Aplica a</th><th>Vigencia</th><th>Estado</th><th></th></tr></thead>
                <tbody>
                <c:forEach items="${promociones}" var="p">
                    <tr>
                        <td><strong><c:out value="${p.nombre}"/></strong><br><span class="texto-gris"><c:out value="${p.descripcion}"/></span></td>
                        <td>${p.tipo.etiqueta}</td>
                        <td>${p.descuentoTexto}</td>
                        <td><c:out value="${not empty p.productos ? fn:length(p.productos).toString().concat(' productos') : not empty p.categoria ? p.categoria.nombre : 'Todo el catálogo'}"/></td>
                        <td>${pg:fecha(p.fechaInicio)} –<br>${pg:fecha(p.fechaFin)}</td>
                        <td><span class="badge ${p.vigente ? 'text-bg-success' : not p.activa ? 'text-bg-secondary' : p.fechaFin lt ahora ? 'text-bg-danger' : 'text-bg-info'}">
                            ${p.vigente ? 'Vigente' : not p.activa ? 'Inactiva' : p.fechaFin lt ahora ? 'Vencida' : 'Programada'}</span></td>
                        <td class="text-end text-nowrap">
                            <a href="${ctx}/admin/promociones/${p.idPromocion}" class="btn btn-sm btn-link"><i class="bi bi-pencil"></i></a>
                            <form action="${ctx}/admin/promociones/${p.idPromocion}/eliminar" method="post" class="d-inline" data-confirmar="¿Eliminar «${fn:escapeXml(p.nombre)}»?">
                                <input type="hidden" name="_csrf" value="${csrfToken}">
                                <button class="btn btn-sm btn-link text-danger"><i class="bi bi-trash"></i></button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty promociones}"><tr><td colspan="7" class="text-center texto-gris py-4">No hay promociones.</td></tr></c:if>
                </tbody>
            </table>
        </div>
    </div>

    <h2 class="h5 mb-2" id="cupones">Cupones <span class="small texto-gris fw-normal">(el cliente escribe el código en su carrito)</span></h2>
    <div class="tarjeta">
        <div class="table-responsive">
            <table class="table tabla-pg align-middle small mb-0">
                <thead><tr><th>Nombre</th><th>Código</th><th>Descuento</th><th>Usos</th><th>Vigencia</th><th>Estado</th><th></th></tr></thead>
                <tbody>
                <c:forEach items="${cupones}" var="cu">
                    <tr>
                        <td><strong><c:out value="${cu.nombre}"/></strong><br><span class="texto-gris"><c:out value="${cu.descripcion}"/></span></td>
                        <td class="font-monospace"><c:out value="${cu.codigo}"/></td>
                        <td>${cu.descuentoTexto}<c:if test="${not empty cu.compraMinima}"><br><span class="texto-gris">desde ${pg:dinero(cu.compraMinima)}</span></c:if></td>
                        <td>${cu.usos}<c:if test="${not empty cu.limiteDeUsos}"> / ${cu.limiteDeUsos}</c:if></td>
                        <td><c:if test="${not empty cu.fechaInicio}">${pg:fecha(cu.fechaInicio)} –<br></c:if>${pg:fecha(cu.fechaVencimiento)}</td>
                        <td><span class="badge ${cu.vigente ? 'text-bg-success' : not cu.activo ? 'text-bg-secondary' : cu.vencido ? 'text-bg-danger' : 'text-bg-info'}">
                            ${cu.vigente ? 'Vigente' : not cu.activo ? 'Inactivo' : cu.vencido ? 'Vencido' : 'No disponible'}</span></td>
                        <td class="text-end text-nowrap">
                            <a href="${ctx}/admin/cupones/${cu.idCupon}" class="btn btn-sm btn-link"><i class="bi bi-pencil"></i></a>
                            <form action="${ctx}/admin/cupones/${cu.idCupon}/eliminar" method="post" class="d-inline" data-confirmar="¿Eliminar el cupón ${fn:escapeXml(cu.codigo)}?">
                                <input type="hidden" name="_csrf" value="${csrfToken}">
                                <button class="btn btn-sm btn-link text-danger"><i class="bi bi-trash"></i></button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</t:admin>
