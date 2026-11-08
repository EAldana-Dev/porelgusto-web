<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/fragments/header.jsp" %>

<div class="p-5 mb-4 bg-body-tertiary rounded-3">
    <h1 class="display-5 fw-bold">¡El proyecto funciona!</h1>
    <p class="col-md-8 fs-5">Spring Boot + Hibernate + JSP + Bootstrap.</p>
    <a class="btn btn-primary btn-lg" href="<c:url value='/h2-console'/>">Abrir consola H2</a>
</div>

<%@ include file="/WEB-INF/jsp/fragments/footer.jsp" %>
