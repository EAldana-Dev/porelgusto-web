<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="Generador de códigos QR">
    <div class="row g-4">
        <div class="col-xl-6">
            <form method="get" class="tarjeta p-4">
                <p class="small texto-gris">Genera códigos QR con los colores de la marca para tarjetas, folletos o el mostrador. Al escanearlos se abre el producto, la colección o la liga que elijas.</p>
                <div class="mb-3">
                    <label for="qrProducto" class="form-label">Producto</label>
                    <select id="qrProducto" class="form-select" data-qr-rapido>
                        <option value="">Elegir producto…</option>
                        <c:forEach items="${productos}" var="p"><option value="/producto/${p.sku}">${p.sku} · <c:out value="${p.nombre}"/></option></c:forEach>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="qrColeccion" class="form-label">Colección</label>
                    <select id="qrColeccion" class="form-select" data-qr-rapido>
                        <option value="">Elegir colección…</option>
                        <option value="/catalogo">Catálogo completo</option>
                        <c:forEach items="${colecciones}" var="col"><option value="/catalogo?coleccion=${col.slug}"><c:out value="${col.nombre}"/></option></c:forEach>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="qrTexto" class="form-label">Contenido del QR (liga o texto)</label>
                    <textarea id="qrTexto" name="texto" rows="3" class="form-control" required maxlength="1000"><c:out value="${texto}"/></textarea>
                </div>
                <div class="mb-4">
                    <label for="tamano" class="form-label">Tamaño (px)</label>
                    <select id="tamano" name="tamano" class="form-select">
                        <c:forEach items="${[240, 360, 600, 1000]}" var="s"><option value="${s}" ${s == tamano ? 'selected' : ''}>${s} × ${s}</option></c:forEach>
                    </select>
                </div>
                <button class="btn btn-ciruela w-100"><i class="bi bi-qr-code me-2"></i>Generar</button>
                <p class="small texto-gris mt-3 mb-0"><i class="bi bi-printer me-1"></i>¿Quieres una tarjeta con la foto del arreglo? Abre un producto en la tienda y usa «Tarjeta con QR para imprimir».</p>
            </form>
        </div>
        <div class="col-xl-6">
            <div class="tarjeta p-4 text-center h-100 d-flex flex-column align-items-center justify-content-center">
                <c:choose>
                    <c:when test="${not empty texto}">
                        <img src="${ctx}/admin/qr/imagen?texto=${pg:url(texto)}&tamano=${tamano}" alt="Código QR generado" class="img-fluid mb-3" style="max-width: 320px">
                        <p class="small texto-gris text-break"><c:out value="${texto}"/></p>
                        <a href="${ctx}/admin/qr/imagen?texto=${pg:url(texto)}&tamano=${tamano}&descargar=true" class="btn btn-outline-ciruela"><i class="bi bi-download me-2"></i>Descargar PNG</a>
                    </c:when>
                    <c:otherwise>
                        <i class="bi bi-qr-code display-1 texto-rosa"></i>
                        <p class="texto-gris mt-3 mb-0">Aquí verás la vista previa.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</t:admin>
