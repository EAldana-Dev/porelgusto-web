<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="Pagos y envíos">
    <c:if test="${llaveIlegible}">
        <div class="alert alert-warning"><i class="bi bi-exclamation-triangle me-1"></i>Hay una llave guardada que ya no se puede leer (cambió la variable <code>AUTH_TOKEN_SECRET</code>). Vuelve a capturarla.</div>
    </c:if>
    <c:if test="${not empty config.actualizado}">
        <p class="small texto-gris">Último cambio: ${pg:fechaHora(config.actualizado)} · <c:out value="${config.actualizadoPor}"/></p>
    </c:if>

    <div class="row g-4">
        <!-- ==================== PAGOS ==================== -->
        <div class="col-xl-7">
            <form action="${ctx}/admin/configuracion/pagos" method="post" class="tarjeta p-4 mb-4" autocomplete="off">
                <input type="hidden" name="_csrf" value="${csrfToken}">
                <h2 class="h5 mb-1">¿A dónde llegan los pagos?</h2>
                <p class="small texto-gris">El dinero se deposita en la cuenta de Stripe o PayPal cuyas llaves estén aquí. Las llaves se guardan cifradas y nunca se muestran completas. Deja un campo vacío para conservar lo guardado.</p>

                <h3 class="h6 mt-4 d-flex align-items-center gap-2"><i class="bi bi-credit-card-2-front texto-ciruela"></i>Tarjeta (Stripe)
                    <span class="badge ${tarjetaSimulada ? 'text-bg-warning' : tarjetaPrueba ? 'text-bg-info' : 'text-bg-success'}">${tarjetaSimulada ? (simulacionPermitida ? 'Simulado' : 'Sin configurar') : tarjetaPrueba ? 'Modo prueba' : 'Pagos reales'}</span></h3>
                <c:if test="${not empty stripeMascara}"><p class="small mb-2">Llave actual: <code>${stripeMascara}</code></p></c:if>
                <label for="stripe" class="form-label small">Secret key de Stripe</label>
                <input id="stripe" name="stripe" class="form-control font-monospace" placeholder="sk_live_… o sk_test_…">
                <div class="form-text">En <a href="https://dashboard.stripe.com/apikeys" target="_blank" rel="noopener">dashboard.stripe.com/apikeys</a> → «Secret key». Con <code>sk_test_</code> no se cobra dinero real. Stripe cobra desde $10 MXN.</div>
                <c:if test="${not empty stripeMascara}">
                    <div class="form-check mt-2"><input class="form-check-input" type="checkbox" name="borrarStripe" value="true" id="borrarStripe"><label class="form-check-label small" for="borrarStripe">Quitar la llave (se deja de ofrecer el pago con tarjeta)</label></div>
                </c:if>

                <h3 class="h6 mt-4 d-flex align-items-center gap-2"><i class="bi bi-paypal texto-ciruela"></i>PayPal
                    <span class="badge ${paypalSimulado ? 'text-bg-warning' : paypalPrueba ? 'text-bg-info' : 'text-bg-success'}">${paypalSimulado ? (simulacionPermitida ? 'Simulado' : 'Sin configurar') : paypalPrueba ? 'Sandbox (prueba)' : 'Pagos reales'}</span></h3>
                <c:if test="${not empty paypalIdMascara}"><p class="small mb-2">Client ID actual: <code>${paypalIdMascara}</code></p></c:if>
                <div class="row g-2">
                    <div class="col-md-6"><label for="paypalId" class="form-label small">Client ID</label><input id="paypalId" name="paypalId" class="form-control font-monospace"></div>
                    <div class="col-md-6"><label for="paypalSecreta" class="form-label small">Secret</label><input id="paypalSecreta" name="paypalSecreta" type="password" class="form-control font-monospace" autocomplete="new-password"></div>
                </div>
                <div class="form-text">En <a href="https://developer.paypal.com/dashboard/applications" target="_blank" rel="noopener">developer.paypal.com</a> → Apps &amp; Credentials (elige Live o Sandbox).</div>
                <div class="form-check form-switch mt-2"><input class="form-check-input" type="checkbox" name="paypalProduccion" value="true" id="paypalProduccion" ${config.paypalProduccion ? 'checked' : ''}><label class="form-check-label small" for="paypalProduccion">Son credenciales <strong>Live</strong> (pagos reales). Apagado = Sandbox.</label></div>
                <c:if test="${not empty paypalIdMascara}">
                    <div class="form-check mt-1"><input class="form-check-input" type="checkbox" name="borrarPaypal" value="true" id="borrarPaypal"><label class="form-check-label small" for="borrarPaypal">Quitar las credenciales (se deja de ofrecer PayPal)</label></div>
                </c:if>

                <button class="btn btn-ciruela mt-4 px-4">Guardar datos de pago</button>
            </form>
            <div class="d-flex flex-wrap gap-2">
                <form action="${ctx}/admin/configuracion/probar/stripe" method="post"><input type="hidden" name="_csrf" value="${csrfToken}"><button class="btn btn-outline-ciruela btn-sm" ${tarjetaSimulada ? 'disabled' : ''}><i class="bi bi-plug me-1"></i>Probar conexión con Stripe</button></form>
                <form action="${ctx}/admin/configuracion/probar/paypal" method="post"><input type="hidden" name="_csrf" value="${csrfToken}"><button class="btn btn-outline-ciruela btn-sm" ${paypalSimulado ? 'disabled' : ''}><i class="bi bi-plug me-1"></i>Probar conexión con PayPal</button></form>
            </div>
            <p class="small texto-gris mt-3">Para cobrar de verdad, la cuenta de Stripe o PayPal debe estar verificada. Stripe deposita lo cobrado en la cuenta bancaria (CLABE) registrada en su panel. Una forma de pago sin llaves no se muestra a los clientes.</p>
        </div>

        <!-- ==================== ENVÍOS ==================== -->
        <div class="col-xl-5" id="envios">
            <form action="${ctx}/admin/configuracion/origen" method="post" class="tarjeta p-4 mb-4">
                <input type="hidden" name="_csrf" value="${csrfToken}">
                <h2 class="h5 mb-1">Punto de partida de los envíos</h2>
                <p class="small texto-gris">El costo se calcula en línea recta desde aquí.</p>
                <label for="direccion" class="form-label small">Dirección de la florería</label>
                <input id="direccion" name="direccion" class="form-control mb-2" required value="<c:out value='${config.origenDireccion}'/>">
                <details class="small mb-3"><summary class="texto-gris">Capturar coordenadas a mano (opcional)</summary>
                    <div class="row g-2 mt-1">
                        <div class="col-6"><label for="latitud" class="form-label small">Latitud</label><input id="latitud" name="latitud" type="number" step="0.0000001" class="form-control" placeholder="${config.origenLatitud}"></div>
                        <div class="col-6"><label for="longitud" class="form-label small">Longitud</label><input id="longitud" name="longitud" type="number" step="0.0000001" class="form-control" placeholder="${config.origenLongitud}"></div>
                    </div>
                    <span class="d-block texto-gris mt-1">Si las dejas vacías, la dirección se ubica en el mapa automáticamente.</span>
                </details>
                <button class="btn btn-ciruela px-4">Guardar punto de partida</button>
                <c:if test="${not empty config.origenLatitud}">
                    <iframe class="w-100 rounded-3 border-0 mt-3" style="height: 220px" title="Punto de partida"
                            src="https://www.openstreetmap.org/export/embed.html?bbox=${config.origenLongitud - 0.01},${config.origenLatitud - 0.01},${config.origenLongitud + 0.01},${config.origenLatitud + 0.01}&layer=mapnik&marker=${config.origenLatitud},${config.origenLongitud}"></iframe>
                </c:if>
            </form>
            <div class="tarjeta p-4">
                <h2 class="h6">Tarifas</h2>
                <table class="table table-sm small mb-2">
                    <tr><td>Hasta ${tienda.envioKm1} km</td><td class="text-end">${pg:dinero(tienda.envioTarifa10)}</td></tr>
                    <tr><td>Hasta ${tienda.envioKm2} km</td><td class="text-end">${pg:dinero(tienda.envioTarifa15)}</td></tr>
                    <tr><td>Hasta ${tienda.envioKm3} km</td><td class="text-end">${pg:dinero(tienda.envioTarifa20)}</td></tr>
                    <tr><td>Compras desde ${pg:dinero(tienda.envioGratisDesde)} (CDMX, hasta ${tienda.envioKm3} km)</td><td class="text-end">Gratis</td></tr>
                    <tr><td>Más de ${tienda.envioKm3} km o fuera de la CDMX</td><td class="text-end">${pg:dinero(tienda.envioTarifa20)}</td></tr>
                </table>
                <p class="small texto-gris mb-0">Las direcciones se ubican con OpenStreetMap. Si una no se encuentra, se cobra la tarifa máxima.</p>
            </div>
        </div>
    </div>
</t:admin>
