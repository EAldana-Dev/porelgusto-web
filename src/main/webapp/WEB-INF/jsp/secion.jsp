
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Por el Gusto</title>

    <link rel="stylesheet" href="<c:url value='/css/login.css'/>">
</head>

<body>
    <div class="container">
        <div class="barra_superior">
            <a href="carrito">
                <img src="/imagenes/logo.png" alt="Foto de perfil" class="logo">
            </a>
        </div>
        <div class="cuadrado">
    <h1 class="Inicio">Inicio de sesión</h1>

    <form action="<c:url value='/login'/>" method="POST" class="formulario">

        <input type="text" name="usuario" placeholder="Usuario" class="usuario" required>

        <input type="password" name="password" placeholder="Contraseña" class="contraseña" required>

        <c:if test="${not empty error}">
            <p role="alert"><c:out value="${error}"/></p>
        </c:if>

        <a href="/Recuperar contraseña">
            <p>¿Olvidaste tu contraseña?</p>
        </a>
        

        <button type="submit" class="iniciar_sesion">
            Iniciar sesión
        </button>

    </form>
    <a href="/signup">
    <p>Crear cuenta</p>
    </a>
    
</div>
    </div>
</body>
</html>
