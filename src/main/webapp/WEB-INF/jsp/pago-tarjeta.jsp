<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Pago con tarjeta">
<div class="container py-5" style="max-width: 900px">
    <div class="row g-4 align-items-center">
        <div class="col-md-6">
            <div class="tarjeta-visual d-flex flex-column justify-content-between mx-auto">
                <div class="d-flex justify-content-between"><i class="bi bi-flower1 fs-3"></i><i class="bi bi-credit-card-2-front fs-3"></i></div>
                <div class="fs-5 letras-separadas" data-vista-numero>•••• •••• •••• ••••</div>
                <div class="small" data-vista-titular>NOMBRE DEL TITULAR</div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="tarjeta p-4">
                <h1 class="h3 mb-1">Pago con tarjeta</h1>
                <p class="texto-gris mb-4">Total: <strong class="texto-ciruela fs-5">${pg:dinero(compra.total)}</strong></p>
                <c:if test="${not empty errorPago}">
                    <div class="alert alert-danger small"><i class="bi bi-x-octagon-fill me-1"></i><c:out value="${errorPago}"/>. No registramos tu pedido; puedes volver a intentarlo.</div>
                </c:if>
                <form action="${ctx}/pago/tarjeta" method="post" autocomplete="off">
                    <input type="hidden" name="_csrf" value="${csrfToken}">
                    <div class="mb-3">
                        <label for="titular" class="form-label">Nombre del titular</label>
                        <input id="titular" name="titular" class="form-control" required value="<c:out value='${titular}'/>">
                    </div>
                    <div class="mb-3">
                        <label for="numero" class="form-label">Número de tarjeta</label>
                        <input id="numero" name="numero" class="form-control" inputmode="numeric" required placeholder="1234 5678 9012 3456">
                    </div>
                    <div class="row g-3 mb-4">
                        <div class="col-6"><label for="vence" class="form-label">Vence</label><input id="vence" name="vence" class="form-control" placeholder="MM/AA" required></div>
                        <div class="col-6"><label for="cvv" class="form-label">CVV</label><input id="cvv" name="cvv" class="form-control" inputmode="numeric" maxlength="4" placeholder="123" required></div>
                    </div>
                    <button class="btn btn-ciruela btn-lg w-100"><i class="bi bi-lock me-2"></i>Pagar ${pg:dinero(compra.total)}</button>
                    <a href="${ctx}/checkout" class="btn btn-link w-100 mt-2">Cambiar método de pago</a>
                </form>
            </div>
        </div>
    </div>
</div>
</t:pagina>
