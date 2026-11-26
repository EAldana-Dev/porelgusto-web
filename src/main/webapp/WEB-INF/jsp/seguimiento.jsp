<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Seguimiento de pedido">
<div class="container py-5" style="max-width: 560px">
    <div class="text-center mb-4">
        <i class="bi bi-truck display-4 texto-ciruela"></i>
        <h1 class="display-6 mt-2">Consulta tu pedido</h1>
        <p class="texto-gris">Escribe el folio que te enviamos por correo y el correo con el que compraste.</p>
    </div>
    <form action="${ctx}/seguimiento" method="post" class="tarjeta p-4">
        <input type="hidden" name="_csrf" value="${csrfToken}">
        <div class="mb-3">
            <label for="folio" class="form-label">Folio del pedido</label>
            <input id="folio" name="folio" class="form-control text-uppercase" required placeholder="PG2611091234" value="<c:out value='${not empty folioBuscado ? folioBuscado : folio}'/>">
        </div>
        <div class="mb-4">
            <label for="correo" class="form-label">Correo</label>
            <input id="correo" name="correo" type="email" class="form-control" required value="<c:out value='${usuarioActual.correoElectronico}'/>">
        </div>
        <button class="btn btn-ciruela w-100">Buscar pedido</button>
    </form>
    <c:if test="${not empty usuarioActual}">
        <p class="text-center mt-3"><a href="${ctx}/cuenta/pedidos">Ver todos mis pedidos</a></p>
    </c:if>
</div>
</t:pagina>
