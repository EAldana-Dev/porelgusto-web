package com.neksbyte.porelgustoweb.service.pago;

import com.neksbyte.porelgustoweb.dto.CompraEnProceso;
import com.neksbyte.porelgustoweb.model.FormaPago;
import com.neksbyte.porelgustoweb.service.ConfiguracionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * PayPal (API Orders v2). Las credenciales se capturan en el panel. Sin credenciales funciona en modo simulado:
 * se muestra una página donde se aprueba o cancela el pago.
 */
@Component
public class PagoConPayPal implements EstrategiaPago {
    private static final Logger log = LoggerFactory.getLogger(PagoConPayPal.class);
    private final ConfiguracionService configuracion;
    private final String moneda;
    private final RestClient http = RestClient.create();

    public PagoConPayPal(ConfiguracionService configuracion, @Value("${app.pagos.moneda:mxn}") String moneda) {
        this.configuracion = configuracion;
        this.moneda = moneda.toUpperCase(Locale.ROOT);
    }

    private String clientId() { String v = configuracion.paypalClientId(); return v == null ? "" : v.trim(); }
    private String clientSecret() { String v = configuracion.paypalSecreta(); return v == null ? "" : v.trim(); }

    /** PayPal real o Sandbox, según el panel. */
    private String api() { return configuracion.paypalProduccion() ? "https://api-m.paypal.com" : "https://api-m.sandbox.paypal.com"; }

    @Override public FormaPago forma() { return FormaPago.PAYPAL; }
    @Override public boolean isConfigurado() { return !clientId().isEmpty() && !clientSecret().isEmpty(); }
    @Override public boolean isSimulado() { return !isConfigurado(); }

    public boolean isModoPrueba() { return !configuracion.paypalProduccion(); }

    /** Revisa que las credenciales funcionen. */
    public String probarConexion() {
        if (isSimulado()) throw new IllegalStateException("No hay credenciales de PayPal: los pagos con PayPal están en modo simulado.");
        try {
            token();
            return "Credenciales válidas en " + (configuracion.paypalProduccion() ? "PayPal (pagos reales)" : "PayPal Sandbox (pruebas)");
        } catch (org.springframework.web.client.RestClientResponseException e) {
            throw new IllegalStateException("PayPal rechazó las credenciales de " + (configuracion.paypalProduccion() ? "Live" : "Sandbox")
                    + " (" + e.getStatusCode().value() + "). Revisa que el Client ID y el Secret sean del mismo modo que el interruptor «Live».");
        } catch (RuntimeException e) {
            throw new IllegalStateException("No pudimos conectar con PayPal. Revisa la conexión a internet del servidor.");
        }
    }

    @Override
    public String iniciar(CompraEnProceso compra, String urlBase) {
        if (isSimulado()) return "/pago/paypal-simulado";
        Map<String, Object> cuerpo = Map.of(
                "intent", "CAPTURE",
                "purchase_units", List.of(Map.of(
                        "reference_id", compra.getReferencia(),
                        "description", "Pedido Por el Gusto",
                        "amount", Map.of("currency_code", moneda, "value", compra.getTotal().setScale(2, RoundingMode.HALF_UP).toPlainString()))),
                "application_context", Map.of(
                        "brand_name", "Por el Gusto",
                        "user_action", "PAY_NOW",
                        "shipping_preference", "NO_SHIPPING",
                        "return_url", urlBase + "/pago/paypal/exito",
                        "cancel_url", urlBase + "/pago/cancelado"));
        try {
            Map<?, ?> r = http.post().uri(api() + "/v2/checkout/orders")
                    .header("Authorization", "Bearer " + token())
                    .contentType(MediaType.APPLICATION_JSON).body(cuerpo).retrieve().body(Map.class);
            compra.setReferenciaProveedor(String.valueOf(r.get("id")));
            for (Object o : (List<?>) r.get("links")) {
                Map<?, ?> liga = (Map<?, ?>) o;
                if ("approve".equals(liga.get("rel")) || "payer-action".equals(liga.get("rel"))) return String.valueOf(liga.get("href"));
            }
            throw new IllegalStateException("PayPal no regresó la liga de aprobación");
        } catch (RestClientException e) {
            log.error("PayPal: no se pudo crear la orden", e);
            throw new IllegalStateException("No pudimos conectar con PayPal. Intenta de nuevo.");
        }
    }

    @Override
    public ResultadoPago confirmar(CompraEnProceso compra, Map<String, String> p) {
        if (isSimulado()) {
            return "aprobar".equals(p.get("accion"))
                    ? ResultadoPago.aprobado("SIM-PAYPAL-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase(), "Simulado")
                    : ResultadoPago.rechazado("Cancelaste el pago en PayPal", "Simulado");
        }
        String orden = p.get("token");
        if (orden == null || !orden.equals(compra.getReferenciaProveedor())) {
            return ResultadoPago.rechazado("La orden de PayPal no coincide con tu compra", "PayPal");
        }
        try {
            Map<?, ?> r = http.post().uri(api() + "/v2/checkout/orders/" + orden + "/capture")
                    .header("Authorization", "Bearer " + token())
                    .contentType(MediaType.APPLICATION_JSON).body(Map.of()).retrieve().body(Map.class);
            if (!"COMPLETED".equals(r.get("status"))) return ResultadoPago.rechazado("Pago rechazado por PayPal", "PayPal");
            Map<?, ?> unidad = (Map<?, ?>) ((List<?>) r.get("purchase_units")).get(0);
            Map<?, ?> captura = (Map<?, ?>) ((List<?>) ((Map<?, ?>) unidad.get("payments")).get("captures")).get(0);
            return ResultadoPago.aprobado(String.valueOf(captura.get("id")), "PayPal");
        } catch (RuntimeException e) {
            log.error("PayPal: no se pudo capturar el pago", e);
            return ResultadoPago.rechazado("Pago rechazado: PayPal no confirmó el cobro", "PayPal");
        }
    }

    /** Token OAuth de la API de PayPal. */
    private String token() {
        MultiValueMap<String, String> f = new LinkedMultiValueMap<>();
        f.add("grant_type", "client_credentials");
        String basic = Base64.getEncoder().encodeToString((clientId() + ":" + clientSecret()).getBytes(StandardCharsets.UTF_8));
        Map<?, ?> r = http.post().uri(api() + "/v1/oauth2/token").header("Authorization", "Basic " + basic)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(f).retrieve().body(Map.class);
        return String.valueOf(r.get("access_token"));
    }

    @Override
    public boolean reembolsar(String transaccion, BigDecimal monto) {
        if (isSimulado() || transaccion == null || transaccion.startsWith("SIM-")) return true;
        try {
            http.post().uri(api() + "/v2/payments/captures/" + transaccion + "/refund")
                    .header("Authorization", "Bearer " + token()).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("amount", Map.of("currency_code", moneda, "value", monto.setScale(2, RoundingMode.HALF_UP).toPlainString())))
                    .retrieve().toBodilessEntity();
            return true;
        } catch (RestClientException e) {
            log.error("PayPal: no se pudo hacer la devolución de {}", transaccion, e);
            return false;
        }
    }
}
