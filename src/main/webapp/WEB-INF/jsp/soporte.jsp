<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:pagina titulo="Soporte y contacto">
<div class="container py-4">
    <div class="text-center mb-5">
        <p class="etiqueta-seccion mb-1">Estamos para ayudarte</p>
        <h1 class="display-5">Soporte y contacto</h1>
    </div>

    <!-- Medios de contacto -->
    <div class="row g-3 mb-5">
        <div class="col-md-4">
            <a href="https://wa.me/${tienda.whatsapp}" target="_blank" rel="noopener" class="tarjeta p-4 d-block text-center text-decoration-none text-reset h-100">
                <i class="bi bi-whatsapp display-5" style="color: #25d366"></i>
                <h2 class="h5 mt-2">WhatsApp</h2>
                <p class="small texto-gris mb-1">${tienda.whatsappDesde}:00 a ${tienda.whatsappHasta}:00 h</p>
                <span class="fw-semibold"><c:out value="${tienda.telefono}"/></span>
            </a>
        </div>
        <div class="col-md-4">
            <a href="tel:${fn:replace(tienda.telefono, ' ', '')}" class="tarjeta p-4 d-block text-center text-decoration-none text-reset h-100">
                <i class="bi bi-telephone display-5 texto-ciruela"></i>
                <h2 class="h5 mt-2">Teléfono</h2>
                <p class="small texto-gris mb-1">Lunes a sábado</p>
                <span class="fw-semibold"><c:out value="${tienda.telefono}"/></span>
            </a>
        </div>
        <div class="col-md-4">
            <a href="mailto:${tienda.correo}" class="tarjeta p-4 d-block text-center text-decoration-none text-reset h-100">
                <i class="bi bi-envelope display-5 texto-ciruela"></i>
                <h2 class="h5 mt-2">Correo</h2>
                <p class="small texto-gris mb-1">Respondemos en menos de 24 h</p>
                <span class="fw-semibold"><c:out value="${tienda.correo}"/></span>
            </a>
        </div>
    </div>

    <div class="row g-4">
        <!-- Preguntas frecuentes (asistente) -->
        <div class="col-lg-6" id="preguntas">
            <h2 class="h3 mb-3">Preguntas frecuentes</h2>
            <div class="accordion" id="faq">
                <div class="accordion-item"><h3 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#f1">¿Hacen entregas el mismo día?</button></h3>
                    <div id="f1" class="accordion-collapse collapse" data-bs-parent="#faq"><div class="accordion-body small">Sí. Elige la fecha de hoy y un horario que empiece al menos 3 horas después de tu compra. No entregamos en domingo.</div></div></div>
                <div class="accordion-item"><h3 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#f2">¿Cuánto cuesta el envío?</button></h3>
                    <div id="f2" class="accordion-collapse collapse" data-bs-parent="#faq"><div class="accordion-body small">Depende de la distancia en línea recta desde la florería: hasta ${tienda.envioKm1} km ${pg:dinero(tienda.envioTarifa10)}, hasta ${tienda.envioKm2} km ${pg:dinero(tienda.envioTarifa15)} y hasta ${tienda.envioKm3} km ${pg:dinero(tienda.envioTarifa20)}. Es gratis en compras desde ${pg:dinero(tienda.envioGratisDesde)} dentro de la CDMX y hasta ${tienda.envioKm3} km. A más distancia o fuera de la CDMX cuesta ${pg:dinero(tienda.envioTarifa20)}.</div></div></div>
                <div class="accordion-item"><h3 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#f3">¿Cómo sigo mi pedido?</button></h3>
                    <div id="f3" class="accordion-collapse collapse" data-bs-parent="#faq"><div class="accordion-body small">En <a href="${ctx}/seguimiento">Seguimiento</a> con tu folio y tu correo, o en <a href="${ctx}/cuenta/pedidos">Mis pedidos</a> si tienes cuenta. Verás las etapas: pendiente, preparando, en camino y entregado.</div></div></div>
                <div class="accordion-item"><h3 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#f4">¿Qué métodos de pago aceptan?</button></h3>
                    <div id="f4" class="accordion-collapse collapse" data-bs-parent="#faq"><div class="accordion-body small">Tarjeta de crédito o débito y PayPal, y también puedes cerrar tu compra por WhatsApp.</div></div></div>
                <div class="accordion-item"><h3 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#f5">¿Qué es la dedicatoria con QR?</button></h3>
                    <div id="f5" class="accordion-collapse collapse" data-bs-parent="#faq"><div class="accordion-body small">Al pagar puedes escribir un mensaje. Lo imprimimos como código QR en la tarjeta del arreglo y quien recibe lo lee con su celular.</div></div></div>
                <div class="accordion-item"><h3 class="accordion-header"><button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#f6">Necesito factura</button></h3>
                    <div id="f6" class="accordion-collapse collapse" data-bs-parent="#faq"><div class="accordion-body small">Entra al detalle de tu pedido (Mis pedidos o Seguimiento) y llena tus datos fiscales en la sección «Factura». Te la enviamos por correo.</div></div></div>
            </div>

            <c:if test="${not empty solicitudes}">
                <h2 class="h4 mt-5 mb-3">Mis solicitudes</h2>
                <div class="list-group">
                    <c:forEach items="${solicitudes}" var="tk">
                        <a href="${ctx}/soporte/${tk.idSolicitud}" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                            <span><c:out value="${tk.asunto}"/><br><span class="small texto-gris">${pg:hace(tk.fechaHora)}</span></span>
                            <span class="badge text-bg-${tk.estado.color}">${tk.estado.etiqueta}</span>
                        </a>
                    </c:forEach>
                </div>
            </c:if>
        </div>

        <!-- Formulario de ticket -->
        <div class="col-lg-6">
            <div class="tarjeta p-4">
                <h2 class="h3 mb-1">Escríbenos</h2>
                <p class="small texto-gris mb-3">Abriremos una solicitud de soporte y te responderemos por correo en menos de 24 horas.</p>
                <form:form modelAttribute="soporteForm" action="${ctx}/soporte" method="post" novalidate="novalidate">
                    <input type="hidden" name="_csrf" value="${csrfToken}">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label for="nombre" class="form-label">Nombre</label>
                            <form:input path="nombre" id="nombre" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="nombre" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-md-6">
                            <label for="correo" class="form-label">Correo</label>
                            <form:input path="correo" id="correo" type="email" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="correo" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-md-8">
                            <label for="asunto" class="form-label">Asunto</label>
                            <form:input path="asunto" id="asunto" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="asunto" cssClass="invalid-feedback"/>
                        </div>
                        <div class="col-md-4">
                            <label for="folio" class="form-label">Folio <span class="fw-normal texto-gris">(opcional)</span></label>
                            <form:input path="folio" id="folio" cssClass="form-control text-uppercase"/>
                        </div>
                        <div class="col-12">
                            <label for="mensaje" class="form-label">Mensaje</label>
                            <form:textarea path="mensaje" id="mensaje" rows="5" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                            <form:errors path="mensaje" cssClass="invalid-feedback"/>
                        </div>
                    </div>
                    <button class="btn btn-ciruela w-100 mt-4">Enviar</button>
                </form:form>
            </div>
        </div>
    </div>
</div>
</t:pagina>
