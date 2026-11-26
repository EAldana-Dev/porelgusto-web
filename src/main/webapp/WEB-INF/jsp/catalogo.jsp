<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="tituloCatalogo" value="${not empty categoriaActual ? categoriaActual.nombre : not empty coleccionActual ? coleccionActual.nombre : filtro.ofertas ? 'Ofertas' : 'Catálogo'}"/>
<t:pagina titulo="${tituloCatalogo}">
<div class="container py-4">

    <!-- Encabezado -->
    <div class="d-flex flex-wrap align-items-end justify-content-between gap-3 mb-4">
        <div>
            <nav aria-label="Ruta"><ol class="breadcrumb small mb-1">
                <li class="breadcrumb-item"><a href="${ctx}/">Inicio</a></li>
                <li class="breadcrumb-item ${empty categoriaActual and empty coleccionActual ? 'active' : ''}"><a href="${ctx}/catalogo">Catálogo</a></li>
                <c:if test="${not empty categoriaActual or not empty coleccionActual}"><li class="breadcrumb-item active"><c:out value="${tituloCatalogo}"/></li></c:if>
            </ol></nav>
            <h1 class="display-5 mb-0"><c:out value="${tituloCatalogo}"/></h1>
            <c:if test="${not empty filtro.q}"><p class="texto-gris mb-0">Resultados para «<c:out value="${filtro.q}"/>»</p></c:if>
            <c:if test="${not empty categoriaActual.descripcion}"><p class="texto-gris mb-0"><c:out value="${categoriaActual.descripcion}"/></p></c:if>
            <c:if test="${not empty coleccionActual.descripcion}"><p class="texto-gris mb-0"><c:out value="${coleccionActual.descripcion}"/></p></c:if>
        </div>
        <div class="d-flex align-items-center gap-2">
            <span class="small texto-gris">${fn:length(resultados)} productos</span>
            <button class="btn btn-outline-ciruela btn-sm d-lg-none" data-bs-toggle="offcanvas" data-bs-target="#panelFiltros"><i class="bi bi-sliders me-1"></i>Filtros</button>
        </div>
    </div>

    <!-- Chips de categorías y colecciones -->
    <div class="d-flex flex-wrap gap-2 mb-4">
        <a href="${ctx}/catalogo" class="chip-cat ${empty filtro.categoria and empty filtro.coleccion and not filtro.ofertas ? 'activo' : ''}">Todos</a>
        <c:forEach items="${categorias}" var="cat">
            <a href="${ctx}/catalogo?categoria=${cat.slug}" class="chip-cat ${filtro.categoria == cat.slug ? 'activo' : ''}"><c:out value="${cat.nombre}"/></a>
        </c:forEach>
        <c:forEach items="${colecciones}" var="col">
            <a href="${ctx}/catalogo?coleccion=${col.slug}" class="chip-cat ${filtro.coleccion == col.slug ? 'activo' : ''}"><i class="bi bi-stars me-1"></i><c:out value="${col.nombre}"/></a>
        </c:forEach>
    </div>

    <div class="row g-4">
        <!-- ==================== PANEL DE FILTROS ==================== -->
        <div class="col-lg-3">
            <div class="offcanvas-lg offcanvas-start" tabindex="-1" id="panelFiltros" aria-labelledby="tituloFiltros">
                <div class="offcanvas-header">
                    <h2 class="offcanvas-title h4" id="tituloFiltros">Filtros</h2>
                    <button type="button" class="btn-close" data-bs-dismiss="offcanvas" data-bs-target="#panelFiltros" aria-label="Cerrar"></button>
                </div>
                <div class="offcanvas-body">
                    <form action="${ctx}/catalogo" method="get" class="filtros tarjeta p-3 w-100">
                        <c:if test="${not empty filtro.q}"><input type="hidden" name="q" value="${fn:escapeXml(filtro.q)}"></c:if>

                        <h6>Ordenar por</h6>
                        <select name="orden" class="form-select form-select-sm mb-4" aria-label="Ordenar por">
                            <option value="relevancia" ${filtro.orden == 'relevancia' ? 'selected' : ''}>Destacados</option>
                            <option value="populares" ${filtro.orden == 'populares' ? 'selected' : ''}>Más populares</option>
                            <option value="novedades" ${filtro.orden == 'novedades' ? 'selected' : ''}>Novedades</option>
                            <option value="precio-asc" ${filtro.orden == 'precio-asc' ? 'selected' : ''}>Precio: menor a mayor</option>
                            <option value="precio-desc" ${filtro.orden == 'precio-desc' ? 'selected' : ''}>Precio: mayor a menor</option>
                            <option value="nombre" ${filtro.orden == 'nombre' ? 'selected' : ''}>Nombre (A-Z)</option>
                        </select>

                        <h6>Categoría</h6>
                        <select name="categoria" class="form-select form-select-sm mb-4" aria-label="Categoría">
                            <option value="">Todas</option>
                            <c:forEach items="${categorias}" var="cat">
                                <option value="${cat.slug}" ${filtro.categoria == cat.slug ? 'selected' : ''}><c:out value="${cat.nombre}"/></option>
                            </c:forEach>
                        </select>

                        <h6>Colección o temporada</h6>
                        <select name="coleccion" class="form-select form-select-sm mb-4" aria-label="Colección">
                            <option value="">Todas</option>
                            <c:forEach items="${colecciones}" var="col">
                                <option value="${col.slug}" ${filtro.coleccion == col.slug ? 'selected' : ''}><c:out value="${col.nombre}"/> (<c:out value="${col.tipo.etiqueta}"/>)</option>
                            </c:forEach>
                        </select>

                        <h6>Rango de precio</h6>
                        <div class="d-flex gap-2 mb-4">
                            <input type="number" name="precioMin" min="0" step="50" class="form-control form-control-sm" placeholder="Mín" value="${filtro.precioMin}" aria-label="Precio mínimo">
                            <input type="number" name="precioMax" min="0" step="50" class="form-control form-control-sm" placeholder="Máx" value="${filtro.precioMax}" aria-label="Precio máximo">
                        </div>

                        <h6>Flores y etiquetas</h6>
                        <div class="lista-etiquetas mb-4">
                            <c:forEach items="${etiquetas}" var="et" varStatus="s">
                                <div class="form-check small">
                                    <input class="form-check-input" type="checkbox" name="etiqueta" value="${fn:escapeXml(et)}" id="et${s.index}" ${pg:contiene(filtro.etiqueta, et) ? 'checked' : ''}>
                                    <label class="form-check-label" for="et${s.index}"><c:out value="${et}"/></label>
                                </div>
                            </c:forEach>
                        </div>

                        <h6>Disponibilidad</h6>
                        <div class="form-check form-switch small">
                            <input class="form-check-input" type="checkbox" name="disponibles" value="true" id="disp" ${filtro.disponibles ? 'checked' : ''}>
                            <label class="form-check-label" for="disp">Solo disponibles</label>
                        </div>
                        <div class="form-check form-switch small mb-4">
                            <input class="form-check-input" type="checkbox" name="ofertas" value="true" id="ofe" ${filtro.ofertas ? 'checked' : ''}>
                            <label class="form-check-label" for="ofe">Solo con descuento</label>
                        </div>

                        <button class="btn btn-ciruela w-100">Aplicar</button>
                        <c:if test="${filtro.activo}"><a href="${ctx}/catalogo" class="btn btn-link btn-sm w-100 mt-1">Limpiar filtros</a></c:if>
                    </form>
                </div>
            </div>
        </div>

        <!-- ==================== RESULTADOS ==================== -->
        <div class="col-lg-9">
            <c:choose>
                <c:when test="${empty resultados}">
                    <div class="tarjeta text-center p-5">
                        <i class="bi bi-flower3 display-4 texto-rosa"></i>
                        <h2 class="mt-3">No encontramos productos</h2>
                        <p class="texto-gris">Ningún producto cumple con los filtros elegidos. Prueba limpiarlos o buscar otra cosa.</p>
                        <a href="${ctx}/catalogo" class="btn btn-ciruela px-4">Limpiar filtros</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row row-cols-2 row-cols-md-3 g-3 g-lg-4">
                        <c:forEach items="${resultados}" var="v">
                            <div class="col"><t:producto v="${v}" enDeseos="${pg:contiene(deseos, v.producto.sku)}"/></div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</t:pagina>
