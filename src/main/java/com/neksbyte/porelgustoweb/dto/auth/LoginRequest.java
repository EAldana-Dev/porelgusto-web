package com.neksbyte.porelgustoweb.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank @Email String correoElectronico, @NotBlank String password) {
}
