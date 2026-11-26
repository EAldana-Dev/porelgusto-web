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
import java.time.YearMonth;
import java.util.Map;
import java.util.UUID;

/**
 * Tarjeta de crédito o débito con Stripe Checkout.
 * La llave se captura en el panel (Configuración de la tienda). Sin llave funciona en modo simulado con tarjetas de prueba:
 * 4242 4242 4242 4242 se aprueba, 4000 0000 0000 0002 se rechaza y 4000 0000 0000 9995 no tiene fondos.
 */
@Component
public class PagoConTarjeta implements EstrategiaPago {
    private static final Logger log = LoggerFactory.getLogger(PagoConTarjeta.class);
    private static final String STRIPE = "https://api.stripe.com/v1";
    /** Stripe no acepta cobros menores a $10 MXN. */
    public static final BigDecimal MINIMO_MXN = BigDecimal.TEN;

    private final ConfiguracionService configuracion;
    private final String moneda;
    private final RestClient http = RestClient.create();

    public PagoConTarjeta(ConfiguracionService configuracion, @Value("${app.pagos.moneda:mxn}") String moneda) {
        this.configuracion = configuracion;
        this.moneda = moneda;
    }

    /** La llave vigente (la del panel o, si no hay, la variable STRIPE_SECRET_KEY). */
    private String claveSecreta() {
        String k = configuracion.stripeSecreta();
        return k == null ? "" : k.trim();
    }

    @Override public FormaPago forma() { return FormaPago.TARJETA; }
    @Override public boolean isConfigurado() { return !claveSecreta().isEmpty(); }
    @Override public boolean isSimulado() { return !isConfigurado(); }

    /** Modo de prueba de Stripe (llave sk_test_): no se cobra dinero real. */
    public boolean isModoPrueba() { return claveSecreta().startsWith("sk_test_") || claveSecreta().startsWith("rk_test_"); }

    /** Revisa que la llave funcione y regresa el nombre de la cuenta que recibe los pagos. */
    public String probarConexion() {
        if (isSimulado()) throw new IllegalStateException("No hay llave de Stripe: los pagos con tarjeta están en modo simulado.");
        try {
            Map<?, ?> r = http.get().uri(STRIPE + "/account").header("Authorization", "Bearer " + claveSecreta()).retrieve().body(Map.class);
            Object perfil = r.get("business_profile") instanceof Map<?, ?> bp ? bp.get("name") : null;
            Object nombre = perfil != null ? perfil : r.get("email") != null ? r.get("email") : r.get("id");
            boolean cobra = Boolean.TRUE.equals(r.get("charges_enabled"));
            return "Cuenta: " + nombre + (isModoPrueba() ? " (modo de prueba)" : "")
                    + (cobra || isModoPrueba() ? "" : ". Stripe todavía no habilita los cobros reales: falta completar la verificación de la cuenta.");
        } catch (org.springframework.web.client.RestClientResponseException e) {
            throw new IllegalStateException("Stripe rechazó la llave (" + e.getStatusCode().value()
                    + (e.getStatusCode().value() == 401 ? ": llave inválida o revocada" : "") + ").");
        } catch (RestClientException e) {
            throw new IllegalStateException("No pudimos conectar con Stripe. Revisa la conexión a internet del servidor.");
        }
    }

    @Override
    public String iniciar(CompraEnProceso compra, String urlBase) {
        if (isSimulado()) return "/pago/tarjeta";
        MultiValueMap<String, String> f = new LinkedMultiValueMap<>();
        f.add("mode", "payment");
        f.add("success_url", urlBase + "/pago/stripe/exito?session_id={CHECKOUT_SESSION_ID}");
        f.add("cancel_url", urlBase + "/pago/cancelado");
        f.add("customer_email", compra.getDatos().getCorreoCliente());
        f.add("client_reference_id", compra.getReferencia());
        f.add("line_items[0][quantity]", "1");
        f.add("line_items[0][price_data][currency]", moneda);
        f.add("line_items[0][price_data][unit_amount]", centavos(compra.getTotal()));
        f.add("line_items[0][price_data][product_data][name]", "Pedido Por el Gusto (" + compra.getLineas().size() + " productos)");
        f.add("payment_method_types[0]", "card");
        try {
            Map<?, ?> r = http.post().uri(STRIPE + "/checkout/sessions")
                    .header("Authorization", "Bearer " + claveSecreta())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(f)
                    .retrieve().body(Map.class);
            compra.setReferenciaProveedor(String.valueOf(r.get("id")));
            return String.valueOf(r.get("url"));
        } catch (RestClientException e) {
            log.error("Stripe: no se pudo crear la sesión de pago", e);
            throw new IllegalStateException("No pudimos conectar con el procesador de tarjetas. Intenta de nuevo.");
        }
    }

    @Override
    public ResultadoPago confirmar(CompraEnProceso compra, Map<String, String> p) {
        if (isSimulado()) return simular(p);
        String sesion = p.get("session_id");
        if (sesion == null || !sesion.equals(compra.getReferenciaProveedor())) {
            return ResultadoPago.rechazado("La sesión de pago no coincide con tu compra", "Stripe");
        }
        try {
            Map<?, ?> r = http.get().uri(STRIPE + "/checkout/sessions/" + sesion)
                    .header("Authorization", "Bearer " + claveSecreta()).retrieve().body(Map.class);
            return "paid".equals(r.get("payment_status"))
                    ? ResultadoPago.aprobado(String.valueOf(r.get("payment_intent")), "Stripe")
                    : ResultadoPago.rechazado("Pago rechazado", "Stripe");
        } catch (RestClientException e) {
            log.error("Stripe: no se pudo confirmar el pago", e);
            return ResultadoPago.rechazado("No pudimos confirmar el pago con Stripe", "Stripe");
        }
    }

    /** Valida la tarjeta de prueba como lo haría un banco. */
    private ResultadoPago simular(Map<String, String> p) {
        String numero = p.getOrDefault("numero", "").replaceAll("\\D", "");
        String vence = p.getOrDefault("vence", "").trim();
        String cvv = p.getOrDefault("cvv", "").trim();
        if (p.getOrDefault("titular", "").isBlank()) return ResultadoPago.rechazado("Escribe el nombre del titular", "Simulado");
        if (numero.length() < 13 || numero.length() > 19 || !luhn(numero)) return ResultadoPago.rechazado("El número de tarjeta no es válido", "Simulado");
        if (!vence.matches("(0[1-9]|1[0-2])/\\d{2}")) return ResultadoPago.rechazado("Escribe la fecha de vencimiento como MM/AA", "Simulado");
        YearMonth ym = YearMonth.of(2000 + Integer.parseInt(vence.substring(3)), Integer.parseInt(vence.substring(0, 2)));
        if (ym.isBefore(YearMonth.now())) return ResultadoPago.rechazado("La tarjeta está vencida", "Simulado");
        if (!cvv.matches("\\d{3,4}")) return ResultadoPago.rechazado("El código de seguridad (CVV) no es válido", "Simulado");
        if (numero.endsWith("0002")) return ResultadoPago.rechazado("Pago rechazado: el banco declinó la tarjeta", "Simulado");
        if (numero.endsWith("9995")) return ResultadoPago.rechazado("Pago rechazado: fondos insuficientes", "Simulado");
        String marca = numero.startsWith("4") ? "Visa" : numero.startsWith("5") ? "Mastercard" : numero.startsWith("3") ? "American Express" : "Tarjeta";
        return ResultadoPago.aprobado("SIM-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase(), "Simulado",
                marca + " terminación " + numero.substring(numero.length() - 4));
    }

    static boolean luhn(String n) {
        int suma = 0;
        boolean doble = false;
        for (int i = n.length() - 1; i >= 0; i--) {
            int d = n.charAt(i) - '0';
            if (doble) { d *= 2; if (d > 9) d -= 9; }
            suma += d;
            doble = !doble;
        }
        return suma % 10 == 0;
    }

    @Override
    public boolean reembolsar(String transaccion, BigDecimal monto) {
        if (isSimulado() || transaccion == null || transaccion.startsWith("SIM-")) return true;
        MultiValueMap<String, String> f = new LinkedMultiValueMap<>();
        f.add("payment_intent", transaccion);
        f.add("amount", centavos(monto));
        try {
            http.post().uri(STRIPE + "/refunds").header("Authorization", "Bearer " + claveSecreta())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(f).retrieve().toBodilessEntity();
            return true;
        } catch (RestClientException e) {
            log.error("Stripe: no se pudo hacer la devolución de {}", transaccion, e);
            return false;
        }
    }

    private static String centavos(BigDecimal monto) {
        return monto.movePointRight(2).setScale(0, RoundingMode.HALF_UP).toPlainString();
    }
}
