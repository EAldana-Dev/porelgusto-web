<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Restablecer contraseña">
<div class="container py-5" style="max-width: 520px">
    <div class="tarjeta p-4 p-lg-5">
        <h1 class="display-6">Restablecer contraseña</h1>
        <p class="texto-gris mb-4">Revisa tu correo e ingresa el código de 6 dígitos.</p>
        <c:if test="${vencido}">
            <form action="${ctx}/recuperar" method="post" class="alert alert-info small d-flex justify-content-between align-items-center">
                <input type="hidden" name="_csrf" value="${csrfToken}">
                <input type="hidden" name="correo" value="<c:out value='${passwordForm.correo}'/>">
                <span>¿Necesitas un código nuevo?</span><button class="btn btn-sm btn-ciruela">Enviar otro</button>
            </form>
        </c:if>
        <form:form modelAttribute="passwordForm" action="${ctx}/restablecer" method="post" novalidate="novalidate">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <div class="mb-3">
                <label for="correo" class="form-label">Correo</label>
                <form:input path="correo" id="correo" type="email" cssClass="form-control" required="required"/>
            </div>
            <div class="mb-3">
                <label for="codigo" class="form-label">Código</label>
                <form:input path="codigo" id="codigo" inputmode="numeric" maxlength="6" cssClass="form-control fs-4 text-center letras-separadas" autocomplete="one-time-code"/>
            </div>
            <div class="mb-3">
                <label for="nueva" class="form-label">Nueva contraseña</label>
                <form:password path="nueva" id="nueva" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="new-password"/>
                <form:errors path="nueva" cssClass="invalid-feedback"/>
            </div>
            <div class="mb-4">
                <label for="confirmacion" class="form-label">Confirmar contraseña</label>
                <form:password path="confirmacion" id="confirmacion" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="new-password"/>
                <form:errors path="confirmacion" cssClass="invalid-feedback"/>
                <form:errors path="passwordsIguales" cssClass="text-danger small d-block mt-1"/>
            </div>
            <button class="btn btn-ciruela w-100">Cambiar contraseña</button>
        </form:form>
        <p class="text-center small mt-4 mb-0"><a href="${ctx}/login">Iniciar sesión con todos mis datos</a></p>
    </div>
</div>
</t:pagina>
