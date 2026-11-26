<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Carrito">
<div class="container py-4">
    <div class="pasos mb-4"><span class="activo">1. Carrito</span><span>›</span><span>2. Entrega y pago</span><span>›</span><span>3. Confirmación</span></div>
    <h1 class="display-5 mb-4">Tu carrito</h1>

    <c:choose>
    <c:when test="${carrito.vacio}">
        <div class="tarjeta text-center p-5">
            <i class="bi bi-bag display-4 texto-rosa"></i>
            <h2 class="mt-3">Tu carrito está vacío</h2>
            <p class="texto-gris">Agrega flores o arreglos para empezar tu pedido.</p>
            <a href="${ctx}/" class="btn btn-ciruela px-4">Ir al inicio</a>
            <a href="${ctx}/catalogo" class="btn btn-outline-ciruela px-4 ms-2">Ver catálogo</a>
        </div>
    </c:when>
    <c:otherwise>
    <div class="row g-4">
        <div class="col-lg-8">
            <!-- Productos -->
            <div class="tarjeta">
                <c:forEach items="${carrito.lineas}" var="i" varStatus="s">
                    <div class="linea-carrito d-flex gap-3 p-3 ${s.last ? '' : 'border-bottom borde-pg'} ${i.disponible ? '' : 'opacity-75'}">
                        <a href="${ctx}/producto/${i.sku}"><img src="${ctx}${i.imagenUrl}" alt="<c:out value='${i.nombre}'/>"></a>
                        <div class="flex-grow-1">
                            <div class="d-flex justify-content-between gap-2">
                                <div>
                                    <a href="${ctx}/producto/${i.sku}" class="titulo-serif fs-5 text-reset text-decoration-none"><c:out value="${i.nombre}"/></a>
                                    <div class="small texto-gris">${i.sku} · ${pg:dinero(i.precioUnitario)} c/u
                                        <c:if test="${i.conDescuento}"><span class="precio-antes ms-1">${pg:dinero(i.precioOriginal)}</span></c:if></div>
                                    <c:if test="${not empty i.nombresOpciones}"><div class="small mt-1"><i class="bi bi-magic texto-ciruela me-1"></i><c:out value="${i.opcionesTexto}"/></div></c:if>
                                    <c:if test="${not empty i.nota}"><div class="small mt-1 texto-gris"><i class="bi bi-chat-left-text me-1"></i><c:out value="${i.nota}"/></div></c:if>
                                    <c:if test="${not i.disponible}"><div class="small text-danger mt-1"><i class="bi bi-exclamation-triangle me-1"></i>No disponible en esta cantidad</div></c:if>
                                </div>
                                <span class="fw-bold text-nowrap">${pg:dinero(i.subtotal)}</span>
                            </div>
                            <div class="d-flex align-items-center gap-3 mt-2">
                                <form action="${ctx}/carrito/cantidad" method="post" class="input-group input-group-sm selector-cantidad" data-cantidad="auto">
                                    <input type="hidden" name="_csrf" value="${csrfToken}">
                                    <input type="hidden" name="idLinea" value="${i.idLinea}">
                                    <button type="button" class="btn btn-outline-secondary" data-paso="-1" aria-label="Menos">−</button>
                                    <input type="number" name="cantidad" value="${i.cantidad}" min="0" max="10" class="form-control text-center" aria-label="Cantidad" onchange="this.form.requestSubmit()">
                                    <button type="button" class="btn btn-outline-secondary" data-paso="1" aria-label="Más">+</button>
                                </form>
                                <form action="${ctx}/carrito/quitar" method="post">
                                    <input type="hidden" name="_csrf" value="${csrfToken}">
                                    <input type="hidden" name="idLinea" value="${i.idLinea}">
                                    <button class="btn btn-link btn-sm text-danger p-0"><i class="bi bi-trash me-1"></i>Eliminar</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Fecha y horario -->
            <div class="tarjeta p-3 p-lg-4 mt-4" id="entrega">
                <h2 class="h4 mb-1"><i class="bi bi-calendar-heart me-2 texto-ciruela"></i>Fecha y horario de entrega</h2>
                <c:choose>
                    <c:when test="${carrito.conEntrega}">
                        <p class="mb-3">Entregaremos el <strong>${pg:fechaLarga(carrito.fechaEntrega)}</strong> entre las <strong>${carrito.horarioEntrega}</strong>.</p>
                    </c:when>
                    <c:otherwise><p class="small texto-gris mb-3">Elige cuándo quieres que llegue. Hoy puedes pedir hasta 3 horas antes del horario. No entregamos en domingo.</p></c:otherwise>
                </c:choose>
                <form action="${ctx}/carrito/entrega" method="post" class="row g-2 align-items-end">
                    <input type="hidden" name="_csrf" value="${csrfToken}">
                    <div class="col-sm-5">
                        <label for="fecha" class="form-label small">Fecha</label>
                        <select id="fecha" name="fecha" class="form-select" required data-fecha-entrega="#horario" data-hoy="${hoy}">
                            <option value="">Elige una fecha</option>
                            <c:forEach items="${fechas}" var="f"><option value="${f}" ${f == carrito.fechaEntrega ? 'selected' : ''}>${pg:fechaLarga(f)}</option></c:forEach>
                        </select>
                    </div>
                    <div class="col-sm-4">
                        <label for="horario" class="form-label small">Horario</label>
                        <select id="horario" name="horario" class="form-select" required data-horarios-hoy="<c:forEach items='${horariosHoy}' var='h' varStatus='s'>${h}${s.last ? '' : '|'}</c:forEach>">
                            <option value="">Elige un horario</option>
                            <c:forEach items="${horarios}" var="h"><option value="${h}" ${h == carrito.horarioEntrega ? 'selected' : ''}>${h}</option></c:forEach>
                        </select>
                    </div>
                    <div class="col-sm-3"><button class="btn btn-outline-ciruela w-100">${carrito.conEntrega ? 'Cambiar' : 'Guardar'}</button></div>
                </form>
            </div>
            <a href="${ctx}/catalogo" class="btn btn-link mt-3 px-0"><i class="bi bi-arrow-left me-1"></i>Seguir comprando</a>
        </div>

        <!-- Resumen -->
        <div class="col-lg-4">
            <div class="tarjeta p-4 resumen-compra">
                <h2 class="h4 mb-3">Resumen</h2>
                <dl class="row small mb-0">
                    <dt class="col-7 fw-normal">Subtotal (${carrito.cantidad} productos)</dt><dd class="col-5 text-end">${pg:dinero(resumen.subtotal)}</dd>
                    <c:if test="${resumen.descuento > 0}">
                        <dt class="col-7 fw-normal texto-ciruela">Cupón <c:out value="${resumen.cupon}"/></dt><dd class="col-5 text-end texto-ciruela">−${pg:dinero(resumen.descuento)}</dd>
                    </c:if>
                    <dt class="col-7 fw-normal">Envío</dt><dd class="col-5 text-end">${not resumen.envioCalculado ? 'Al pagar' : resumen.envio.signum() == 0 ? 'Gratis' : pg:dinero(resumen.envio)}</dd>
                    <dt class="col-7 fw-normal texto-gris">IVA incluido (16%)</dt><dd class="col-5 text-end texto-gris">${pg:dinero(resumen.iva)}</dd>
                </dl>
                <hr class="borde-pg">
                <div class="d-flex justify-content-between align-items-baseline mb-3">
                    <span class="fw-semibold">Total${resumen.envioCalculado ? '' : ' sin envío'}</span><span class="fs-3 fw-bold texto-ciruela">${pg:dinero(resumen.total)}</span>
                </div>
                <c:if test="${not resumen.envioCalculado}">
                    <p class="small texto-gris mb-2"><i class="bi bi-geo-alt me-1"></i>El envío se calcula con la dirección de entrega: hasta ${tienda.envioKm1} km ${pg:dinero(tienda.envioTarifa10)}, hasta ${tienda.envioKm2} km ${pg:dinero(tienda.envioTarifa15)} y hasta ${tienda.envioKm3} km ${pg:dinero(tienda.envioTarifa20)}.</p>
                </c:if>
                <c:if test="${resumen.faltaParaEnvioGratis > 0}">
                    <p class="small bg-rosa-suave rounded-3 p-2"><i class="bi bi-truck me-1"></i>Te faltan <strong>${pg:dinero(resumen.faltaParaEnvioGratis)}</strong> para el envío gratis (CDMX, hasta ${tienda.envioKm3} km).</p>
                </c:if>
                <c:if test="${not empty resumen.cuponError}">
                    <p class="small text-danger"><i class="bi bi-exclamation-circle me-1"></i><c:out value="${resumen.cuponError}"/></p>
                </c:if>

                <!-- Cupón -->
                <c:choose>
                    <c:when test="${not empty resumen.cupon}">
                        <form action="${ctx}/carrito/cupon/quitar" method="post" class="d-flex justify-content-between align-items-center small mb-3">
                            <input type="hidden" name="_csrf" value="${csrfToken}">
                            <span><i class="bi bi-ticket-perforated texto-ciruela me-1"></i><c:out value="${resumen.cuponNombre}"/></span>
                            <button class="btn btn-link btn-sm text-danger p-0">Quitar</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <form action="${ctx}/carrito/cupon" method="post" class="input-group input-group-sm mb-3">
                            <input type="hidden" name="_csrf" value="${csrfToken}">
                            <input type="text" name="codigo" class="form-control" placeholder="Código de cupón" aria-label="Código de cupón" style="text-transform: uppercase">
                            <button class="btn btn-outline-ciruela btn-cuadrado">Aplicar</button>
                        </form>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${not todoDisponible}"><div class="alert alert-danger small py-2">Quita los productos no disponibles para continuar.</div></c:when>
                    <c:when test="${not carrito.conEntrega}"><div class="alert alert-info small py-2"><i class="bi bi-calendar me-1"></i>Elige la fecha y el horario de entrega para continuar.</div></c:when>
                </c:choose>
                <a href="${ctx}/checkout" class="btn btn-ciruela btn-lg w-100 mb-2 ${todoDisponible and carrito.conEntrega ? '' : 'disabled'}"><i class="bi bi-lock me-2"></i>Pagar</a>
                <a href="${ctx}/carrito/whatsapp" class="btn btn-whatsapp w-100 ${todoDisponible and carrito.conEntrega ? '' : 'disabled'}" target="_blank" rel="noopener"><i class="bi bi-whatsapp me-2"></i>Continuar por WhatsApp</a>
                <p class="small texto-gris text-center mt-3 mb-0"><i class="bi bi-shield-lock me-1"></i>Tarjeta o PayPal</p>
            </div>
        </div>
    </div>
    </c:otherwise>
    </c:choose>
</div>
</t:pagina>
