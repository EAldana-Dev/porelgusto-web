<%@ tag description="Plantilla del sitio: menú, mensajes, contenido y pie de página" pageEncoding="UTF-8" %>
<%@ attribute name="titulo" required="true" %>
<%@ attribute name="descripcion" required="false" %>
<%@ attribute name="simple" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${titulo}"/> | Por el Gusto · Envío de flores</title>
    <meta name="description" content="${empty descripcion ? 'Flores frescas, arreglos y regalos con envío el mismo día.' : fn:escapeXml(descripcion)}">
    <link rel="icon" href="${ctx}/imagenes/logo.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,500;0,600;1,500&family=DM+Sans:wght@400;500;700&display=swap">
    <link rel="stylesheet" href="${ctx}/webjars/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${ctx}/css/styles.css">
</head>
<body data-base="${ctx}" class="d-flex flex-column min-vh-100">

<c:if test="${not simple}">
<div class="barra-aviso text-center py-2 px-3">
    <i class="bi bi-truck"></i> Envío gratis en compras desde ${pg:dinero(tienda.envioGratisDesde)} dentro de la CDMX
</div>

<!-- ==================== CABECERA ==================== -->
<header class="pg-navbar sticky-top">
    <nav class="navbar navbar-expand-lg py-2">
        <div class="container">
            <a class="navbar-brand d-flex flex-column lh-1 me-4" href="${ctx}/">
                <span class="logo-titulo">POR EL GUSTO</span>
                <span class="logo-subtitulo text-center">ENVÍO DE FLORES</span>
            </a>

            <div class="d-flex align-items-center gap-3 order-lg-last ms-auto ms-lg-0">
                <c:if test="${not empty usuarioActual}">
                    <a href="${ctx}/cuenta/notificaciones" class="icono-nav" aria-label="Notificaciones" data-bs-toggle="tooltip" title="Notificaciones">
                        <i class="bi bi-bell"></i>
                        <c:if test="${notifPendientes > 0}"><span class="contador">${notifPendientes}</span></c:if>
                    </a>
                    <a href="${ctx}/cuenta/deseos" class="icono-nav d-none d-sm-inline" aria-label="Lista de deseos" data-bs-toggle="tooltip" title="Lista de deseos"><i class="bi bi-heart"></i></a>
                </c:if>
                <div class="dropdown">
                    <a href="#" class="icono-nav" data-bs-toggle="dropdown" aria-expanded="false" aria-label="Mi cuenta"><i class="bi bi-person"></i></a>
                    <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0">
                        <c:choose>
                            <c:when test="${not empty usuarioActual}">
                                <li><h6 class="dropdown-header">Hola, <c:out value="${usuarioActual.primerNombre}"/></h6></li>
                                <c:if test="${usuarioActual.personal}">
                                    <li><a class="dropdown-item fw-semibold" href="${ctx}/admin"><i class="bi bi-speedometer2 me-2"></i>Panel de administración</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                </c:if>
                                <li><a class="dropdown-item" href="${ctx}/cuenta"><i class="bi bi-person-gear me-2"></i>Mi cuenta</a></li>
                                <li><a class="dropdown-item" href="${ctx}/cuenta/pedidos"><i class="bi bi-bag-check me-2"></i>Mis pedidos</a></li>
                                <li><a class="dropdown-item" href="${ctx}/cuenta/deseos"><i class="bi bi-heart me-2"></i>Lista de deseos</a></li>
                                <li><a class="dropdown-item" href="${ctx}/soporte"><i class="bi bi-life-preserver me-2"></i>Soporte</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <form action="${ctx}/logout" method="post">
                                        <input type="hidden" name="_csrf" value="${csrfToken}">
                                        <button class="dropdown-item text-danger"><i class="bi bi-box-arrow-right me-2"></i>Cerrar sesión</button>
                                    </form>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li><a class="dropdown-item" href="${ctx}/login"><i class="bi bi-box-arrow-in-right me-2"></i>Iniciar sesión</a></li>
                                <li><a class="dropdown-item" href="${ctx}/registro"><i class="bi bi-person-plus me-2"></i>Crear cuenta</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="${ctx}/seguimiento"><i class="bi bi-truck me-2"></i>Seguir mi pedido</a></li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
                <a href="${ctx}/carrito" class="icono-nav" aria-label="Carrito" data-bs-toggle="tooltip" title="Carrito">
                    <i class="bi bi-bag"></i>
                    <c:if test="${carritoCantidad > 0}"><span class="contador">${carritoCantidad}</span></c:if>
                </a>
                <button class="navbar-toggler border-0 px-1" type="button" data-bs-toggle="collapse" data-bs-target="#menu"
                        aria-controls="menu" aria-expanded="false" aria-label="Abrir menú">
                    <span class="navbar-toggler-icon"></span>
                </button>
            </div>

            <div class="collapse navbar-collapse" id="menu">
                <ul class="navbar-nav mx-auto">
                    <li class="nav-item"><a class="nav-link px-3 ${rutaActual == ctx.concat('/') ? 'active' : ''}" href="${ctx}/">Inicio</a></li>
                    <li class="nav-item dropdown">
                        <a class="nav-link px-3 dropdown-toggle ${fn:startsWith(rutaActual, ctx.concat('/catalogo')) ? 'active' : ''}" href="#" data-bs-toggle="dropdown" aria-expanded="false">Catálogo</a>
                        <ul class="dropdown-menu shadow-sm border-0">
                            <li><a class="dropdown-item fw-semibold" href="${ctx}/catalogo">Ver todo</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <c:forEach items="${categoriasMenu}" var="cat">
                                <li><a class="dropdown-item" href="${ctx}/catalogo?categoria=${cat.slug}"><c:out value="${cat.nombre}"/></a></li>
                            </c:forEach>
                        </ul>
                    </li>
                    <li class="nav-item"><a class="nav-link px-3" href="${ctx}/catalogo?ofertas=true">Ofertas</a></li>
                    <li class="nav-item"><a class="nav-link px-3 ${fn:startsWith(rutaActual, ctx.concat('/seguimiento')) ? 'active' : ''}" href="${ctx}/seguimiento">Seguimiento</a></li>
                    <li class="nav-item"><a class="nav-link px-3 ${fn:startsWith(rutaActual, ctx.concat('/soporte')) ? 'active' : ''}" href="${ctx}/soporte">Contacto</a></li>
                </ul>
                <form class="buscador my-2 my-lg-0 me-lg-3" role="search" action="${ctx}/catalogo" method="get" data-buscador autocomplete="off">
                    <label for="buscar" class="visually-hidden">Buscar</label>
                    <input type="search" id="buscar" name="q" class="form-control" placeholder="Buscar flores, ramos…" value="${fn:escapeXml(param.q)}">
                    <button class="btn-buscar" type="submit" aria-label="Buscar"><i class="bi bi-search"></i></button>
                </form>
            </div>
        </div>
    </nav>
</header>
</c:if>

<!-- ==================== MENSAJES ==================== -->
<c:if test="${not empty mensaje or not empty error or not empty avisos}">
    <div class="container mt-3">
        <c:if test="${not empty mensaje}">
            <div class="alert alert-success alert-dismissible fade show d-flex align-items-center gap-2" role="alert">
                <i class="bi bi-check-circle-fill"></i>
                <span class="flex-grow-1"><c:out value="${mensaje}"/></span>
                <c:if test="${agregadoAlCarrito}"><a href="${ctx}/carrito" class="btn btn-sm btn-ciruela ms-2">Ver carrito</a></c:if>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show d-flex align-items-center gap-2" role="alert">
                <i class="bi bi-exclamation-circle-fill"></i>
                <span class="flex-grow-1"><c:out value="${error}"/></span>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
            </div>
        </c:if>
        <c:forEach items="${avisos}" var="aviso">
            <div class="alert alert-info d-flex align-items-center gap-2" role="alert"><i class="bi bi-info-circle-fill"></i><c:out value="${aviso}"/></div>
        </c:forEach>
    </div>
</c:if>

<main class="flex-grow-1">
    <jsp:doBody/>
</main>

<c:if test="${not simple}">
<!-- ==================== PIE DE PÁGINA ==================== -->
<footer class="pg-footer mt-5 pt-5 pb-4">
    <div class="container">
        <div class="row g-4">
            <div class="col-lg-4">
                <div class="d-flex flex-column lh-1 mb-3">
                    <span class="logo-titulo">POR EL GUSTO</span>
                    <span class="logo-subtitulo">ENVÍO DE FLORES</span>
                </div>
                <p class="small texto-gris mb-0">Flores frescas seleccionadas cada día, arreglos únicos para cada ocasión y entrega rápida y segura en tu ciudad.</p>
            </div>
            <div class="col-6 col-lg-2">
                <h3 class="h6 etiqueta-seccion">Tienda</h3>
                <ul class="list-unstyled small d-grid gap-2">
                    <li><a href="${ctx}/catalogo">Catálogo</a></li>
                    <li><a href="${ctx}/catalogo?ofertas=true">Ofertas</a></li>
                    <li><a href="${ctx}/catalogo?orden=novedades">Novedades</a></li>
                </ul>
            </div>
            <div class="col-6 col-lg-2">
                <h3 class="h6 etiqueta-seccion">Ayuda</h3>
                <ul class="list-unstyled small d-grid gap-2">
                    <li><a href="${ctx}/seguimiento">Seguir mi pedido</a></li>
                    <li><a href="${ctx}/soporte">Soporte</a></li>
                    <li><a href="${ctx}/soporte#preguntas">Preguntas frecuentes</a></li>
                </ul>
            </div>
            <div class="col-lg-4">
                <h3 class="h6 etiqueta-seccion">Contacto</h3>
                <address class="small d-grid gap-2 mb-0">
                    <a href="https://wa.me/${tienda.whatsapp}" target="_blank" rel="noopener"><i class="bi bi-whatsapp me-2"></i>WhatsApp <c:out value="${tienda.telefono}"/></a>
                    <a href="tel:${fn:replace(tienda.telefono, ' ', '')}"><i class="bi bi-telephone me-2"></i><c:out value="${tienda.telefono}"/></a>
                    <a href="mailto:${tienda.correo}"><i class="bi bi-envelope me-2"></i><c:out value="${tienda.correo}"/></a>
                </address>
            </div>
        </div>
        <hr class="borde-pg my-4">
        <div class="d-flex flex-wrap justify-content-between gap-2 small texto-gris">
            <span>© Por el Gusto · Proyecto NeksByte 5IM9</span>
            <span><i class="bi bi-shield-lock me-1"></i>Pagos seguros con tarjeta o PayPal</span>
        </div>
    </div>
</footer>

<a href="https://wa.me/${tienda.whatsapp}" class="whatsapp-flotante no-imprimir" target="_blank" rel="noopener" aria-label="Escríbenos por WhatsApp"><i class="bi bi-whatsapp"></i></a>
</c:if>

<!-- Aviso de cookies (para "Recomendados para ti") -->
<c:if test="${not cookiesDecididas and not simple}">
    <div class="aviso-cookies no-imprimir">
        <p class="small mb-3"><i class="bi bi-cookie me-1 texto-ciruela"></i>
            Usamos cookies para recordar los productos y categorías que te gustan y mostrarte <strong>recomendaciones para ti</strong>. ¿Aceptas?</p>
        <form action="${ctx}/cookies" method="post" class="d-flex gap-2 justify-content-end">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button name="decision" value="rechazar" class="btn btn-sm btn-outline-secondary">Rechazar</button>
            <button name="decision" value="aceptar" class="btn btn-sm btn-ciruela px-3">Aceptar</button>
        </form>
    </div>
</c:if>

<script src="${ctx}/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${ctx}/js/app.js"></script>
</body>
</html>
