<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="pg" uri="https://porelgusto.mx/funciones" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin titulo="Usuarios y roles">
    <div class="row g-3 mb-4">
        <c:forEach items="${roles}" var="r">
            <div class="col-md-6 col-xl-3">
                <div class="dato-card h-100">
                    <div class="fw-bold"><c:out value="${r.rol}"/></div>
                    <div class="small texto-gris mb-2"><c:out value="${descripciones[r.idTipoUsuario]}"/></div>
                    <div class="d-flex flex-wrap gap-1">
                        <c:forEach items="${permisos[r.idTipoUsuario]}" var="pe"><span class="badge text-bg-light">${pe}</span></c:forEach>
                        <c:if test="${empty permisos[r.idTipoUsuario]}"><span class="small texto-gris">Sin acceso al panel</span></c:if>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="tarjeta">
        <div class="table-responsive">
            <table class="table tabla-pg align-middle small mb-0">
                <thead><tr><th>Usuario</th><th>Correo</th><th>Registro</th><th>Rol</th><th>Estado</th></tr></thead>
                <tbody>
                <c:forEach items="${usuarios}" var="us">
                    <tr class="${us.activo ? '' : 'opacity-50'}">
                        <td><strong><c:out value="${us.nombre}"/></strong><br><span class="texto-gris"><c:out value="${us.telefono}"/></span></td>
                        <td><c:out value="${us.correoElectronico}"/></td>
                        <td>${pg:fecha(us.fechaRegistro)}</td>
                        <td>
                            <form action="${ctx}/admin/usuarios/${us.idUsuario}/rol" method="post" class="d-flex gap-1">
                                <input type="hidden" name="_csrf" value="${csrfToken}">
                                <select name="rol" class="form-select form-select-sm" aria-label="Rol" ${us.idUsuario == usuarioActual.idUsuario ? 'disabled' : ''} onchange="this.form.requestSubmit()">
                                    <c:forEach items="${roles}" var="r"><option value="${r.idTipoUsuario}" ${us.idRol == r.idTipoUsuario ? 'selected' : ''}><c:out value="${r.rol}"/></option></c:forEach>
                                </select>
                            </form>
                        </td>
                        <td>
                            <form action="${ctx}/admin/usuarios/${us.idUsuario}/estado" method="post" data-confirmar="${us.activo ? '¿Desactivar esta cuenta? No podrá iniciar sesión.' : '¿Reactivar esta cuenta?'}">
                                <input type="hidden" name="_csrf" value="${csrfToken}">
                                <button class="btn btn-sm ${us.activo ? 'btn-outline-secondary' : 'btn-outline-success'}" ${us.idUsuario == usuarioActual.idUsuario ? 'disabled' : ''}>${us.activo ? 'Activa' : 'Inactiva'}</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</t:admin>
