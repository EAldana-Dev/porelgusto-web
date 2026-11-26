<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="PayPal" simple="true">
<div class="container py-5" style="max-width: 480px">
    <div class="tarjeta p-4 text-center">
        <div class="fs-1 fw-bold mb-2" style="color: #003087"><i class="bi bi-paypal"></i> PayPal</div>
        <p class="mb-1">Vas a pagar a <strong>Por el Gusto</strong></p>
        <p class="display-6 fw-bold mb-4">${pg:dinero(compra.total)} <span class="fs-6 texto-gris">MXN</span></p>
        <form action="${ctx}/pago/paypal-simulado" method="post" class="d-grid gap-2">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button name="accion" value="aprobar" class="btn btn-lg text-white" style="background: #0070ba">Pagar ahora</button>
            <button name="accion" value="cancelar" class="btn btn-link">Cancelar y volver a la tienda</button>
        </form>
    </div>
</div>
</t:pagina>
