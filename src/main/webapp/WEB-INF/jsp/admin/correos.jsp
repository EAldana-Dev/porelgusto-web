<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:admin titulo="Correos enviados">
<div style="max-width: 900px">
    <p class="texto-gris">Los últimos 50 correos que mandó la tienda (pedidos, avisos, códigos de recuperación).
        <c:if test="${not envioReal}"><br><span class="text-danger"><i class="bi bi-exclamation-triangle me-1"></i>No hay servidor de correo configurado (MAIL_HOST): se guardan aquí pero no llegan a los clientes.</span></c:if></p>
    <c:if test="${empty correos}"><div class="tarjeta p-4 text-center texto-gris">Aún no se ha enviado ningún correo.</div></c:if>
    <div class="accordion" id="correos">
        <c:forEach items="${correos}" var="m" varStatus="s">
            <div class="accordion-item">
                <h2 class="accordion-header">
                    <button class="accordion-button ${s.first ? '' : 'collapsed'}" type="button" data-bs-toggle="collapse" data-bs-target="#c${s.index}">
                        <span class="me-3 small texto-gris text-nowrap">${pg:hace(m.fechaHora)}</span>
                        <span class="flex-grow-1"><strong><c:out value="${m.asunto}"/></strong><br><span class="small texto-gris">Para: <c:out value="${m.para}"/></span></span>
                    </button>
                </h2>
                <div id="c${s.index}" class="accordion-collapse collapse ${s.first ? 'show' : ''}" data-bs-parent="#correos">
                    <div class="accordion-body small" style="white-space: pre-line"><c:out value="${m.cuerpo}"/></div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</t:admin>
