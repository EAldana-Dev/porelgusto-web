package com.neksbyte.porelgustoweb.exception;

/** Error de una regla del negocio (se muestra tal cual al usuario). */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) { super(mensaje); }
}
