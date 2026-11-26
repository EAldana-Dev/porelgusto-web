package com.neksbyte.porelgustoweb.exception;

/** No existe el producto, pedido, etc. (se muestra la página 404). */
public class NoEncontradoException extends RuntimeException {
    public NoEncontradoException(String mensaje) { super(mensaje); }
}
