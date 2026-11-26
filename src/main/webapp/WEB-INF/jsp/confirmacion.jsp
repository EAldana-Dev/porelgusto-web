<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Pedido confirmado">
<div class="container py-5" style="max-width: 860px">
    <div class="pasos mb-4"><span>1. Carrito</span><span>›</span><span>2. Entrega y pago</span><span>›</span><span class="activo">3. Confirmación</span></div>
    <div class="text-center mb-4">
        <i class="bi bi-check-circle-fill display-3 texto-ciruela"></i>
        <h1 class="display-5 mt-2">¡Gracias por tu compra!</h1>
        <p class="texto-gris mb-1">Tu folio de pedido es</p>
        <p class="display-6 fw-bold letras-separadas texto-ciruela">${pedido.folio}</p>
        <p class="small texto-gris">Enviamos el comprobante a <strong><c:out value="${pedido.correoCliente}"/></strong>. Guarda tu folio para consultar el estado.</p>
    </div>


    <div class="row g-4">
        <div class="col-md-7">
            <div class="tarjeta p-4 h-100">
                <h2 class="h5 mb-3">Resumen</h2>
                <c:forEach items="${pedido.items}" var="i">
                    <div class="d-flex justify-content-between small mb-2">
                        <span>${i.cantidad} × <c:out value="${i.arregloFloral.nombre}"/><c:if test="${not empty i.personalizacion}"><br><span class="texto-gris"><c:out value="${i.personalizacion}"/></span></c:if></span>
                        <span>${pg:dinero(i.subtotal)}</span>
                    </div>
                </c:forEach>
                <hr class="borde-pg">
                <div class="d-flex justify-content-between fw-bold"><span>Total</span><span class="texto-ciruela">${pg:dinero(pedido.total)}</span></div>
                <hr class="borde-pg">
                <p class="small mb-1"><i class="bi bi-calendar-heart me-2 texto-ciruela"></i>${pg:fechaLarga(pedido.fechaEntrega)}, ${pedido.horarioEntrega}</p>
                <p class="small mb-0"><i class="bi bi-geo-alt me-2 texto-ciruela"></i><c:out value="${pedido.destinatario}"/>, <c:out value="${pedido.direccionEntrega}"/></p>
            </div>
        </div>
        <div class="col-md-5">
            <c:if test="${pedido.conDedicatoria}">
                <div class="tarjeta p-4 text-center h-100">
                    <h2 class="h5">Tu dedicatoria con QR</h2>
                    <img src="${ctx}/qr/${pedido.tokenQr}.png" alt="Código QR de la dedicatoria" width="170" height="170" class="my-2">
                    <p class="small texto-gris mb-2">La imprimimos en la tarjeta del arreglo.</p>
                    <a href="${ctx}/qr/${pedido.tokenQr}" target="_blank" class="small">Ver cómo se verá</a>
                </div>
            </c:if>
        </div>
    </div>

    <div class="d-flex flex-wrap justify-content-center gap-2 mt-4">
        <a href="${ctx}/pedido/${pedido.folio}" class="btn btn-ciruela px-4"><i class="bi bi-truck me-2"></i>Seguir mi pedido</a>
        <a href="${ctx}/pedido/${pedido.folio}/ticket" class="btn btn-outline-ciruela px-4" target="_blank"><i class="bi bi-receipt me-2"></i>Ticket digital</a>
        <a href="${ctx}/" class="btn btn-link">Volver al inicio</a>
    </div>
    <c:if test="${empty usuarioActual}">
        <p class="text-center small texto-gris mt-4">¿Quieres ver todos tus pedidos en un solo lugar? <a href="${ctx}/registro">Crea una cuenta</a>.</p>
    </c:if>
</div>
</t:pagina>
