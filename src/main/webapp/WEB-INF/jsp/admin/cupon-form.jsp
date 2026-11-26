<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="${empty cuponForm.id ? 'Nuevo cupón' : 'Editar cupón'}">
    <a href="${ctx}/admin/promociones#cupones" class="small"><i class="bi bi-arrow-left me-1"></i>Promociones y cupones</a>
    <form:form modelAttribute="cuponForm" action="${ctx}/admin/cupones" method="post" novalidate="novalidate" cssClass="mt-3">
        <input type="hidden" name="_csrf" value="${csrfToken}">
        <form:hidden path="id"/>
        <div class="row g-4">
            <div class="col-xl-8">
                <div class="tarjeta p-4">
                    <div class="row g-3">
                        <div class="col-md-8">
                            <label for="nombre" class="form-label">Nombre</label>
                            <form:input path="nombre" id="nombre" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="nombre" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-md-4">
                            <label for="codigo" class="form-label">Código</label>
                            <form:input path="codigo" id="codigo" cssClass="form-control text-uppercase" cssErrorClass="form-control text-uppercase is-invalid" placeholder="PRIMAVERA20"/>
                            <form:errors path="codigo" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-12">
                            <label for="descripcion" class="form-label">Descripción</label>
                            <form:input path="descripcion" id="descripcion" cssClass="form-control"/>
                        </div>
                        <div class="col-md-4">
                            <label for="tipoDescuento" class="form-label">Tipo de descuento</label>
                            <form:select path="tipoDescuento" id="tipoDescuento" cssClass="form-select">
                                <form:option value="PORCENTAJE" label="Porcentaje"/>
                                <form:option value="MONTO" label="Monto fijo"/>
                            </form:select>
                        </div>
                        <div class="col-md-4">
                            <label for="valor" class="form-label">Valor (% o $)</label>
                            <form:input path="valor" id="valor" type="number" step="0.01" min="0" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="valor" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-md-4">
                            <label for="compraMinima" class="form-label">Compra mínima</label>
                            <form:input path="compraMinima" id="compraMinima" type="number" step="1" min="0" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="compraMinima" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-md-4">
                            <label for="inicio" class="form-label">Válido desde</label>
                            <form:input path="inicio" id="inicio" type="date" cssClass="form-control"/>
                        </div>
                        <div class="col-md-4">
                            <label for="vencimiento" class="form-label">Vence</label>
                            <form:input path="vencimiento" id="vencimiento" type="date" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="vencimiento" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-md-4">
                            <label for="limiteDeUsos" class="form-label">Límite de usos</label>
                            <form:input path="limiteDeUsos" id="limiteDeUsos" type="number" min="1" cssClass="form-control" cssErrorClass="form-control is-invalid" placeholder="Sin límite"/>
                            <form:errors path="limiteDeUsos" cssClass="invalid-feedback"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xl-4">
                <div class="tarjeta p-4 mb-4">
                    <c:if test="${not empty cupon}"><p class="small texto-gris">Usado ${cupon.usos} ${cupon.usos == 1 ? 'vez' : 'veces'}.</p></c:if>
                    <div class="form-check form-switch"><form:checkbox path="activo" id="activo" cssClass="form-check-input"/><label for="activo" class="form-check-label">Activo</label></div>
                    <c:if test="${empty cuponForm.id}">
                        <div class="form-check form-switch"><form:checkbox path="notificar" id="notificar" cssClass="form-check-input"/><label for="notificar" class="form-check-label">Enviar por correo a los clientes</label></div>
                    </c:if>
                </div>
                <button class="btn btn-ciruela btn-lg w-100">Guardar</button>
            </div>
        </div>
    </form:form>
</t:admin>
