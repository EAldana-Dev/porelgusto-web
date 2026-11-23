<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Por el Gusto</title>
    <link rel="stylesheet" href="<c:url value='/css/signup.css'/>">
</head>
<body>
    <div class="container">
        <div class="barra_superior">
            <a href="home">
                <img src="/imagenes/logo.png" alt="Foto de perfil" class="logo">
            </a>
        </div>
        <div class="cuadrado">
    <h1 class="Inicio">Crear cuenta</h1>

    <form action="<c:url value='/signup'/>" method="POST" class="formulario">

        <input type="text" name="nombre" placeholder="Nombre" class="usuario" maxlength="150" required>

        <input type="text" name="direccion" placeholder="Dirección" class="usuario" maxlength="255">

        <input type="email" name="correoElectronico" placeholder="Correo electrónico" class="usuario" maxlength="150" required>

        <input type="tel" name="telefono" placeholder="Teléfono" class="usuario" maxlength="50">

        <input type="password" name="password" placeholder="Contraseña (mínimo 8 caracteres)" class="contraseña" minlength="8" maxlength="72" required>

        <c:if test="${not empty error}">
            <p role="alert"><c:out value="${error}"/></p>
        </c:if>

        <button type="submit" class="iniciar_sesion">
            Crear cuenta
        </button>

            </form>
    <a href="/login">
    <p>Ya tengo cuenta</p>
    </a>

    </div>
</body>
</html>
