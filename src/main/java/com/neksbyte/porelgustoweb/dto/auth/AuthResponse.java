package com.neksbyte.porelgustoweb.dto.auth;

public record AuthResponse(String token, String tokenType, long expiresIn, UsuarioResumen usuario) {
    public record UsuarioResumen(Integer idUsuario, String nombre, String correoElectronico, Integer idTipoUsuario) {}
}
