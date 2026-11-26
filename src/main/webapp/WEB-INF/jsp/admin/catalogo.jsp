<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="Configuración del catálogo">
    <ul class="nav nav-pills mb-4 gap-2">
        <c:forEach items="${tipos}" var="tp">
            <li class="nav-item"><a class="chip-cat ${tipo == tp.name() ? 'activo' : ''}" href="${ctx}/admin/catalogo?tipo=${tp}">${tp == 'CATEGORIA' ? 'Categorías' : tp == 'GRUPO' ? 'Grupos' : 'Temporadas y eventos'}</a></li>
        </c:forEach>
        <li class="nav-item"><a class="chip-cat ${tipo == 'OPCION' ? 'activo' : ''}" href="${ctx}/admin/catalogo?tipo=OPCION">Opciones de personalización</a></li>
    </ul>

    <div class="row g-4">
        <c:choose>
        <c:when test="${tipo == 'OPCION'}">
            <div class="col-xl-7">
                <div class="tarjeta">
                    <table class="table tabla-pg align-middle small mb-0">
                        <thead><tr><th>Opción</th><th>Aplica a</th><th class="text-end">Costo adicional</th><th>Estado</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach items="${opciones}" var="o">
                            <tr>
                                <td><strong><c:out value="${o.nombre}"/></strong><br><span class="texto-gris"><c:out value="${o.descripcion}"/></span></td>
                                <td><c:out value="${o.general ? 'Todos los arreglos' : o.arregloFloral.nombre}"/></td>
                                <td class="text-end">${pg:dinero(o.costo)}</td>
                                <td><span class="badge ${o.disponible ? 'text-bg-success' : 'text-bg-secondary'}">${o.disponible ? 'Disponible' : 'No disponible'}</span></td>
                                <td class="text-end text-nowrap">
                                    <a href="${ctx}/admin/catalogo?tipo=OPCION&editar=${o.idModificacion}" class="btn btn-sm btn-link"><i class="bi bi-pencil"></i></a>
                                    <form action="${ctx}/admin/catalogo/opciones/${o.idModificacion}/eliminar" method="post" class="d-inline" data-confirmar="¿Eliminar esta opción?">
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
            <div class="col-xl-5">
                <div class="tarjeta p-4">
                    <h2 class="h5 mb-3">${empty opcionForm.id ? 'Nueva opción' : 'Editar opción'}</h2>
                    <form:form modelAttribute="opcionForm" action="${ctx}/admin/catalogo/opciones" method="post" novalidate="novalidate">
                        <input type="hidden" name="_csrf" value="${csrfToken}">
                        <form:hidden path="id"/>
                        <div class="mb-3"><label for="nombre" class="form-label">Nombre</label>
                            <form:input path="nombre" id="nombre" cssClass="form-control" cssErrorClass="form-control is-invalid"/><form:errors path="nombre" cssClass="invalid-feedback"/></div>
                        <div class="mb-3"><label for="descripcion" class="form-label">Descripción</label><form:input path="descripcion" id="descripcion" cssClass="form-control"/></div>
                        <div class="row g-3 mb-3">
                            <div class="col-4"><label for="costo" class="form-label">Costo ($)</label>
                                <form:input path="costo" id="costo" type="number" step="1" min="0" cssClass="form-control" cssErrorClass="form-control is-invalid"/><form:errors path="costo" cssClass="invalid-feedback"/></div>
                            <div class="col-4"><label for="tipoOpcion" class="form-label">Tipo</label>
                                <form:select path="tipo" id="tipoOpcion" cssClass="form-select"><form:option value="Extra"/><form:option value="Detalle"/><form:option value="Tamaño"/></form:select></div>
                            <div class="col-4"><label for="orden" class="form-label">Orden</label><form:input path="orden" id="orden" type="number" cssClass="form-control"/></div>
                        </div>
                        <div class="form-check form-switch mb-3"><form:checkbox path="disponible" id="disponible" cssClass="form-check-input"/><label for="disponible" class="form-check-label">Disponible</label></div>
                        <button class="btn btn-ciruela w-100">Guardar</button>
                        <c:if test="${not empty opcionForm.id}"><a href="${ctx}/admin/catalogo?tipo=OPCION" class="btn btn-link w-100">Cancelar</a></c:if>
                    </form:form>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="col-xl-7">
                <div class="tarjeta">
                    <table class="table tabla-pg align-middle small mb-0">
                        <thead><tr><th>Nombre</th><th>Vigencia</th><th>Orden</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach items="${elementos}" var="el">
                            <tr>
                                <td><strong><c:out value="${el.nombre}"/></strong><br><span class="texto-gris"><c:out value="${el.descripcion}"/></span></td>
                                <td>${empty el.fechaInicio and empty el.fechaFin ? 'Siempre' : pg:fecha(el.fechaInicio).concat(' – ').concat(pg:fecha(el.fechaFin))}
                                    <c:if test="${not el.vigente}"><span class="badge text-bg-secondary">No vigente</span></c:if></td>
                                <td>${el.orden}</td>
                                <td class="text-end text-nowrap">
                                    <a href="${ctx}/catalogo?${el.tipo == 'CATEGORIA' ? 'categoria' : 'coleccion'}=${el.slug}" target="_blank" class="btn btn-sm btn-link"><i class="bi bi-eye"></i></a>
                                    <a href="${ctx}/admin/catalogo?tipo=${tipo}&editar=${el.idCategoria}" class="btn btn-sm btn-link"><i class="bi bi-pencil"></i></a>
                                    <form action="${ctx}/admin/catalogo/categorias/${el.idCategoria}/eliminar" method="post" class="d-inline" data-confirmar="¿Eliminar «${fn:escapeXml(el.nombre)}»?">
                                        <input type="hidden" name="_csrf" value="${csrfToken}">
                                        <input type="hidden" name="tipo" value="${tipo}">
                                        <button class="btn btn-sm btn-link text-danger"><i class="bi bi-trash"></i></button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty elementos}"><tr><td colspan="4" class="text-center texto-gris py-4">No hay elementos.</td></tr></c:if>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="col-xl-5">
                <div class="tarjeta p-4">
                    <h2 class="h5 mb-3">${empty categoriaForm.id ? 'Nuevo elemento' : 'Editar elemento'}</h2>
                    <form:form modelAttribute="categoriaForm" action="${ctx}/admin/catalogo/categorias" method="post" novalidate="novalidate">
                        <input type="hidden" name="_csrf" value="${csrfToken}">
                        <form:hidden path="id"/>
                        <form:hidden path="tipo"/>
                        <div class="mb-3"><label for="nombre" class="form-label">Nombre</label>
                            <form:input path="nombre" id="nombre" cssClass="form-control" cssErrorClass="form-control is-invalid"/><form:errors path="nombre" cssClass="invalid-feedback"/></div>
                        <div class="mb-3"><label for="descripcion" class="form-label">Descripción</label><form:input path="descripcion" id="descripcion" cssClass="form-control"/></div>
                        <c:if test="${tipo != 'CATEGORIA'}">
                            <div class="row g-3 mb-3">
                                <div class="col-6"><label for="inicio" class="form-label">Inicio</label><form:input path="inicio" id="inicio" type="date" cssClass="form-control"/></div>
                                <div class="col-6"><label for="fin" class="form-label">Fin</label><form:input path="fin" id="fin" type="date" cssClass="form-control"/></div>
                            </div>
                        </c:if>
                        <div class="mb-3"><label for="orden" class="form-label">Orden</label><form:input path="orden" id="orden" type="number" cssClass="form-control"/></div>
                        <button class="btn btn-ciruela w-100">Guardar</button>
                        <c:if test="${not empty categoriaForm.id}"><a href="${ctx}/admin/catalogo?tipo=${tipo}" class="btn btn-link w-100">Cancelar</a></c:if>
                    </form:form>
                </div>
            </div>
        </c:otherwise>
        </c:choose>
    </div>
</t:admin>
