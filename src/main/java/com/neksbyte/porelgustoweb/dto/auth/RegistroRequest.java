package com.neksbyte.porelgustoweb.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @NotBlank @Size(max = 150) String nombre,
        @Size(max = 255) String direccion,
        @NotBlank @Email @Size(max = 150) String correoElectronico,
        @Size(max = 50) String telefono,
        @NotBlank @Size(min = 8, max = 72) String password) {
}
