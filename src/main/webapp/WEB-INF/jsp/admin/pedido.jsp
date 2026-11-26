<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="Pedido ${pedido.folio}">
    <a href="${ctx}/admin/pedidos" class="small"><i class="bi bi-arrow-left me-1"></i>Pedidos</a>
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 my-3">
        <div>
            <span class="badge text-bg-${pedido.estado.color} fs-6">${pedido.estado.etiqueta}</span>
            <span class="small texto-gris ms-2">Creado ${pg:fechaHora(pedido.fechaHora)}<c:if test="${not empty pedido.formaPago}"> · ${pedido.formaPago.etiqueta}</c:if></span>
        </div>
        <a href="${ctx}/pedido/${pedido.folio}/ticket" target="_blank" class="btn btn-sm btn-outline-ciruela"><i class="bi bi-receipt me-1"></i>Ticket</a>
    </div>

    <div class="row g-4">
        <div class="col-xl-7">
            <!-- Actualizar estado -->
            <div class="tarjeta p-4 mb-4">
                <h2 class="h5 mb-3">Actualizar estado</h2>
                <p class="small texto-gris">Orden permitido: Pendiente → Preparando → En camino → Entregado. Cada cambio se guarda con fecha y hora, y se avisa al cliente.</p>
                <c:choose>
                    <c:when test="${pedido.estado.esFinal()}"><p class="mb-0">El pedido ya está <strong>${pedido.estado.etiqueta}</strong>.</p></c:when>
                    <c:otherwise>
                        <form action="${ctx}/admin/pedidos/${pedido.folio}/estado" method="post" class="row g-2">
                            <input type="hidden" name="_csrf" value="${csrfToken}">
                            <div class="col-md-5">
                                <label for="estado" class="form-label small">Nuevo estado</label>
                                <select id="estado" name="estado" class="form-select" onchange="document.querySelectorAll('[data-solo]').forEach(e => e.classList.toggle('d-none', e.dataset.solo !== this.value))">
                                    <c:if test="${not empty siguiente}"><option value="${siguiente}">${pedido.estado == 'PENDIENTE_PAGO' ? 'Confirmar pago (→ Pendiente)' : siguiente.etiqueta}</option></c:if>
                                    <option value="CANCELADO">Cancelar pedido</option>
                                </select>
                            </div>
                            <div class="col-md-7">
                                <label for="nota" class="form-label small">Nota <span class="texto-gris">(opcional)</span></label>
                                <input id="nota" name="nota" class="form-control" placeholder="Visible para el cliente en el historial">
                            </div>
                            <div class="col-md-6 ${siguiente == 'EN_CAMINO' ? '' : 'd-none'}" data-solo="EN_CAMINO">
                                <label for="repartidor" class="form-label small">Repartidor</label>
                                <input id="repartidor" name="repartidor" class="form-control" value="<c:out value='${empty pedido.empleado ? usuarioActual.nombre : pedido.empleado.nombre}'/>">
                            </div>
                            <div class="col-md-6 ${siguiente == 'EN_CAMINO' ? '' : 'd-none'}" data-solo="EN_CAMINO">
                                <label for="telefono" class="form-label small">Teléfono del repartidor</label>
                                <input id="telefono" name="telefono" class="form-control" value="<c:out value='${empty pedido.empleado ? usuarioActual.telefono : pedido.empleado.telefono}'/>">
                            </div>
                            <div class="col-md-6 ${siguiente == 'ENTREGADO' ? '' : 'd-none'}" data-solo="ENTREGADO">
                                <label for="recibio" class="form-label small">¿Quién recibió?</label>
                                <input id="recibio" name="recibio" class="form-control" value="<c:out value='${pedido.destinatario}'/>">
                            </div>
                            <div class="col-12"><button class="btn btn-ciruela px-4">Actualizar</button></div>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Envío -->
            <c:if test="${pedido.conEnvio}">
                <div class="tarjeta p-4 mb-4">
                    <h2 class="h5 mb-3"><i class="bi bi-truck me-2 texto-ciruela"></i>Envío ${pedido.numeroGuia}</h2>
                    <p class="small mb-2"><c:out value="${pedido.repartidor}"/><c:if test="${not empty pedido.telefonoRepartidor}"> · <c:out value="${pedido.telefonoRepartidor}"/></c:if>
                        · Llegada estimada ${pg:fechaHora(pedido.horaEstimada)}</p>
                    <c:if test="${pedido.estado == 'EN_CAMINO'}">
                        <button type="button" class="btn btn-sm btn-outline-ciruela" data-enviar-ubicacion="${pedido.numeroGuia}"><i class="bi bi-geo me-1"></i>Enviar mi ubicación al cliente</button>
                        <span class="small texto-gris ms-2" data-ubicacion-estado>
                            <c:if test="${pedido.ubicacionDefinida}">Última ubicación ${pg:hace(pedido.ubicacionActualizada)}</c:if></span>
                    </c:if>
                    <c:if test="${not empty pedido.recibio}"><p class="small mt-2 mb-0">Recibió: <strong><c:out value="${pedido.recibio}"/></strong> · ${pg:fechaHora(pedido.fechaEntregado)}${pedido.entregaVerificadaQR ? ' · verificado con QR' : ''}</p></c:if>
                </div>
            </c:if>

            <!-- Productos -->
            <div class="tarjeta p-4 mb-4">
                <h2 class="h5 mb-3">Productos (${pedido.totalPiezas})</h2>
                <c:forEach items="${pedido.items}" var="i">
                    <div class="d-flex gap-3 mb-3">
                        <img src="${ctx}${i.arregloFloral.imagenUrl}" alt="" class="rounded-3" style="width: 60px; height: 75px; object-fit: cover">
                        <div class="flex-grow-1 small">
                            <div class="fw-semibold">${i.cantidad} × <c:out value="${i.arregloFloral.nombre}"/> <span class="texto-gris">(${i.arregloFloral.sku})</span></div>
                            <c:if test="${not empty i.personalizacion}"><div><i class="bi bi-magic texto-ciruela me-1"></i><c:out value="${i.personalizacion}"/></div></c:if>
                            <c:if test="${not empty i.nota}"><div class="texto-gris"><i class="bi bi-chat-left-text me-1"></i><c:out value="${i.nota}"/></div></c:if>
                        </div>
                        <span class="small fw-bold">${pg:dinero(i.subtotal)}</span>
                    </div>
                </c:forEach>
                <hr class="borde-pg">
                <div class="d-flex justify-content-between small"><span>Subtotal</span><span>${pg:dinero(pedido.subtotal)}</span></div>
                <c:if test="${pedido.descuento > 0}"><div class="d-flex justify-content-between small"><span>Descuento <c:out value="${pedido.cupon.codigo}"/></span><span>−${pg:dinero(pedido.descuento)}</span></div></c:if>
                <div class="d-flex justify-content-between small"><span>Envío<c:if test="${not empty pedido.distanciaKm}"> (${pedido.distanciaKm} km en línea recta)</c:if></span><span>${pg:dinero(pedido.costoEnvio)}</span></div>
                <div class="d-flex justify-content-between fw-bold mt-1"><span>Total</span><span class="texto-ciruela">${pg:dinero(pedido.total)}</span></div>
            </div>
        </div>

        <div class="col-xl-5">
            <div class="tarjeta p-4 mb-4">
                <h2 class="h5 mb-3">Entrega</h2>
                <p class="small mb-1"><i class="bi bi-calendar-heart me-2 texto-ciruela"></i><strong>${pg:fechaLarga(pedido.fechaEntrega)}</strong><c:if test="${not empty pedido.horarioEntrega}">, ${pedido.horarioEntrega}</c:if></p>
                <p class="small mb-1"><i class="bi bi-person me-2 texto-ciruela"></i><c:out value="${pedido.destinatario}"/><c:if test="${not empty pedido.telefonoDestinatario}"> · <c:out value="${pedido.telefonoDestinatario}"/></c:if></p>
                <p class="small mb-1"><i class="bi bi-geo-alt me-2 texto-ciruela"></i><c:out value="${pedido.direccionEntrega}"/></p>
                <c:if test="${not empty pedido.instruccionesEspeciales}"><p class="small mb-1 texto-gris"><c:out value="${pedido.instruccionesEspeciales}"/></p></c:if>
                <a class="small" target="_blank" rel="noopener" href="https://www.google.com/maps/search/?api=1&query=${pg:url(pedido.direccionEntrega)}"><i class="bi bi-map me-1"></i>Abrir en mapa</a>
                <hr class="borde-pg">
                <h3 class="h6">Cliente</h3>
                <p class="small mb-0"><c:out value="${pedido.nombreCliente}"/><br><c:out value="${pedido.correoCliente}"/><br><c:out value="${pedido.telefonoCliente}"/></p>
            </div>

            <c:if test="${pedido.conDedicatoria}">
                <div class="tarjeta p-4 mb-4 d-flex gap-3 align-items-center">
                    <img src="${ctx}/qr/${pedido.tokenQr}.png" alt="QR" width="110" height="110">
                    <div class="small">
                        <strong>Dedicatoria con QR</strong>
                        <p class="texto-gris mb-1">“<c:out value="${pedido.dedicatoria}"/>”</p>
                        <a href="${ctx}/qr/${pedido.tokenQr}.png" download="qr-${pedido.folio}.png">Descargar para imprimir</a>
                    </div>
                </div>
            </c:if>

            <div class="tarjeta p-4 mb-4">
                <h2 class="h5 mb-1"><i class="bi bi-whatsapp me-2" style="color: #25d366"></i>Compartir con el equipo</h2>
                <p class="small texto-gris">Se manda solo lo necesario para preparar y entregar: fecha, quién recibe, dirección, productos y dedicatoria. Sin precios ni datos de pago.</p>
                <div class="d-flex flex-wrap gap-2 mb-2">
                    <c:forEach items="${compartirEquipo}" var="e">
                        <a href="${e.liga}" target="_blank" rel="noopener" class="btn btn-sm btn-whatsapp"><i class="bi bi-whatsapp me-1"></i><c:out value="${e.nombre}"/> <span class="opacity-75">(<c:out value="${e.rol}"/>)</span></a>
                    </c:forEach>
                    <a href="${compartirLibre}" target="_blank" rel="noopener" class="btn btn-sm btn-outline-secondary"><i class="bi bi-person-lines-fill me-1"></i>Elegir contacto</a>
                </div>
                <c:if test="${empty compartirEquipo}"><p class="small texto-gris mb-2">Agrega el teléfono del personal en sus cuentas para enviarlo directo.</p></c:if>
                <details class="small"><summary class="texto-gris">Ver el mensaje</summary><pre class="small bg-rosa-suave rounded-3 p-2 mt-2 mb-0" style="white-space: pre-wrap"><c:out value="${mensajeEquipo}"/></pre></details>
            </div>

            <c:if test="${usuarioActual.admin}">
                <div class="tarjeta p-4 mb-4">
                    <h2 class="h5 mb-3">Asignar a empleado o repartidor</h2>
                    <form action="${ctx}/admin/pedidos/${pedido.folio}/asignar" method="post" class="d-flex gap-2">
                        <input type="hidden" name="_csrf" value="${csrfToken}">
                        <select name="empleadoId" class="form-select" aria-label="Empleado">
                            <option value="">Sin asignar</option>
                            <c:forEach items="${empleados}" var="e"><option value="${e.idUsuario}" ${pedido.empleado.idUsuario == e.idUsuario ? 'selected' : ''}><c:out value="${e.nombre}"/> (<c:out value="${e.rol}"/>)</option></c:forEach>
                        </select>
                        <button class="btn btn-outline-ciruela btn-cuadrado">Asignar</button>
                    </form>
                </div>
            </c:if>

            <div class="tarjeta p-4 mb-4">
                <h2 class="h5 mb-3">Historial</h2>
                <ul class="historial list-unstyled small mb-0">
                    <c:forEach items="${pedido.historial}" var="h">
                        <li><strong><c:out value="${h.estado}"/></strong> · ${pg:fechaHora(h.fechaHora)} · <span class="texto-gris"><c:out value="${h.autor}"/></span>
                            <c:if test="${not empty h.nota}"><br><c:out value="${h.nota}"/></c:if></li>
                    </c:forEach>
                </ul>
            </div>

            <div class="tarjeta p-4 mb-4">
                <h2 class="h5 mb-3">Pago</h2>
                <div class="small d-flex justify-content-between">
                    <span><c:out value="${empty pedido.metodoPago ? '—' : pedido.metodoPago.tipoPago}"/> · <c:out value="${pedido.proveedorPago}"/>
                        <c:if test="${not empty pedido.metodoPago.descripcion}"><br><span class="texto-gris"><c:out value="${pedido.metodoPago.descripcion}"/></span></c:if>
                        <br><span class="font-monospace texto-gris"><c:out value="${pedido.transaccion}"/></span></span>
                    <span class="text-end">${pg:dinero(pedido.total)}<br><span class="badge ${pedido.pagado ? 'text-bg-success' : 'text-bg-warning'}">${pedido.estadoPago}</span></span>
                </div>
                <c:if test="${not empty pedido.factura}">
                    <hr class="borde-pg">
                    <p class="small mb-0"><i class="bi bi-file-earmark-text me-1 texto-ciruela"></i>Factura solicitada: <strong><c:out value="${pedido.factura.rfc}"/></strong> · <c:out value="${pedido.factura.razonSocial}"/><br>
                        <span class="texto-gris"><c:out value="${pedido.factura.cfdi}"/> · <c:out value="${pedido.factura.direccionFiscal}"/></span></p>
                </c:if>
            </div>

            <c:if test="${not pedido.estado.esFinal()}">
                <div class="tarjeta p-4 border-danger">
                    <h2 class="h5 mb-2 text-danger">Reportar un problema</h2>
                    <p class="small texto-gris">Se avisa al cliente y a los administradores.</p>
                    <form action="${ctx}/admin/pedidos/${pedido.folio}/fallo" method="post">
                        <input type="hidden" name="_csrf" value="${csrfToken}">
                        <textarea name="descripcion" rows="2" class="form-control mb-2" required placeholder="Ej. No hay nadie en el domicilio"></textarea>
                        <button class="btn btn-outline-danger btn-sm">Reportar falla</button>
                    </form>
                </div>
            </c:if>
        </div>
    </div>
</t:admin>
