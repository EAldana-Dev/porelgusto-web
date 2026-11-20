package com.neksbyte.porelgustoweb.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String correoElectronico, @NotBlank String password) {
}
