<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:cuenta titulo="Mi cuenta">
    <section class="tarjeta p-4 mb-4">
        <h2 class="h4 mb-3">Datos personales</h2>
        <form:form modelAttribute="perfilForm" action="${ctx}/cuenta" method="post" novalidate="novalidate">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <div class="row g-3">
                <div class="col-md-6">
                    <label for="nombre" class="form-label">Nombre completo</label>
                    <form:input path="nombre" id="nombre" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                    <form:errors path="nombre" cssClass="invalid-feedback"/>
                </div>
                <div class="col-md-6">
                    <label for="correo" class="form-label">Correo</label>
                    <form:input path="correo" id="correo" type="email" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                    <form:errors path="correo" cssClass="invalid-feedback"/>
                </div>
                <div class="col-md-4">
                    <label for="telefono" class="form-label">Teléfono</label>
                    <form:input path="telefono" id="telefono" type="tel" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                    <form:errors path="telefono" cssClass="invalid-feedback"/>
                </div>
                <div class="col-md-4">
                    <label for="fechaNacimiento" class="form-label">Nacimiento</label>
                    <form:input path="fechaNacimiento" id="fechaNacimiento" type="date" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                    <form:errors path="fechaNacimiento" cssClass="invalid-feedback"/>
                </div>
                <div class="col-12">
                    <label for="direccion" class="form-label">Dirección <span class="texto-gris fw-normal">(opcional)</span></label>
                    <form:input path="direccion" id="direccion" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                    <form:errors path="direccion" cssClass="invalid-feedback"/>
                </div>
            </div>
            <button class="btn btn-ciruela mt-4 px-4">Guardar cambios</button>
        </form:form>
    </section>

    <section class="tarjeta p-4 mb-4" id="password">
        <h2 class="h4 mb-3">Cambiar contraseña</h2>
        <form:form modelAttribute="passwordForm" action="${ctx}/cuenta/password" method="post" novalidate="novalidate">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <div class="row g-3">
                <div class="col-md-4">
                    <label for="actual" class="form-label">Contraseña actual</label>
                    <form:password path="actual" id="actual" cssClass="form-control" autocomplete="current-password"/>
                </div>
                <div class="col-md-4">
                    <label for="nueva" class="form-label">Nueva</label>
                    <form:password path="nueva" id="nueva" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="new-password"/>
                    <form:errors path="nueva" cssClass="invalid-feedback"/>
                </div>
                <div class="col-md-4">
                    <label for="confirmacion" class="form-label">Confirmar</label>
                    <form:password path="confirmacion" id="confirmacion" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="new-password"/>
                    <form:errors path="confirmacion" cssClass="invalid-feedback"/>
                    <form:errors path="passwordsIguales" cssClass="text-danger small d-block mt-1"/>
                </div>
            </div>
            <button class="btn btn-outline-ciruela mt-4 px-4">Actualizar contraseña</button>
        </form:form>
    </section>

    <section class="tarjeta p-4" id="direcciones">
        <h2 class="h4 mb-1">Direcciones guardadas</h2>
        <p class="small texto-gris">Se agregan al pagar marcando «Guardar esta dirección».</p>
        <c:if test="${empty direcciones}"><p class="texto-gris mb-0">Aún no tienes direcciones guardadas.</p></c:if>
        <div class="row g-3">
            <c:forEach items="${direcciones}" var="d">
                <div class="col-md-6">
                    <div class="border borde-pg rounded-3 p-3 h-100 d-flex justify-content-between gap-2">
                        <div class="small">
                            <div class="fw-semibold"><i class="bi bi-geo-alt texto-ciruela me-1"></i><c:out value="${d.alias}"/></div>
                            <c:out value="${d.formatoEtiqueta()}"/>
                            <c:if test="${not empty d.telefono}"><br><span class="texto-gris"><c:out value="${d.telefono}"/></span></c:if>
                        </div>
                        <form action="${ctx}/cuenta/direcciones/${d.idContacto}/eliminar" method="post" data-confirmar="¿Eliminar esta dirección?">
                            <input type="hidden" name="_csrf" value="${csrfToken}">
                            <button class="btn btn-link btn-sm text-danger p-0" aria-label="Eliminar"><i class="bi bi-trash"></i></button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
    </section>
</t:cuenta>
