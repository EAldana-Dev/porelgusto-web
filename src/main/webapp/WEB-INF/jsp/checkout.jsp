<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Entrega y pago">
<div class="container py-4">
    <div class="pasos mb-4"><span>1. Carrito</span><span>›</span><span class="activo">2. Entrega y pago</span><span>›</span><span>3. Confirmación</span></div>

    <form:form modelAttribute="checkoutForm" action="${ctx}/checkout" method="post" novalidate="novalidate" data-cotizar-envio="${ctx}/checkout/envio">
    <input type="hidden" name="_csrf" value="${csrfToken}">
    <div class="row g-4">
        <div class="col-lg-7">
            <c:if test="${empty usuarioActual}">
                <div class="alert alert-info small"><i class="bi bi-person me-1"></i>¿Ya tienes cuenta? <a href="${ctx}/login?next=/checkout">Inicia sesión</a> para usar tus direcciones y ver tus pedidos después. No es obligatorio.</div>
            </c:if>

            <!-- Contacto -->
            <section class="tarjeta p-3 p-lg-4 mb-4">
                <h2 class="h4 mb-3">Tus datos</h2>
                <div class="row g-3">
                    <div class="col-md-6">
                        <label for="nombreCliente" class="form-label">Nombre completo</label>
                        <form:input path="nombreCliente" id="nombreCliente" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="name"/>
                        <form:errors path="nombreCliente" cssClass="invalid-feedback"/>
                    </div>
                    <div class="col-md-6">
                        <label for="telefonoCliente" class="form-label">Teléfono</label>
                        <form:input path="telefonoCliente" id="telefonoCliente" type="tel" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="tel"/>
                        <form:errors path="telefonoCliente" cssClass="invalid-feedback"/>
                    </div>
                    <div class="col-12">
                        <label for="correoCliente" class="form-label">Correo (te enviamos el ticket)</label>
                        <form:input path="correoCliente" id="correoCliente" type="email" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="email"/>
                        <form:errors path="correoCliente" cssClass="invalid-feedback"/>
                    </div>
                </div>
            </section>

            <!-- Entrega -->
            <section class="tarjeta p-3 p-lg-4 mb-4">
                <div class="d-flex justify-content-between align-items-start mb-3">
                    <h2 class="h4 mb-0">¿A dónde lo llevamos?</h2>
                    <span class="small text-end">${pg:fechaLarga(carrito.fechaEntrega)}<br><span class="texto-gris">${carrito.horarioEntrega}</span></span>
                </div>
                <c:if test="${not empty direcciones}">
                    <p class="small texto-gris mb-2">Usar una dirección guardada:</p>
                    <div class="d-flex flex-wrap gap-2 mb-3">
                        <c:forEach items="${direcciones}" var="d">
                            <button type="button" class="btn btn-sm btn-outline-secondary btn-cuadrado text-start"
                                    data-direccion='{"destinatario":"${fn:escapeXml(d.nombre)}","telefonoDestinatario":"${fn:escapeXml(d.telefono)}","calle":"${fn:escapeXml(d.calle)}","colonia":"${fn:escapeXml(d.colonia)}","ciudad":"${fn:escapeXml(d.ciudad)}","codigoPostal":"${fn:escapeXml(d.codigoPostal)}","referencias":"${fn:escapeXml(d.referencias)}"}'>
                                <i class="bi bi-geo-alt me-1"></i><c:out value="${d.alias}"/><br><span class="small texto-gris"><c:out value="${d.calle}"/></span>
                            </button>
                        </c:forEach>
                    </div>
                </c:if>
                <div class="row g-3">
                    <div class="col-md-7">
                        <label for="destinatario" class="form-label">¿Quién recibe?</label>
                        <form:input path="destinatario" id="destinatario" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                        <form:errors path="destinatario" cssClass="invalid-feedback"/>
                    </div>
                    <div class="col-md-5">
                        <label for="telefonoDestinatario" class="form-label">Teléfono de quien recibe</label>
                        <form:input path="telefonoDestinatario" id="telefonoDestinatario" type="tel" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                        <form:errors path="telefonoDestinatario" cssClass="invalid-feedback"/>
                    </div>
                    <div class="col-12">
                        <label for="calle" class="form-label">Calle y número</label>
                        <form:input path="calle" id="calle" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="street-address"/>
                        <form:errors path="calle" cssClass="invalid-feedback"/>
                    </div>
                    <div class="col-md-5">
                        <label for="colonia" class="form-label">Colonia</label>
                        <form:input path="colonia" id="colonia" cssClass="form-control"/>
                    </div>
                    <div class="col-md-4">
                        <label for="ciudad" class="form-label">Alcaldía o ciudad</label>
                        <form:input path="ciudad" id="ciudad" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                        <form:errors path="ciudad" cssClass="invalid-feedback"/>
                    </div>
                    <div class="col-md-3">
                        <label for="codigoPostal" class="form-label">C.P.</label>
                        <form:input path="codigoPostal" id="codigoPostal" inputmode="numeric" maxlength="5" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="postal-code"/>
                        <form:errors path="codigoPostal" cssClass="invalid-feedback"/>
                    </div>
                    <div class="col-12">
                        <label for="referencias" class="form-label">Referencias <span class="texto-gris fw-normal">(opcional)</span></label>
                        <form:input path="referencias" id="referencias" cssClass="form-control" placeholder="Ej. portón negro, tocar el timbre 2"/>
                    </div>
                    <c:if test="${not empty usuarioActual}">
                        <div class="col-12 form-check ms-1">
                            <form:checkbox path="guardarDireccion" id="guardarDireccion" cssClass="form-check-input"/>
                            <label for="guardarDireccion" class="form-check-label small">Guardar esta dirección en mi cuenta</label>
                        </div>
                    </c:if>
                </div>
            </section>

            <!-- Dedicatoria con QR -->
            <c:if test="${permiteQR}">
                <section class="tarjeta p-3 p-lg-4 mb-4">
                    <h2 class="h4 mb-1"><i class="bi bi-qr-code me-2 texto-ciruela"></i>Dedicatoria con código QR <span class="small texto-gris fw-normal">(gratis, opcional)</span></h2>
                    <p class="small texto-gris">Incluimos una tarjeta con un QR. Al escanearlo, quien recibe verá tu mensaje.</p>
                    <form:textarea path="dedicatoria" id="dedicatoria" rows="3" maxlength="500" cssClass="form-control" cssErrorClass="form-control is-invalid" placeholder="Escribe tu mensaje…"/>
                    <form:errors path="dedicatoria" cssClass="invalid-feedback"/>
                </section>
            </c:if>

            <!-- Método de pago -->
            <section class="tarjeta p-3 p-lg-4">
                <h2 class="h4 mb-3">Método de pago</h2>
                <form:errors path="metodoPago" cssClass="alert alert-danger d-block small py-2"/>
                <div class="d-grid gap-2">
                    <c:if test="${not tarjetaDisponible and not paypalDisponible}">
                        <div class="alert alert-info small mb-0"><i class="bi bi-whatsapp me-1"></i>Por ahora el pago en línea no está disponible. Puedes completar tu compra <a href="${ctx}/carrito/whatsapp" target="_blank" rel="noopener">por WhatsApp</a>.</div>
                    </c:if>
                    <c:if test="${tarjetaDisponible}">
                    <label class="metodo-pago">
                        <form:radiobutton path="metodoPago" value="TARJETA" cssClass="form-check-input mt-0"/>
                        <i class="bi bi-credit-card-2-front"></i>
                        <span class="flex-grow-1"><strong>Tarjeta de crédito o débito</strong><br><span class="small texto-gris">Visa, Mastercard y American Express, de crédito o débito</span></span>
                    </label>
                    </c:if>
                    <c:if test="${paypalDisponible}">
                    <label class="metodo-pago">
                        <form:radiobutton path="metodoPago" value="PAYPAL" cssClass="form-check-input mt-0"/>
                        <i class="bi bi-paypal"></i>
                        <span class="flex-grow-1"><strong>PayPal</strong><br><span class="small texto-gris">Paga con tu cuenta de PayPal</span></span>
                    </label>
                    </c:if>
                </div>
            </section>
        </div>

        <!-- Resumen -->
        <div class="col-lg-5">
            <div class="tarjeta p-4 resumen-compra">
                <h2 class="h4 mb-3">Tu pedido</h2>
                <c:forEach items="${carrito.lineas}" var="i">
                    <div class="d-flex gap-3 mb-3">
                        <img src="${ctx}${i.imagenUrl}" alt="" class="rounded-3" style="width: 56px; height: 70px; object-fit: cover">
                        <div class="flex-grow-1 small">
                            <div class="fw-semibold"><c:out value="${i.nombre}"/></div>
                            <div class="texto-gris">${i.cantidad} × ${pg:dinero(i.precioUnitario)}</div>
                            <c:if test="${not empty i.nombresOpciones}"><div class="texto-gris"><c:out value="${i.opcionesTexto}"/></div></c:if>
                        </div>
                        <span class="small fw-bold">${pg:dinero(i.subtotal)}</span>
                    </div>
                </c:forEach>
                <hr class="borde-pg">
                <dl class="row small mb-0">
                    <dt class="col-7 fw-normal">Subtotal</dt><dd class="col-5 text-end">${pg:dinero(resumen.subtotal)}</dd>
                    <c:if test="${resumen.descuento > 0}"><dt class="col-7 fw-normal texto-ciruela">Cupón <c:out value="${resumen.cupon}"/></dt><dd class="col-5 text-end texto-ciruela">−${pg:dinero(resumen.descuento)}</dd></c:if>
                    <dt class="col-7 fw-normal">Envío<br><span class="texto-gris small" data-envio-detalle><c:out value="${not resumen.envioCalculado ? 'Escribe el código postal' : resumen.envioDetalle}"/></span></dt>
                    <dd class="col-5 text-end" data-envio-costo>${not resumen.envioCalculado ? 'Por calcular' : resumen.envio.signum() == 0 ? 'Gratis' : pg:dinero(resumen.envio)}</dd>
                    <dt class="col-7 fw-normal texto-gris">IVA incluido</dt><dd class="col-5 text-end texto-gris" data-envio-iva>${pg:dinero(resumen.iva)}</dd>
                </dl>
                <hr class="borde-pg">
                <div class="d-flex justify-content-between align-items-baseline mb-3">
                    <span class="fw-semibold">Total a pagar</span><span class="fs-3 fw-bold texto-ciruela" data-envio-total>${pg:dinero(resumen.total)}</span>
                </div>
                <p class="small texto-gris"><i class="bi bi-truck me-1"></i>Envío en línea recta desde la florería: hasta ${tienda.envioKm1} km ${pg:dinero(tienda.envioTarifa10)}, hasta ${tienda.envioKm2} km ${pg:dinero(tienda.envioTarifa15)}, hasta ${tienda.envioKm3} km ${pg:dinero(tienda.envioTarifa20)}; gratis desde ${pg:dinero(tienda.envioGratisDesde)}. Más de ${tienda.envioKm3} km o fuera de la CDMX: ${pg:dinero(tienda.envioTarifa20)}.</p>
                <button class="btn btn-ciruela btn-lg w-100"><i class="bi bi-lock me-2"></i>Pagar <span data-envio-total>${pg:dinero(resumen.total)}</span></button>
                <a href="${ctx}/carrito" class="btn btn-link w-100 mt-2">Volver al carrito</a>
            </div>
        </div>
    </div>
    </form:form>
</div>
</t:pagina>
