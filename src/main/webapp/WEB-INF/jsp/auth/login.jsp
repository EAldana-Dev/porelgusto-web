<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Iniciar sesión">
<div class="container py-5">
    <div class="row g-4 align-items-stretch justify-content-center">
        <div class="col-lg-5">
            <div class="tarjeta p-4 p-lg-5 h-100">
                <h1 class="display-6 mb-1">Iniciar sesión</h1>
                <p class="texto-gris mb-4">Es opcional: puedes ver el catálogo y comprar sin cuenta.</p>
                <form action="${ctx}/login" method="post">
                    <input type="hidden" name="_csrf" value="${csrfToken}">
                    <input type="hidden" name="next" value="<c:out value='${next}'/>">
                    <div class="mb-3">
                        <label for="usuario" class="form-label">Correo o nombre de usuario</label>
                        <input id="usuario" name="usuario" class="form-control ${not empty error ? 'is-invalid' : ''}" required autofocus autocomplete="username" value="<c:out value='${login}'/>">
                    </div>
                    <div class="mb-2">
                        <label for="password" class="form-label">Contraseña</label>
                        <input id="password" name="password" type="password" class="form-control ${not empty error ? 'is-invalid' : ''}" required autocomplete="current-password">
                    </div>
                    <div class="text-end mb-4"><a href="${ctx}/recuperar" class="small">¿Olvidaste tu contraseña?</a></div>
                    <button class="btn btn-ciruela btn-lg w-100">Entrar</button>
                </form>
                <p class="text-center small mt-4 mb-0">¿No tienes cuenta? <a href="${ctx}/registro">Regístrate</a></p>
            </div>
        </div>
        <div class="col-lg-5 d-none d-lg-block"><div class="auth-fondo h-100"></div></div>
    </div>
</div>
</t:pagina>
