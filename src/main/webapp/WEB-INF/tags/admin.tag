<%@ tag description="Plantilla del panel de administración" pageEncoding="UTF-8" %>
<%@ attribute name="titulo" required="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="u" value="${usuarioActual}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${titulo}"/> | Panel · Por el Gusto</title>
    <link rel="icon" href="${ctx}/imagenes/logo.png">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,500;0,600;1,500&family=DM+Sans:wght@400;500;700&display=swap">
    <link rel="stylesheet" href="${ctx}/webjars/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${ctx}/css/styles.css">
</head>
<body data-base="${ctx}">
<div class="admin-layout d-lg-flex">
    <!-- Barra lateral: solo muestra los módulos permitidos para el rol -->
    <aside class="admin-sidebar p-3 d-flex flex-column">
        <a href="${ctx}/admin" class="d-flex flex-column lh-1 text-decoration-none px-2 my-3">
            <span class="logo-titulo">POR EL GUSTO</span>
            <span class="logo-subtitulo">PANEL · <c:out value="${u.rol}"/></span>
        </a>
        <nav class="nav flex-column gap-1 flex-grow-1">
            <a class="nav-link ${modulo == 'tablero' ? 'active' : ''}" href="${ctx}/admin"><i class="bi bi-speedometer2"></i>Tablero</a>
            <c:if test="${u.tienePermiso('PEDIDOS')}"><a class="nav-link ${modulo == 'pedidos' ? 'active' : ''}" href="${ctx}/admin/pedidos"><i class="bi bi-receipt"></i>Pedidos</a></c:if>
            <c:if test="${u.tienePermiso('PRODUCTOS')}"><a class="nav-link ${modulo == 'productos' ? 'active' : ''}" href="${ctx}/admin/productos"><i class="bi bi-flower1"></i>Productos e inventario</a></c:if>
            <c:if test="${u.tienePermiso('CATALOGO')}"><a class="nav-link ${modulo == 'catalogo' ? 'active' : ''}" href="${ctx}/admin/catalogo"><i class="bi bi-diagram-3"></i>Configuración del catálogo</a></c:if>
            <c:if test="${u.tienePermiso('PROMOCIONES')}"><a class="nav-link ${modulo == 'promociones' ? 'active' : ''}" href="${ctx}/admin/promociones"><i class="bi bi-ticket-perforated"></i>Promociones y cupones</a></c:if>
            <c:if test="${u.tienePermiso('SOPORTE')}"><a class="nav-link ${modulo == 'soporte' ? 'active' : ''}" href="${ctx}/admin/soporte"><i class="bi bi-life-preserver"></i>Soporte</a></c:if>
            <c:if test="${u.tienePermiso('USUARIOS')}"><a class="nav-link ${modulo == 'usuarios' ? 'active' : ''}" href="${ctx}/admin/usuarios"><i class="bi bi-people"></i>Usuarios y roles</a></c:if>
            <c:if test="${u.tienePermiso('QR')}"><a class="nav-link ${modulo == 'qr' ? 'active' : ''}" href="${ctx}/admin/qr"><i class="bi bi-qr-code"></i>Generador de QR</a></c:if>
            <c:if test="${u.tienePermiso('ESTADISTICAS')}"><a class="nav-link ${modulo == 'estadisticas' ? 'active' : ''}" href="${ctx}/admin/estadisticas"><i class="bi bi-graph-up"></i>Estadísticas del sitio</a></c:if>
            <c:if test="${u.tienePermiso('CONFIGURACION')}"><a class="nav-link ${modulo == 'configuracion' ? 'active' : ''}" href="${ctx}/admin/configuracion"><i class="bi bi-gear"></i>Pagos y envíos</a></c:if>
            <hr class="border-light opacity-25">
            <a class="nav-link" href="${ctx}/" target="_blank"><i class="bi bi-shop"></i>Ver tienda</a>
            <c:if test="${u.tienePermiso('CONFIGURACION')}"><a class="nav-link ${modulo == 'correos' ? 'active' : ''}" href="${ctx}/admin/correos"><i class="bi bi-envelope-paper"></i>Correos enviados</a></c:if>
            <c:if test="${u.tienePermiso('CONFIGURACION')}"><a class="nav-link" href="${ctx}/swagger-ui.html" target="_blank"><i class="bi bi-braces"></i>API (Swagger)</a></c:if>
        </nav>
        <div class="small px-2 mt-3">
            <div class="fw-semibold"><c:out value="${u.nombre}"/></div>
            <form action="${ctx}/logout" method="post">
                <input type="hidden" name="_csrf" value="${csrfToken}">
                <button class="btn btn-link btn-sm text-white-50 p-0">Cerrar sesión</button>
            </form>
        </div>
    </aside>

    <div class="admin-contenido">
        <header class="d-flex align-items-center justify-content-between border-bottom borde-pg bg-white px-4 py-3">
            <h1 class="h3 mb-0"><c:out value="${titulo}"/></h1>
            <a href="${ctx}/cuenta/notificaciones" class="icono-nav" aria-label="Notificaciones">
                <i class="bi bi-bell"></i><c:if test="${notifPendientes > 0}"><span class="contador">${notifPendientes}</span></c:if>
            </a>
        </header>
        <div class="p-4">
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success alert-dismissible fade show" role="alert"><i class="bi bi-check-circle-fill me-2"></i><c:out value="${mensaje}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button></div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert"><i class="bi bi-exclamation-circle-fill me-2"></i><c:out value="${error}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button></div>
            </c:if>
            <jsp:doBody/>
        </div>
    </div>
</div>
<script src="${ctx}/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${ctx}/js/app.js"></script>
</body>
</html>
