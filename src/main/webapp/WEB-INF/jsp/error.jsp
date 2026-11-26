<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="codigo" value="${not empty status ? status : requestScope['jakarta.servlet.error.status_code']}"/>
<t:pagina titulo="${codigo == 404 ? 'Página no encontrada' : codigo == 403 ? 'Acceso denegado' : 'Algo salió mal'}">
<div class="container py-5 text-center" style="max-width: 600px">
    <p class="display-1 titulo-serif texto-rosa mb-0">${codigo}</p>
    <c:choose>
        <c:when test="${codigo == 404}">
            <h1 class="display-6">No encontramos esta página</h1>
            <p class="texto-gris"><c:out value="${not empty message and message != 'No message available' ? message : 'Puede que el enlace esté mal escrito o que el producto ya no exista.'}"/></p>
        </c:when>
        <c:when test="${codigo == 403}">
            <h1 class="display-6">No tienes acceso</h1>
            <p class="texto-gris"><c:out value="${not empty message and message != 'No message available' and message != 'Forbidden' ? message : 'No tienes permiso para ver esta sección o la página expiró. Recarga e inténtalo de nuevo.'}"/></p>
        </c:when>
        <c:otherwise>
            <h1 class="display-6">Algo salió mal</h1>
            <p class="texto-gris">Tuvimos un problema al procesar tu solicitud. Intenta de nuevo en un momento.</p>
        </c:otherwise>
    </c:choose>
    <a href="${ctx}/" class="btn btn-ciruela px-4 mt-2">Ir al inicio</a>
    <a href="${ctx}/catalogo" class="btn btn-outline-ciruela px-4 mt-2 ms-2">Ver catálogo</a>
</div>
</t:pagina>
