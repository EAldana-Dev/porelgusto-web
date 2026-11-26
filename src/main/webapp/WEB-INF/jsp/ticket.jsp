<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Ticket ${pedido.folio}" simple="true">
<div class="container py-4" style="max-width: 720px">
    <div class="d-flex justify-content-end gap-2 mb-3 no-imprimir">
        <a href="${ctx}/pedido/${pedido.folio}" class="btn btn-link">Volver al pedido</a>
        <button class="btn btn-ciruela" onclick="window.print()"><i class="bi bi-printer me-2"></i>Imprimir o guardar PDF</button>
    </div>
    <div class="tarjeta p-4 p-md-5">
        <div class="d-flex justify-content-between align-items-start mb-4">
            <div class="d-flex flex-column lh-1">
                <span class="logo-titulo">POR EL GUSTO</span>
                <span class="logo-subtitulo">ENVÍO DE FLORES</span>
            </div>
            <div class="text-end small">
                <div class="fw-bold">Ticket digital y comprobante de pago</div>
                <div>Folio <strong>${pedido.folio}</strong></div>
                <c:if test="${not empty pedido.ticket}"><div>Ticket <strong>${pedido.ticket.folioVenta}</strong></div></c:if>
                <div class="texto-gris">${pg:fechaHora(pedido.fechaHora)}</div>
            </div>
        </div>
        <div class="row small mb-4">
            <div class="col-6"><div class="texto-gris">Cliente</div><c:out value="${pedido.nombreCliente}"/><br><c:out value="${pedido.correoCliente}"/></div>
            <div class="col-6 text-end"><div class="texto-gris">Entrega</div>${pg:fecha(pedido.fechaEntrega)}<c:if test="${not empty pedido.horarioEntrega}">, ${pedido.horarioEntrega}</c:if><br><c:out value="${pedido.destinatario}"/></div>
        </div>
        <table class="table table-sm small">
            <thead><tr><th>Producto</th><th class="text-center">Cant.</th><th class="text-end">Precio</th><th class="text-end">Importe</th></tr></thead>
            <tbody>
            <c:forEach items="${pedido.items}" var="i">
                <tr><td>${i.arregloFloral.sku} · <c:out value="${i.arregloFloral.nombre}"/><c:if test="${not empty i.personalizacion}"><br><span class="texto-gris"><c:out value="${i.personalizacion}"/></span></c:if></td>
                    <td class="text-center">${i.cantidad}</td><td class="text-end">${pg:dinero(i.precioUnitario)}</td><td class="text-end">${pg:dinero(i.subtotal)}</td></tr>
            </c:forEach>
            </tbody>
            <tfoot class="border-top">
                <tr><td colspan="3" class="text-end">Subtotal</td><td class="text-end">${pg:dinero(pedido.subtotal)}</td></tr>
                <c:if test="${pedido.descuento > 0}"><tr><td colspan="3" class="text-end">Descuento (<c:out value="${pedido.cupon.codigo}"/>)</td><td class="text-end">−${pg:dinero(pedido.descuento)}</td></tr></c:if>
                <tr><td colspan="3" class="text-end">Envío</td><td class="text-end">${pg:dinero(pedido.costoEnvio)}</td></tr>
                <tr><td colspan="3" class="text-end texto-gris">IVA incluido (16%)</td><td class="text-end texto-gris">${pg:dinero(pedido.iva)}</td></tr>
                <tr class="fw-bold"><td colspan="3" class="text-end">Total</td><td class="text-end">${pg:dinero(pedido.total)}</td></tr>
            </tfoot>
        </table>
        <h2 class="h6 mt-4">Pago</h2>
        <table class="table table-sm small mb-4">
            <thead><tr><th>Método</th><th>Estado</th><th>Referencia</th><th class="text-end">Monto</th></tr></thead>
            <tbody>
                <tr><td><c:out value="${empty pedido.formaPago ? pedido.metodoPago.tipoPago : pedido.formaPago.etiqueta}"/><c:if test="${not empty pedido.metodoPago.descripcion}"><br><span class="texto-gris"><c:out value="${pedido.metodoPago.descripcion}"/></span></c:if></td>
                    <td>${pedido.estadoPago}</td>
                    <td class="font-monospace"><c:out value="${pedido.transaccion}"/></td><td class="text-end">${pg:dinero(pedido.total)}</td></tr>
            </tbody>
        </table>
        <p class="small texto-gris mb-0">Este comprobante no es una factura fiscal. Puedes solicitar tu factura desde el detalle de tu pedido.</p>
    </div>
</div>
</t:pagina>
