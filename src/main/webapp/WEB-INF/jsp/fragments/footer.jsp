<%-- Pie común: cierra <main>, footer y scripts. Uso: <%@ include file="/WEB-INF/jsp/fragments/footer.jsp" %> --%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:useBean id="ahora" class="java.util.Date"/>
</main>
<footer class="bg-light border-top py-3 mt-auto">
    <div class="container text-center text-muted small">
        Por el Gusto &middot; <fmt:formatDate value="${ahora}" pattern="yyyy"/>
    </div>
</footer>
<script src="<c:url value='/webjars/bootstrap/js/bootstrap.bundle.min.js'/>"></script>
<script src="<c:url value='/js/app.js'/>"></script>
</body>
</html>
