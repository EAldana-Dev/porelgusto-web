<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Pedido ${pedido.folio}">
<div class="container py-4" style="max-width: 1000px">
    <div class="d-flex flex-wrap justify-content-between align-items-end gap-2 mb-4">
        <div>
            <p class="etiqueta-seccion mb-1">Pedido del ${pg:fecha(pedido.fechaHora)}</p>
            <h1 class="display-6 mb-0">Folio ${pedido.folio}</h1>
        </div>
        <span class="badge rounded-pill text-bg-${pedido.estado.color} fs-6 px-3 py-2">${pedido.estado.etiqueta}</span>
    </div>

    <!-- Línea de tiempo: pendiente, preparando, en camino, entregado -->
    <c:choose>
        <c:when test="${pedido.estado == 'CANCELADO'}">
            <div class="alert alert-danger"><i class="bi bi-x-circle-fill me-2"></i>Este pedido fue cancelado.
                <c:if test="${pedido.estadoPago == 'REEMBOLSADO'}"> Te devolvimos el cobro.</c:if></div>
        </c:when>
        <c:when test="${pedido.estado == 'PENDIENTE_PAGO'}">
            <div class="alert alert-info"><i class="bi bi-hourglass-split me-2"></i>Estamos confirmando tu pago de <strong>${pg:dinero(pedido.total)}</strong>.</div>
        </c:when>
    </c:choose>
    <div class="tarjeta p-3 p-lg-4 mb-4">
        <div class="linea-tiempo">
            <c:forEach items="${etapas}" var="e">
                <c:set var="fechaEtapa" value="${pedido.fechaDe(e)}"/>
                <div class="etapa ${not empty fechaEtapa ? 'hecha' : ''} ${pedido.estado == e ? 'actual' : ''}">
                    <div class="circulo"><i class="bi ${e == 'PENDIENTE' ? 'bi-receipt' : e == 'PREPARANDO' ? 'bi-flower1' : e == 'EN_CAMINO' ? 'bi-truck' : 'bi-house-heart'}"></i></div>
                    <div class="fw-semibold">${e.etiqueta}</div>
                    <div class="small texto-gris">${pg:fechaHora(fechaEtapa)}</div>
                </div>
            </c:forEach>
        </div>

        <c:if test="${pedido.conEnvio}">
            <div class="bg-rosa-suave rounded-4 p-3">
                <div class="d-flex flex-wrap justify-content-between gap-2">
                    <div>
                        <div class="small texto-gris">Guía de rastreo</div>
                        <div class="fw-bold">${pedido.numeroGuia} · <span data-rastreo-estado>${pedido.estado.etiqueta}</span></div>
                        <div class="small"><i class="bi bi-person-badge me-1"></i><c:out value="${pedido.repartidor}"/>
                            <c:if test="${not empty pedido.telefonoRepartidor}"> · <a href="tel:${pedido.telefonoRepartidor}"><c:out value="${pedido.telefonoRepartidor}"/></a></c:if></div>
                    </div>
                    <div class="text-end">
                        <div class="small texto-gris">${pedido.estado == 'ENTREGADO' ? 'Entregado' : 'Llegada estimada'}</div>
                        <div class="fw-bold">${pg:fechaHora(pedido.estado == 'ENTREGADO' ? pedido.fechaEntregado : pedido.horaEstimada)}</div>
                        <c:if test="${not empty pedido.recibio}"><div class="small">Recibió: <c:out value="${pedido.recibio}"/><c:if test="${pedido.entregaVerificadaQR}"> <i class="bi bi-qr-code" title="Verificado con QR"></i></c:if></div></c:if>
                    </div>
                </div>
                <c:if test="${pedido.estado == 'EN_CAMINO'}">
                    <p class="small texto-gris mt-2 mb-2" data-rastreo-actualizado>Aún sin ubicación del repartidor</p>
                    <iframe class="w-100 rounded-3 border-0 d-none" style="height: 260px" data-rastreo="${pedido.numeroGuia}" title="Ubicación del repartidor"></iframe>
                </c:if>
            </div>
        </c:if>
    </div>

    <div class="row g-4">
        <div class="col-lg-7">
            <div class="tarjeta p-4 mb-4">
                <h2 class="h5 mb-3">Productos</h2>
                <c:forEach items="${pedido.items}" var="i">
                    <div class="d-flex gap-3 mb-3">
                        <img src="${ctx}${i.arregloFloral.imagenUrl}" alt="" class="rounded-3" style="width: 60px; height: 75px; object-fit: cover">
                        <div class="flex-grow-1 small">
                            <div class="fw-semibold"><c:out value="${i.arregloFloral.nombre}"/></div>
                            <div class="texto-gris">${i.cantidad} × ${pg:dinero(i.precioUnitario)}</div>
                            <c:if test="${not empty i.personalizacion}"><div class="texto-gris"><i class="bi bi-magic me-1"></i><c:out value="${i.personalizacion}"/></div></c:if>
                            <c:if test="${not empty i.nota}"><div class="texto-gris"><i class="bi bi-chat-left-text me-1"></i><c:out value="${i.nota}"/></div></c:if>
                        </div>
                        <span class="small fw-bold">${pg:dinero(i.subtotal)}</span>
                    </div>
                </c:forEach>
                <hr class="borde-pg">
                <dl class="row small mb-0">
                    <dt class="col-7 fw-normal">Subtotal</dt><dd class="col-5 text-end">${pg:dinero(pedido.subtotal)}</dd>
                    <c:if test="${pedido.descuento > 0}"><dt class="col-7 fw-normal">Descuento (<c:out value="${pedido.cupon.codigo}"/>)</dt><dd class="col-5 text-end">−${pg:dinero(pedido.descuento)}</dd></c:if>
                    <dt class="col-7 fw-normal">Envío<c:if test="${not empty pedido.distanciaKm}"> <span class="texto-gris">(${pedido.distanciaKm} km)</span></c:if></dt><dd class="col-5 text-end">${pedido.costoEnvio.signum() == 0 ? 'Gratis' : pg:dinero(pedido.costoEnvio)}</dd>
                    <dt class="col-7 fw-normal texto-gris">IVA incluido</dt><dd class="col-5 text-end texto-gris">${pg:dinero(pedido.iva)}</dd>
                    <dt class="col-7">Total</dt><dd class="col-5 text-end fw-bold texto-ciruela">${pg:dinero(pedido.total)}</dd>
                </dl>
            </div>

            <!-- Factura (tabla Factura) -->
            <div class="tarjeta p-4 mb-4" id="factura">
                <h2 class="h5 mb-3"><i class="bi bi-file-earmark-text me-2 texto-ciruela"></i>Factura</h2>
                <c:choose>
                    <c:when test="${not empty pedido.factura}">
                        <p class="small mb-1">Solicitada el ${pg:fecha(pedido.factura.fechaEmision)} para <strong><c:out value="${pedido.factura.razonSocial}"/></strong> (RFC <c:out value="${pedido.factura.rfc}"/>).</p>
                        <p class="small texto-gris mb-0">Uso del CFDI: <c:out value="${pedido.factura.cfdi}"/> · Total ${pg:dinero(pedido.factura.total)}. Te la enviamos a tu correo.</p>
                    </c:when>
                    <c:when test="${pedido.estado == 'CANCELADO'}"><p class="small texto-gris mb-0">Los pedidos cancelados no se facturan.</p></c:when>
                    <c:when test="${pedido.estadoPago == 'PENDIENTE'}"><p class="small texto-gris mb-0">Podrás pedir tu factura cuando confirmemos tu pago.</p></c:when>
                    <c:otherwise>
                        <p class="small texto-gris">¿Necesitas factura? Escribe tus datos fiscales y te la enviamos por correo.</p>
                        <form:form modelAttribute="facturaForm" action="${ctx}/pedido/${pedido.folio}/factura" method="post" novalidate="novalidate" cssClass="row g-2">
                            <input type="hidden" name="_csrf" value="${csrfToken}">
                            <div class="col-md-5">
                                <label for="rfc" class="form-label small">RFC</label>
                                <form:input path="rfc" id="rfc" cssClass="form-control text-uppercase" cssErrorClass="form-control text-uppercase is-invalid" maxlength="13"/>
                                <form:errors path="rfc" cssClass="invalid-feedback"/>
                            </div>
                            <div class="col-md-7">
                                <label for="razonSocial" class="form-label small">Nombre o razón social</label>
                                <form:input path="razonSocial" id="razonSocial" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                                <form:errors path="razonSocial" cssClass="invalid-feedback"/>
                            </div>
                            <div class="col-12">
                                <label for="direccionFiscal" class="form-label small">Domicilio fiscal</label>
                                <form:input path="direccionFiscal" id="direccionFiscal" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                                <form:errors path="direccionFiscal" cssClass="invalid-feedback"/>
                            </div>
                            <div class="col-md-8">
                                <label for="cfdi" class="form-label small">Uso del CFDI</label>
                                <form:select path="cfdi" id="cfdi" cssClass="form-select">
                                    <form:options items="${usosCfdi}"/>
                                </form:select>
                            </div>
                            <div class="col-md-4 d-flex align-items-end"><button class="btn btn-outline-ciruela w-100">Solicitar</button></div>
                        </form:form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="col-lg-5">
            <div class="tarjeta p-4 mb-4">
                <h2 class="h5 mb-3">Entrega</h2>
                <p class="small mb-1"><i class="bi bi-calendar-heart me-2 texto-ciruela"></i>${pg:fechaLarga(pedido.fechaEntrega)}<c:if test="${not empty pedido.horarioEntrega}">, ${pedido.horarioEntrega}</c:if></p>
                <p class="small mb-1"><i class="bi bi-person me-2 texto-ciruela"></i><c:out value="${pedido.destinatario}"/></p>
                <p class="small mb-0"><i class="bi bi-geo-alt me-2 texto-ciruela"></i><c:out value="${pedido.direccionEntrega}"/></p>
                <c:if test="${not empty pedido.instruccionesEspeciales}"><p class="small mt-1 mb-0 texto-gris"><c:out value="${pedido.instruccionesEspeciales}"/></p></c:if>
            </div>
            <div class="tarjeta p-4 mb-4">
                <h2 class="h5 mb-3">Historial</h2>
                <ul class="historial list-unstyled small mb-0">
                    <c:forEach items="${pedido.historial}" var="h">
                        <li><strong><c:out value="${h.estado}"/></strong> · <span class="texto-gris">${pg:fechaHora(h.fechaHora)}</span>
                            <c:if test="${not empty h.nota}"><br><c:out value="${h.nota}"/></c:if></li>
                    </c:forEach>
                </ul>
            </div>
            <div class="d-grid gap-2">
                <a href="${ctx}/pedido/${pedido.folio}/ticket" target="_blank" class="btn btn-outline-ciruela"><i class="bi bi-receipt me-2"></i>Ticket y comprobante de pago</a>
                <c:if test="${pedido.conDedicatoria}"><a href="${ctx}/qr/${pedido.tokenQr}" target="_blank" class="btn btn-outline-ciruela"><i class="bi bi-qr-code me-2"></i>Ver dedicatoria</a></c:if>
                <a href="${ctx}/soporte" class="btn btn-link"><i class="bi bi-life-preserver me-1"></i>¿Algún problema? Contáctanos</a>
            </div>
        </div>
    </div>
</div>
</t:pagina>
