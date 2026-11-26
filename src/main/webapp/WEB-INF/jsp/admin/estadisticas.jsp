<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="Estadísticas del sitio">
    <div class="d-flex flex-wrap gap-2 mb-4">
        <c:forEach items="${[1, 7, 30, 90]}" var="d">
            <a href="${ctx}/admin/estadisticas?dias=${d}" class="chip-cat ${r.dias == d ? 'activo' : ''}"><c:choose><c:when test="${d == 1}">Hoy</c:when><c:otherwise>Últimos ${d} días</c:otherwise></c:choose></a>
        </c:forEach>
    </div>

    <div class="row g-3 mb-4">
        <div class="col-6 col-xl"><div class="dato-card"><div class="small texto-gris">Visitantes</div><div class="numero">${r.visitantes}</div></div></div>
        <div class="col-6 col-xl"><div class="dato-card"><div class="small texto-gris">Páginas vistas</div><div class="numero">${r.vistas}</div></div></div>
        <div class="col-6 col-xl"><div class="dato-card"><div class="small texto-gris">Pedidos</div><div class="numero">${r.pedidos}</div></div></div>
        <div class="col-6 col-xl"><div class="dato-card"><div class="small texto-gris">Ventas</div><div class="numero">${pg:dinero(r.ventas)}</div></div></div>
        <div class="col-6 col-xl"><div class="dato-card"><div class="small texto-gris">Visitantes que compran</div><div class="numero">${r.conversion}</div></div></div>
    </div>

    <c:if test="${r.dias > 1}">
        <div class="tarjeta p-4 mb-4">
            <h2 class="h5 mb-3">Visitas por día</h2>
            <div class="d-flex align-items-end gap-1" style="height: 160px" role="img" aria-label="Gráfica de visitas por día">
                <c:forEach items="${r.porDia}" var="d">
                    <div class="flex-fill d-flex flex-column justify-content-end h-100" title="${pg:fecha(d.dia)}: ${d.visitantes} visitantes, ${d.vistas} páginas">
                        <div class="rounded-top" style="height: ${d.porcentaje}%; min-height: ${d.vistas > 0 ? 3 : 1}px; background: var(--pg-ciruela); opacity: ${d.vistas > 0 ? 1 : .2}"></div>
                    </div>
                </c:forEach>
            </div>
            <div class="d-flex justify-content-between small texto-gris mt-1"><span>${pg:fecha(r.porDia[0].dia)}</span><span>Hoy</span></div>
        </div>
    </c:if>

    <div class="row g-4">
        <c:set var="bloques" value="${[['¿De dónde llegan?', r.origenes, 'bi-signpost-split'], ['País', r.paises, 'bi-globe-americas'], ['Ciudad', r.ciudades, 'bi-geo-alt'], ['Páginas más vistas', r.paginas, 'bi-file-earmark-text'], ['Dispositivo', r.dispositivos, 'bi-phone'], ['Navegador', r.navegadores, 'bi-browser-chrome']]}"/>
        <c:forEach items="${bloques}" var="b">
            <div class="col-md-6 col-xl-4">
                <div class="tarjeta p-4 h-100">
                    <h2 class="h6 mb-3"><i class="bi ${b[2]} me-2 texto-ciruela"></i>${b[0]}</h2>
                    <c:if test="${empty b[1]}"><p class="small texto-gris mb-0">Sin datos en este periodo.</p></c:if>
                    <c:forEach items="${b[1]}" var="x">
                        <div class="small mb-2">
                            <div class="d-flex justify-content-between"><span class="text-truncate me-2"><c:out value="${x.nombre}"/></span><span class="fw-semibold">${x.total}</span></div>
                            <div class="progress" style="height: 5px"><div class="progress-bar" style="width: ${x.porcentaje}%; background: var(--pg-ciruela)"></div></div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${not empty r.campanas}">
        <div class="tarjeta p-4 mt-4">
            <h2 class="h6 mb-3"><i class="bi bi-megaphone me-2 texto-ciruela"></i>Campañas (utm_campaign)</h2>
            <c:forEach items="${r.campanas}" var="x"><span class="badge text-bg-light me-1"><c:out value="${x.nombre}"/> · ${x.total}</span></c:forEach>
        </div>
    </c:if>

    <p class="small texto-gris mt-4 mb-0">
        No se guardan direcciones IP: cada visitante se cuenta con una huella anónima que cambia cada día. «Directo» significa que escribió la dirección o llegó desde una app sin enlace.
        Para medir campañas agrega <code>?utm_source=instagram&amp;utm_campaign=madres</code> a tus ligas.
        <c:choose>
            <c:when test="${ubicacionDisponible}">Ubicación aproximada: <a href="https://db-ip.com" target="_blank" rel="noopener">IP Geolocation by DB-IP</a>.</c:when>
            <c:otherwise><br><i class="bi bi-info-circle me-1"></i>Para ver país y ciudad, descarga la base gratuita con <code>scripts/descargar-geoip.sh</code> y reinicia la aplicación.</c:otherwise>
        </c:choose>
    </p>
</t:admin>
