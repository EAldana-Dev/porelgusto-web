<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="Productos e inventario">
    <div class="d-flex flex-wrap justify-content-between gap-2 mb-3">
        <form class="d-flex gap-2" method="get">
            <input name="q" class="form-control" placeholder="Buscar por nombre o SKU" value="<c:out value='${q}'/>" aria-label="Buscar">
            <button class="btn btn-outline-ciruela btn-cuadrado"><i class="bi bi-search"></i></button>
        </form>
        <a href="${ctx}/admin/productos/nuevo" class="btn btn-ciruela"><i class="bi bi-plus-lg me-1"></i>Nuevo producto</a>
    </div>
    <div class="tarjeta">
        <div class="table-responsive">
            <table class="table tabla-pg align-middle small mb-0">
                <thead><tr><th></th><th>SKU</th><th>Producto</th><th>Categoría</th><th class="text-end">Precio</th><th class="text-center">Existencias</th><th class="text-center">Vendidos</th><th>Estado</th><th></th></tr></thead>
                <tbody>
                <c:forEach items="${productos}" var="p">
                    <tr class="${p.activo ? '' : 'opacity-50'}">
                        <td><img src="${ctx}${p.imagenUrl}" alt="" class="mini"></td>
                        <td class="font-monospace">${p.sku}</td>
                        <td><a href="${ctx}/admin/productos/${p.idArregloFloral}" class="fw-semibold"><c:out value="${p.nombre}"/></a></td>
                        <td><c:out value="${p.categoria}"/></td>
                        <td class="text-end">${pg:dinero(p.costo)}</td>
                        <td class="text-center"><span class="badge ${p.existencias == 0 ? 'text-bg-danger' : p.bajoInventario ? 'text-bg-warning' : 'text-bg-light'}">${p.existencias}</span></td>
                        <td class="text-center">${p.ventas}</td>
                        <td>${not p.activo ? 'Inactivo' : p.disponible ? 'Disponible' : 'No disponible'}<c:if test="${p.prueba}"><br><span class="badge text-bg-warning">Prueba · oculto</span></c:if></td>
                        <td class="text-end text-nowrap">
                            <a href="${ctx}/producto/${p.sku}" target="_blank" class="btn btn-sm btn-link" title="Ver en tienda"><i class="bi bi-eye"></i></a>
                            <a href="${ctx}/admin/productos/${p.idArregloFloral}" class="btn btn-sm btn-link" title="Editar"><i class="bi bi-pencil"></i></a>
                            <form action="${ctx}/admin/productos/${p.idArregloFloral}/eliminar" method="post" class="d-inline" data-confirmar="¿Eliminar «${fn:escapeXml(p.nombre)}» del catálogo?">
                                <input type="hidden" name="_csrf" value="${csrfToken}">
                                <button class="btn btn-sm btn-link text-danger" title="Eliminar"><i class="bi bi-trash"></i></button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty productos}"><tr><td colspan="9" class="text-center texto-gris py-5">No hay productos con esa búsqueda.</td></tr></c:if>
                </tbody>
            </table>
        </div>
    </div>
</t:admin>
