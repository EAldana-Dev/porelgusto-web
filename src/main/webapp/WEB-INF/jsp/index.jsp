<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Flores que dicen lo que sientes">

    <!-- ==================== HERO ==================== -->
    <section class="container py-4 py-lg-5">
        <div class="row g-4 align-items-center">
            <div class="col-lg-5">
                <h1 class="hero-titulo mb-4">Flores<br>que dicen<br><em>lo que sientes</em></h1>
                <p class="texto-gris mb-4">Arreglos hechos a mano con flores frescas, listos para llegar hoy mismo.</p>
                <div class="d-flex flex-wrap gap-2 mb-5">
                    <a href="${ctx}/catalogo" class="btn btn-ciruela px-4">Ver catálogo</a>
                    <a href="${ctx}/seguimiento" class="btn btn-outline-ciruela px-4">Seguir mi pedido</a>
                </div>
                <div class="row small g-3">
                    <div class="col">
                        <h2 class="beneficio-titulo text-uppercase fw-bold">Flores frescas</h2>
                        <p class="mb-0">Seleccionadas cada día para ti</p>
                    </div>
                    <div class="col border-start borde-pg">
                        <h2 class="beneficio-titulo text-uppercase fw-bold">Envío el mismo día</h2>
                        <p class="mb-0">Entrega rápida y segura en tu ciudad</p>
                    </div>
                    <div class="col border-start borde-pg">
                        <h2 class="beneficio-titulo text-uppercase fw-bold">Diseños exclusivos</h2>
                        <p class="mb-0">Arreglos únicos para cada ocasión</p>
                    </div>
                </div>
            </div>

            <div class="col-md-5 col-lg-3">
                <c:choose>
                    <c:when test="${not empty ofertaDestacada}">
                        <div class="card tarjeta-promo border-0 rounded-4 p-4">
                            <p class="small text-uppercase letras-separadas texto-rosa fw-bold mb-1"><c:out value="${ofertaDestacada.tipo.etiqueta}"/></p>
                            <h2 class="promo-titulo"><c:out value="${ofertaDestacada.nombre}"/></h2>
                            <hr class="linea-corta my-3">
                            <p class="fs-5 mb-1"><strong><c:out value="${ofertaDestacada.descuentoTexto}"/></strong> de descuento</p>
                            <p class="small texto-gris mb-4">Hasta el ${pg:fecha(ofertaDestacada.fechaFin)}</p>
                            <a href="${ctx}/catalogo?${empty ofertaDestacada.categoria ? 'ofertas=true' : (ofertaDestacada.categoria.tipo == 'CATEGORIA' ? 'categoria=' : 'coleccion=').concat(ofertaDestacada.categoria.slug)}"
                               class="btn btn-ciruela text-uppercase fw-bold letras-separadas py-2">Comprar</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="card tarjeta-promo border-0 rounded-4 p-4">
                            <p class="small text-uppercase letras-separadas texto-rosa fw-bold mb-1">Nuevo</p>
                            <h2 class="promo-titulo">Novedades</h2>
                            <hr class="linea-corta my-3">
                            <p class="fs-5 mb-4">Descubre los arreglos recién llegados</p>
                            <a href="${ctx}/catalogo?orden=novedades" class="btn btn-ciruela text-uppercase fw-bold letras-separadas py-2">Ver</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="col-md-7 col-lg-4">
                <div class="hero-foto rounded-4" role="img" aria-label="Ramo de flores rosas"></div>
            </div>
        </div>
    </section>

    <!-- ==================== MÁS VENDIDOS ==================== -->
    <section class="container pb-4" aria-labelledby="titulo-vendidos">
        <div class="separador d-flex align-items-center gap-3 my-4">
            <hr class="flex-grow-1">
            <h2 id="titulo-vendidos" class="small text-uppercase letras-separadas texto-ciruela fw-bold m-0 font-sans">Más vendidos</h2>
            <hr class="flex-grow-1">
        </div>
        <div class="row row-cols-1 row-cols-md-3 g-4">
            <c:forEach items="${masVendidos}" var="v">
                <article class="col">
                    <a href="${ctx}/producto/${v.producto.sku}" class="producto-mini d-flex align-items-center gap-4 text-decoration-none text-reset justify-content-md-center">
                        <img src="${ctx}${v.producto.imagenUrl}" alt="<c:out value='${v.producto.nombre}'/>" loading="lazy">
                        <div>
                            <h3 class="fs-5 fw-bold mb-1" style="font-family: var(--pg-sans)"><c:out value="${v.producto.nombre}"/></h3>
                            <p class="small texto-gris mb-3"><c:out value="${v.producto.categoria}"/></p>
                            <p class="fs-5 texto-ciruela mb-0">${pg:dinero(v.precioFinal)}</p>
                        </div>
                    </a>
                </article>
            </c:forEach>
        </div>
    </section>

    <!-- ==================== RECOMENDADOS ==================== -->
    <section class="container py-5" aria-labelledby="titulo-recomendados">
        <div class="d-flex flex-wrap align-items-end justify-content-between gap-2 mb-4">
            <div>
                <p class="etiqueta-seccion mb-1">${personalizado ? 'Según lo que te gusta' : 'Temporada, novedades y lo más pedido'}</p>
                <h2 id="titulo-recomendados" class="display-6 mb-0">Recomendados para ti</h2>
            </div>
            <a href="${ctx}/catalogo" class="btn btn-outline-ciruela btn-sm">Ver todo el catálogo</a>
        </div>
        <div class="row row-cols-2 row-cols-md-3 row-cols-lg-4 g-3 g-lg-4">
            <c:forEach items="${recomendados}" var="v">
                <div class="col"><t:producto v="${v}"/></div>
            </c:forEach>
        </div>
    </section>

    <!-- ==================== COLECCIONES ==================== -->
    <c:if test="${not empty colecciones}">
        <section class="container py-4" aria-labelledby="titulo-colecciones">
            <p class="etiqueta-seccion mb-1">Para cada ocasión</p>
            <h2 id="titulo-colecciones" class="display-6 mb-4">Colecciones</h2>
            <div class="row row-cols-2 row-cols-lg-4 g-3">
                <c:forEach items="${colecciones}" var="col">
                    <c:set var="portada" value="${portadas['col-'.concat(col.slug)]}"/>
                    <div class="col">
                        <a href="${ctx}/catalogo?coleccion=${col.slug}" class="coleccion-card">
                            <img src="${ctx}${empty portada ? '/img/productos/PG-014.webp' : portada}" alt="" loading="lazy">
                            <div class="texto">
                                <h3><c:out value="${col.nombre}"/></h3>
                                <p class="small mb-0 opacity-75"><c:out value="${col.descripcion}"/></p>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </section>
    </c:if>

    <!-- ==================== CATEGORÍAS ==================== -->
    <section class="container py-5" aria-labelledby="titulo-categorias">
        <h2 id="titulo-categorias" class="display-6 mb-4 text-center">Explora por categoría</h2>
        <div class="d-flex flex-wrap justify-content-center gap-4">
            <c:forEach items="${categoriasMenu}" var="cat">
                <c:set var="portada" value="${portadas[cat.slug]}"/>
                <div style="width: 130px">
                    <a href="${ctx}/catalogo?categoria=${cat.slug}" class="text-decoration-none text-reset d-block text-center">
                        <img src="${ctx}${empty portada ? '/imagenes/logo.png' : portada}" alt="" class="rounded-circle mb-2 border borde-pg" style="width: 120px; height: 120px; object-fit: cover" loading="lazy">
                        <span class="d-block fw-semibold"><c:out value="${cat.nombre}"/></span>
                    </a>
                </div>
            </c:forEach>
        </div>
    </section>

    <!-- ==================== CÓMO FUNCIONA ==================== -->
    <section class="container py-4">
        <div class="tarjeta p-4 p-lg-5 bg-rosa-suave">
            <div class="row g-4 text-center">
                <div class="col-md-3"><i class="bi bi-flower1 fs-1 texto-ciruela"></i><h3 class="h5 mt-2">1. Elige</h3><p class="small texto-gris mb-0">Personaliza tu arreglo con tarjeta, moño o globo.</p></div>
                <div class="col-md-3"><i class="bi bi-calendar-heart fs-1 texto-ciruela"></i><h3 class="h5 mt-2">2. Programa</h3><p class="small texto-gris mb-0">Escoge el día y el horario de entrega.</p></div>
                <div class="col-md-3"><i class="bi bi-credit-card fs-1 texto-ciruela"></i><h3 class="h5 mt-2">3. Paga</h3><p class="small texto-gris mb-0">Tarjeta, PayPal o por WhatsApp.</p></div>
                <div class="col-md-3"><i class="bi bi-qr-code fs-1 texto-ciruela"></i><h3 class="h5 mt-2">4. Sorprende</h3><p class="small texto-gris mb-0">Agrega una dedicatoria con código QR.</p></div>
            </div>
        </div>
    </section>
</t:pagina>
