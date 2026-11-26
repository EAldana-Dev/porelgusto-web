<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Crear cuenta">
<div class="container py-5" style="max-width: 760px">
    <div class="tarjeta p-4 p-lg-5">
        <h1 class="display-6 mb-1">Crear cuenta</h1>
        <p class="texto-gris mb-4">Con tu cuenta guardas tu lista de deseos, tus direcciones y sigues tus pedidos.</p>
        <form:form modelAttribute="registroForm" action="${ctx}/registro" method="post" novalidate="novalidate">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <div class="row g-3">
                <div class="col-md-6">
                    <label for="nombre" class="form-label">Nombre</label>
                    <form:input path="nombre" id="nombre" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="given-name"/>
                    <form:errors path="nombre" cssClass="invalid-feedback"/>
                </div>
                <div class="col-md-6">
                    <label for="apellido" class="form-label">Apellidos</label>
                    <form:input path="apellido" id="apellido" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="family-name"/>
                    <form:errors path="apellido" cssClass="invalid-feedback"/>
                </div>
                <div class="col-md-7">
                    <label for="correo" class="form-label">Correo electrónico</label>
                    <form:input path="correo" id="correo" type="email" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="email"/>
                    <form:errors path="correo" cssClass="invalid-feedback"/>
                </div>
                <div class="col-md-5">
                    <label for="telefono" class="form-label">Teléfono <span class="texto-gris fw-normal">(opcional)</span></label>
                    <form:input path="telefono" id="telefono" type="tel" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="tel"/>
                    <form:errors path="telefono" cssClass="invalid-feedback"/>
                </div>
                <div class="col-md-5">
                    <label for="fechaNacimiento" class="form-label">Fecha de nacimiento <span class="texto-gris fw-normal">(opcional)</span></label>
                    <form:input path="fechaNacimiento" id="fechaNacimiento" type="date" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                    <form:errors path="fechaNacimiento" cssClass="invalid-feedback"/>
                </div>
                <div class="col-md-6">
                    <label for="password" class="form-label">Contraseña</label>
                    <form:password path="password" id="password" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="new-password"/>
                    <form:errors path="password" cssClass="invalid-feedback"/>
                    <div class="form-text">Mínimo 8 caracteres.</div>
                </div>
                <div class="col-md-6">
                    <label for="confirmacion" class="form-label">Confirmar contraseña</label>
                    <form:password path="confirmacion" id="confirmacion" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="new-password"/>
                    <form:errors path="confirmacion" cssClass="invalid-feedback"/>
                    <form:errors path="passwordsIguales" cssClass="text-danger small d-block mt-1"/>
                </div>
            </div>
            <button class="btn btn-ciruela btn-lg w-100 mt-4">Registrar</button>
        </form:form>
        <p class="text-center small mt-4 mb-0">¿Ya tienes cuenta? <a href="${ctx}/login">Inicia sesión</a></p>
    </div>
</div>
</t:pagina>
