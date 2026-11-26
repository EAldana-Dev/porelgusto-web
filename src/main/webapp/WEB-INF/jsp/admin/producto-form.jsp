<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="${empty producto ? 'Nuevo producto' : 'Editar '.concat(producto.sku)}">
    <a href="${ctx}/admin/productos" class="small"><i class="bi bi-arrow-left me-1"></i>Productos</a>
    <%-- Formulario con archivo: el token CSRF va en la URL porque el cuerpo es multipart --%>
    <form:form modelAttribute="productoForm" action="${ctx}/admin/productos?_csrf=${csrfToken}" method="post" enctype="multipart/form-data" novalidate="novalidate" cssClass="mt-3">
        <form:hidden path="id"/>
        <div class="row g-4">
            <div class="col-xl-8">
                <div class="tarjeta p-4 mb-4">
                    <div class="row g-3">
                        <div class="col-md-8">
                            <label for="nombre" class="form-label">Nombre</label>
                            <form:input path="nombre" id="nombre" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="nombre" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-md-4">
                            <label for="precio" class="form-label">Precio (MXN, con IVA)</label>
                            <form:input path="precio" id="precio" type="number" step="0.01" min="1" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="precio" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-12">
                            <label for="descripcion" class="form-label">Descripción</label>
                            <form:textarea path="descripcion" id="descripcion" rows="3" maxlength="255" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="descripcion" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-md-6">
                            <label for="categoria" class="form-label">Categoría</label>
                            <form:select path="categoria" id="categoria" cssClass="form-select" cssErrorClass="form-select is-invalid">
                                <form:option value="" label="Elige una categoría"/>
                                <form:options items="${categorias}" itemValue="nombre" itemLabel="nombre"/>
                            </form:select>
                            <form:errors path="categoria" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-md-6">
                            <label for="etiquetas" class="form-label">Flores y etiquetas <span class="texto-gris fw-normal">(separadas por comas)</span></label>
                            <form:input path="etiquetas" id="etiquetas" maxlength="100" cssClass="form-control" cssErrorClass="form-control is-invalid" placeholder="Rosas, Eucalipto"/>
                            <form:errors path="etiquetas" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-12">
                            <span class="form-label d-block">Colecciones, grupos y temporadas</span>
                            <div class="d-flex flex-wrap gap-3">
                                <c:forEach items="${colecciones}" var="col">
                                    <div class="form-check">
                                        <form:checkbox path="colecciones" value="${col.idCategoria}" id="col${col.idCategoria}" cssClass="form-check-input"/>
                                        <label for="col${col.idCategoria}" class="form-check-label"><c:out value="${col.nombre}"/> <span class="small texto-gris">(${col.tipo.etiqueta})</span></label>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="tarjeta p-4">
                    <h2 class="h5 mb-3">Inventario y disponibilidad</h2>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label for="existencias" class="form-label">Existencias</label>
                            <form:input path="existencias" id="existencias" type="number" min="0" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="existencias" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-md-4">
                            <label for="puntoReorden" class="form-label">Avisar cuando queden</label>
                            <form:input path="puntoReorden" id="puntoReorden" type="number" min="0" cssClass="form-control"/>
                        </div>
                        <div class="col-md-4 d-flex flex-column justify-content-end gap-1">
                            <div class="form-check form-switch"><form:checkbox path="activo" id="activo" cssClass="form-check-input"/><label for="activo" class="form-check-label">Visible en el catálogo</label></div>
                            <div class="form-check form-switch"><form:checkbox path="personalizable" id="personalizable" cssClass="form-check-input"/><label for="personalizable" class="form-check-label">Permite personalización</label></div>
                            <div class="form-check form-switch"><form:checkbox path="permiteQR" id="permiteQR" cssClass="form-check-input"/><label for="permiteQR" class="form-check-label">Permite dedicatoria QR</label></div>
                            <div class="form-check form-switch"><form:checkbox path="prueba" id="prueba" cssClass="form-check-input"/><label for="prueba" class="form-check-label">Producto de prueba <span class="small texto-gris">(oculto, sin envío)</span></label></div>
                        </div>
                    </div>
                    <p class="small texto-gris mt-3 mb-0">Si el producto estaba agotado y le pones existencias, avisamos por correo a quienes lo tienen en su lista de deseos.</p>
                </div>
            </div>
            <div class="col-xl-4">
                <div class="tarjeta p-4 mb-4">
                    <h2 class="h5 mb-3">Imagen</h2>
                    <c:if test="${not empty producto}"><img src="${ctx}${producto.imagenUrl}" alt="" class="w-100 rounded-3 mb-3" style="aspect-ratio: 3/4; object-fit: cover"></c:if>
                    <input type="file" name="imagen" accept="image/jpeg,image/png,image/webp" class="form-control" ${empty producto ? 'required' : ''} aria-label="Imagen">
                    <div class="form-text">JPG, PNG o WEBP, máximo 5 MB. ${empty producto ? '' : 'Déjalo vacío para conservar la actual.'}</div>
                </div>
                <button class="btn btn-ciruela btn-lg w-100">Guardar producto</button>
                <c:if test="${not empty producto}"><a href="${ctx}/producto/${producto.sku}" target="_blank" class="btn btn-link w-100 mt-2">Ver en la tienda</a></c:if>
            </div>
        </div>
    </form:form>
</t:admin>
