<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="${empty promocionForm.id ? 'Nueva promoción' : 'Editar promoción'}">
    <a href="${ctx}/admin/promociones" class="small"><i class="bi bi-arrow-left me-1"></i>Promociones</a>
    <form:form modelAttribute="promocionForm" action="${ctx}/admin/promociones" method="post" novalidate="novalidate" cssClass="mt-3">
        <input type="hidden" name="_csrf" value="${csrfToken}">
        <form:hidden path="id"/>
        <div class="row g-4">
            <div class="col-xl-8">
                <div class="tarjeta p-4">
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label for="tipo" class="form-label">Tipo</label>
                            <form:select path="tipo" id="tipo" cssClass="form-select"><c:forEach items="${tipos}" var="tp"><form:option value="${tp}" label="${tp.etiqueta}"/></c:forEach></form:select>
                        </div>
                        <div class="col-md-8">
                            <label for="nombre" class="form-label">Nombre</label>
                            <form:input path="nombre" id="nombre" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="nombre" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-12">
                            <label for="descripcion" class="form-label">Descripción</label>
                            <form:input path="descripcion" id="descripcion" cssClass="form-control"/>
                        </div>
                        <div class="col-md-6">
                            <label for="tipoDescuento" class="form-label">Tipo de descuento</label>
                            <form:select path="tipoDescuento" id="tipoDescuento" cssClass="form-select"><c:forEach items="${tiposDescuento}" var="td"><form:option value="${td}" label="${td.etiqueta}"/></c:forEach></form:select>
                        </div>
                        <div class="col-md-6">
                            <label for="valor" class="form-label">Valor (% o $)</label>
                            <form:input path="valor" id="valor" type="number" step="0.01" min="0" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="valor" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-md-6">
                            <label for="inicio" class="form-label">Inicio</label>
                            <form:input path="inicio" id="inicio" type="datetime-local" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="inicio" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-md-6">
                            <label for="fin" class="form-label">Fin</label>
                            <form:input path="fin" id="fin" type="datetime-local" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="fin" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-12">
                            <label for="condiciones" class="form-label">Condiciones</label>
                            <form:input path="condiciones" id="condiciones" cssClass="form-control"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xl-4">
                <div class="tarjeta p-4 mb-4">
                    <h2 class="h6">¿A qué productos aplica?</h2>
                    <p class="small texto-gris">Las promociones, ofertas y eventos bajan el precio automáticamente mientras están vigentes.</p>
                    <label for="categoriaId" class="form-label small">Categoría o colección</label>
                    <form:select path="categoriaId" id="categoriaId" cssClass="form-select mb-3">
                        <form:option value="" label="Todo el catálogo"/>
                        <c:forEach items="${categorias}" var="cat"><form:option value="${cat.idCategoria}" label="${cat.nombre} (${cat.tipo.etiqueta})"/></c:forEach>
                    </form:select>
                    <label for="productos" class="form-label small">O productos específicos <span class="texto-gris">(Ctrl+clic)</span></label>
                    <form:select path="productos" id="productos" multiple="true" size="8" cssClass="form-select">
                        <c:forEach items="${productos}" var="p"><form:option value="${p.idArregloFloral}" label="${p.sku} · ${p.nombre}"/></c:forEach>
                    </form:select>
                </div>
                <div class="tarjeta p-4 mb-4">
                    <div class="form-check form-switch"><form:checkbox path="activo" id="activo" cssClass="form-check-input"/><label for="activo" class="form-check-label">Activa</label></div>
                    <c:if test="${empty promocionForm.id}">
                        <div class="form-check form-switch"><form:checkbox path="notificar" id="notificar" cssClass="form-check-input"/><label for="notificar" class="form-check-label">Enviar por correo a los clientes</label></div>
                    </c:if>
                </div>
                <button class="btn btn-ciruela btn-lg w-100">Guardar</button>
            </div>
        </div>
    </form:form>
</t:admin>
