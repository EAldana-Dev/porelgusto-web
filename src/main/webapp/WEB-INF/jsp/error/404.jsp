<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Página no encontrada"/>
<%@ include file="/WEB-INF/jsp/fragments/header.jsp" %>

<section class="text-center py-5">
    <h1 class="display-1">404</h1>
    <p class="lead">La página que buscas no existe.</p>
    <a class="btn btn-outline-primary" href="<c:url value='/'/>">Volver al inicio</a>
</section>

<%@ include file="/WEB-INF/jsp/fragments/footer.jsp" %>
