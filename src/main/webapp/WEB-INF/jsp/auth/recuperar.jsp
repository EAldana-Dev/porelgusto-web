<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Recuperar contraseña">
<div class="container py-5" style="max-width: 520px">
    <div class="tarjeta p-4 p-lg-5">
        <i class="bi bi-key display-5 texto-ciruela"></i>
        <h1 class="display-6 mt-2">¿Olvidaste tu contraseña?</h1>
        <p class="texto-gris mb-4">Escribe el correo de tu cuenta y te enviaremos un código temporal para restablecerla.</p>
        <form action="${ctx}/recuperar" method="post">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <label for="correo" class="form-label">Correo</label>
            <input id="correo" name="correo" type="email" class="form-control mb-4" required autofocus value="<c:out value='${correo}'/>">
            <button class="btn btn-ciruela w-100">Enviar código</button>
        </form>
        <p class="text-center small mt-4 mb-0"><a href="${ctx}/restablecer">Ya tengo un código</a> · <a href="${ctx}/login">Volver a iniciar sesión</a></p>
    </div>
</div>
</t:pagina>
