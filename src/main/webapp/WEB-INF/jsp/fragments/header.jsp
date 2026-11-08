<%-- Cabecera común: <head>, Bootstrap y barra de navegación.
     Uso: <c:set var="titulo" value="..."/> y luego <%@ include file="/WEB-INF/jsp/fragments/header.jsp" %> --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${titulo}"/> | Por el Gusto</title>
    <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/css/bootstrap.min.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>
<body class="d-flex flex-column min-vh-100">
<header>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="<c:url value='/'/>">Por el Gusto</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menu"
                    aria-controls="menu" aria-expanded="false" aria-label="Abrir menú">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="menu">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/'/>">Inicio</a></li>
                </ul>
            </div>
        </div>
    </nav>
</header>
<main class="container py-4 flex-grow-1">
