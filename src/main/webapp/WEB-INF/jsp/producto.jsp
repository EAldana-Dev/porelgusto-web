<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="${p.nombre}" descripcion="${p.detalles}">
<div class="container py-4">
    <nav aria-label="Ruta"><ol class="breadcrumb small">
        <li class="breadcrumb-item"><a href="${ctx}/">Inicio</a></li>
        <li class="breadcrumb-item"><a href="${ctx}/catalogo?categoria=${p.categoriaSlug}"><c:out value="${p.categoria}"/></a></li>
        <li class="breadcrumb-item active"><c:out value="${p.nombre}"/></li>
    </ol></nav>

    <c:if test="${not p.activo}"><div class="alert alert-warning small"><i class="bi bi-eye-slash me-1"></i>Este producto está oculto del catálogo (solo lo ve el personal).</div></c:if>

    <div class="row g-4 g-lg-5">
        <!-- Galería -->
        <div class="col-lg-6">
            <div class="ficha-foto position-relative">
                <div class="insignias">
                    <c:if test="${v.conDescuento}"><span class="insignia insignia-oferta">-<c:out value="${v.promocion.descuentoTexto}"/> · <c:out value="${v.promocion.nombre}"/></span></c:if>
                    <c:if test="${p.nuevo}"><span class="insignia insignia-nuevo">Nuevo</span></c:if>
                </div>
                <img src="${ctx}${p.imagenUrl}" alt="<c:out value='${p.nombre}'/>" width="720" height="960">
            </div>
            <c:if test="${fn:length(p.imagenes) > 1}">
                <div class="d-flex gap-2 mt-2">
                    <c:forEach items="${p.imagenes}" var="img">
                        <img src="${ctx}${img.direccionImg}" alt="" class="rounded-3 border borde-pg" style="width: 72px; height: 90px; object-fit: cover">
                    </c:forEach>
                </div>
            </c:if>
        </div>

        <!-- Detalle -->
        <div class="col-lg-6">
            <p class="etiqueta-seccion mb-2"><c:out value="${p.categoria}"/> · ${p.sku}</p>
            <h1 class="display-5 mb-2"><c:out value="${p.nombre}"/></h1>
            <div class="d-flex align-items-baseline gap-3 mb-3">
                <span class="precio-grande">${pg:dinero(v.precioFinal)}</span>
                <c:if test="${v.conDescuento}"><span class="precio-antes fs-5">${pg:dinero(p.costo)}</span></c:if>
            </div>
            <c:choose>
                <c:when test="${p.disponible}">
                    <p class="small mb-3" style="color: var(--pg-exito)"><i class="bi bi-check-circle-fill me-1"></i>Disponible
                        <c:if test="${p.bajoInventario}"> · ¡Quedan solo ${p.existencias}!</c:if></p>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-danger py-2 small"><i class="bi bi-x-circle-fill me-1"></i><strong>No disponible.</strong> Guárdalo en tu lista de deseos y te avisamos por correo cuando vuelva.</div>
                </c:otherwise>
            </c:choose>
            <p class="mb-3"><c:out value="${p.detalles}"/></p>
            <div class="d-flex flex-wrap gap-2 mb-4">
                <c:forEach items="${p.etiquetas}" var="et"><a href="${ctx}/catalogo?etiqueta=${pg:url(et)}" class="chip-cat small py-1"><c:out value="${et}"/></a></c:forEach>
                <c:forEach items="${p.colecciones}" var="col"><a href="${ctx}/catalogo?coleccion=${col.slug}" class="chip-cat small py-1"><i class="bi bi-stars me-1"></i><c:out value="${col.nombre}"/></a></c:forEach>
            </div>

            <c:if test="${p.disponible}">
            <form action="${ctx}/carrito/agregar" method="post" class="tarjeta p-3 p-lg-4">
                <input type="hidden" name="_csrf" value="${csrfToken}">
                <input type="hidden" name="productoId" value="${p.idArregloFloral}">
                <input type="hidden" name="sku" value="${p.sku}">

                <!-- Personalización -->
                <c:if test="${not empty opciones}">
                    <h2 class="h5 mb-1"><i class="bi bi-magic me-2 texto-ciruela"></i>Personaliza tu arreglo</h2>
                    <p class="small texto-gris mb-3">Cada opción muestra su costo adicional.</p>
                    <div class="d-grid gap-2 mb-4">
                        <c:forEach items="${opciones}" var="o">
                            <label class="opcion-check ${o.disponible ? '' : 'no-disponible'}">
                                <input class="form-check-input mt-0" type="checkbox" name="opciones" value="${o.idModificacion}" data-costo="${o.costo}" ${o.disponible ? '' : 'disabled'}>
                                <span class="flex-grow-1">
                                    <span class="d-block fw-semibold small"><c:out value="${o.nombre}"/></span>
                                    <span class="d-block small texto-gris"><c:out value="${o.disponible ? o.descripcion : 'No disponible por ahora'}"/></span>
                                </span>
                                <span class="small fw-bold texto-ciruela">+${pg:dinero(o.costo)}</span>
                            </label>
                        </c:forEach>
                    </div>
                </c:if>

                <div class="mb-3">
                    <label for="nota" class="form-label">Nota para el florista <span class="texto-gris fw-normal">(opcional)</span></label>
                    <input type="text" id="nota" name="nota" maxlength="300" class="form-control" placeholder="Ej. sin lirios, colores pastel…">
                </div>

                <!-- Fecha y horario de entrega -->
                <div class="row g-2 mb-3">
                    <div class="col-sm-6">
                        <label for="fecha" class="form-label">Fecha de entrega</label>
                        <select id="fecha" name="fecha" class="form-select" data-fecha-entrega="#horario" data-hoy="${hoy}">
                            <option value="">Elegir después</option>
                            <c:forEach items="${fechas}" var="f"><option value="${f}">${pg:fechaLarga(f)}</option></c:forEach>
                        </select>
                    </div>
                    <div class="col-sm-6">
                        <label for="horario" class="form-label">Horario</label>
                        <select id="horario" name="horario" class="form-select" data-horarios-hoy="<c:forEach items='${horariosHoy}' var='h' varStatus='s'>${h}${s.last ? '' : '|'}</c:forEach>">
                            <option value="">Elegir después</option>
                            <c:forEach items="${horarios}" var="h"><option value="${h}">${h}</option></c:forEach>
                        </select>
                    </div>
                </div>

                <div class="d-flex flex-wrap align-items-center gap-3 mb-3">
                    <div class="input-group selector-cantidad" data-cantidad>
                        <button type="button" class="btn btn-outline-secondary" data-paso="-1" aria-label="Menos">−</button>
                        <input type="number" name="cantidad" value="1" min="1" max="${p.existencias < 10 ? p.existencias : 10}" class="form-control text-center" aria-label="Cantidad">
                        <button type="button" class="btn btn-outline-secondary" data-paso="1" aria-label="Más">+</button>
                    </div>
                    <div>
                        <span class="small texto-gris d-block">Total</span>
                        <span class="fs-4 fw-bold texto-ciruela" data-precio-total data-precio-base="${v.precioFinal}">${pg:dinero(v.precioFinal)}</span>
                    </div>
                </div>

                <div class="d-grid d-sm-flex gap-2">
                    <button class="btn btn-ciruela btn-lg flex-grow-1"><i class="bi bi-bag-plus me-2"></i>Agregar al carrito</button>
                    <button class="btn btn-outline-ciruela btn-lg" name="comprarAhora" value="true">Comprar ahora</button>
                </div>
            </form>
            </c:if>

            <div class="d-flex flex-wrap align-items-center gap-3 mt-3">
                <form action="${ctx}/deseos/${p.sku}" method="post">
                    <input type="hidden" name="_csrf" value="${csrfToken}">
                    <input type="hidden" name="accion" value="${enDeseos ? 'quitar' : 'agregar'}">
                    <button class="btn ${p.disponible ? 'btn-link' : 'btn-ciruela'} px-0 px-sm-3">
                        <i class="bi ${enDeseos ? 'bi-heart-fill' : 'bi-heart'} me-1"></i>${enDeseos ? 'En tu lista de deseos' : 'Agregar a mi lista de deseos'}
                    </button>
                </form>
                <a class="btn btn-link px-0" href="https://wa.me/${tienda.whatsapp}?text=${pg:url('Hola, me interesa el arreglo '.concat(p.nombre).concat(' (').concat(p.sku).concat(')'))}" target="_blank" rel="noopener">
                    <i class="bi bi-whatsapp me-1"></i>Preguntar por WhatsApp</a>
                <button class="btn btn-link px-0" type="button" data-bs-toggle="collapse" data-bs-target="#qrProducto"><i class="bi bi-qr-code me-1"></i>Código QR</button>
            </div>
            <div class="collapse" id="qrProducto">
                <div class="tarjeta p-3 d-flex align-items-center gap-3 mt-2">
                    <img src="${ctx}/producto/${p.sku}/qr.png" alt="Código QR del producto" width="120" height="120">
                    <div class="small">
                        <p class="texto-gris mb-2">Escanéalo con la cámara de tu celular para abrir este arreglo directamente. Ideal para compartir.</p>
                        <a href="${ctx}/producto/${p.sku}/tarjeta-qr" target="_blank"><i class="bi bi-printer me-1"></i>Tarjeta con QR para imprimir o guardar en PDF</a>
                    </div>
                </div>
            </div>

            <ul class="list-unstyled small texto-gris mt-4 d-grid gap-2">
                <li><i class="bi bi-truck me-2 texto-ciruela"></i>Envío desde ${pg:dinero(tienda.envioTarifa10)} según la distancia; gratis desde ${pg:dinero(tienda.envioGratisDesde)} en la CDMX (hasta ${tienda.envioKm3} km). Entrega el mismo día si pides 3 horas antes.</li>
                <li><i class="bi bi-qr-code me-2 texto-ciruela"></i>Agrega una dedicatoria al pagar: quien recibe la ve al escanear el QR.</li>
                <li><i class="bi bi-shield-check me-2 texto-ciruela"></i>Pago seguro con tarjeta o PayPal.</li>
            </ul>
        </div>
    </div>

    <c:if test="${not empty relacionados}">
        <section class="mt-5 pt-4">
            <h2 class="display-6 mb-4">También te puede gustar</h2>
            <div class="row row-cols-2 row-cols-lg-4 g-3 g-lg-4">
                <c:forEach items="${relacionados}" var="r"><div class="col"><t:producto v="${r}"/></div></c:forEach>
            </div>
        </section>
    </c:if>
</div>
</t:pagina>
