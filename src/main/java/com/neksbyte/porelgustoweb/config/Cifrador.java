package com.neksbyte.porelgustoweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Cifra las llaves de pago que se guardan desde el panel (AES-GCM). La llave de cifrado sale de
 * app.auth.token-secret (variable AUTH_TOKEN_SECRET en producción): si cambia, hay que volver a capturarlas.
 */
@Component
public class Cifrador {
    private final SecretKeySpec llave;
    private final SecureRandom random = new SecureRandom();

    public Cifrador(@Value("${app.auth.token-secret:dev-only-change-this-token-secret-32-bytes-minimum}") String secreto) {
        try {
            byte[] h = MessageDigest.getInstance("SHA-256").digest(("pg-config:" + secreto).getBytes(StandardCharsets.UTF_8));
            this.llave = new SecretKeySpec(h, "AES");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String cifrar(String texto) {
        if (texto == null || texto.isBlank()) return null;
        try {
            byte[] iv = new byte[12];
            random.nextBytes(iv);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, llave, new GCMParameterSpec(128, iv));
            byte[] cifrado = c.doFinal(texto.trim().getBytes(StandardCharsets.UTF_8));
            byte[] todo = new byte[iv.length + cifrado.length];
            System.arraycopy(iv, 0, todo, 0, iv.length);
            System.arraycopy(cifrado, 0, todo, iv.length, cifrado.length);
            return Base64.getEncoder().encodeToString(todo);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo cifrar", e);
        }
    }

    /** Regresa null si no hay valor o si no se puede descifrar (por ejemplo, cambió el secreto). */
    public String descifrar(String guardado) {
        if (guardado == null || guardado.isBlank()) return null;
        try {
            byte[] todo = Base64.getDecoder().decode(guardado);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.DECRYPT_MODE, llave, new GCMParameterSpec(128, todo, 0, 12));
            return new String(c.doFinal(todo, 12, todo.length - 12), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }
}
