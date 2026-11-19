package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class TokenService {
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private final byte[] secret;
    private final long expirationSeconds;

    public TokenService(@Value("${app.auth.token-secret:dev-only-change-this-token-secret-32-bytes-minimum}") String secret,
                        @Value("${app.auth.token-expiration-seconds:3600}") long expirationSeconds) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        if (this.secret.length < 32) throw new IllegalArgumentException("app.auth.token-secret debe tener al menos 32 bytes");
        this.expirationSeconds = expirationSeconds;
    }

    public String issue(Usuario usuario) {
        long now = Instant.now().getEpochSecond();
        String header = encode("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = encode("{\"sub\":\"" + usuario.getIdUsuario() + "\",\"iat\":" + now
                + ",\"exp\":" + (now + expirationSeconds) + "}");
        String unsigned = header + "." + payload;
        return unsigned + "." + ENCODER.encodeToString(sign(unsigned));
    }

    public long getExpirationSeconds() { return expirationSeconds; }

    private String encode(String value) { return ENCODER.encodeToString(value.getBytes(StandardCharsets.UTF_8)); }

    private byte[] sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo firmar el token", exception);
        }
    }
}
