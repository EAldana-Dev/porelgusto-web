package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.config.Cifrador;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.ConfiguracionTienda;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.repository.ConfiguracionTiendaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Configuración editable desde el panel: llaves de Stripe y PayPal (a qué cuentas llega el dinero)
 * y punto de partida de los envíos. Si en el panel no hay llaves, se usan las variables de entorno
 * (STRIPE_SECRET_KEY, PAYPAL_CLIENT_ID, PAYPAL_CLIENT_SECRET); si tampoco hay, el pago se simula.
 */
@Service
public class ConfiguracionService {
    private final ConfiguracionTiendaRepository repo;
    private final Cifrador cifrador;
    private final Geocodificador geo;
    private final String stripeEntorno;
    private final String paypalIdEntorno;
    private final String paypalSecretoEntorno;
    private final String origenDireccion;
    private final double origenLat;
    private final double origenLng;

    public ConfiguracionService(ConfiguracionTiendaRepository repo, Cifrador cifrador, Geocodificador geo,
                                @Value("${app.pagos.stripe.secret-key:}") String stripeEntorno,
                                @Value("${app.pagos.paypal.client-id:}") String paypalIdEntorno,
                                @Value("${app.pagos.paypal.client-secret:}") String paypalSecretoEntorno,
                                @Value("${app.envio.origen.direccion}") String origenDireccion,
                                @Value("${app.envio.origen.latitud}") double origenLat,
                                @Value("${app.envio.origen.longitud}") double origenLng) {
        this.repo = repo;
        this.cifrador = cifrador;
        this.geo = geo;
        this.stripeEntorno = stripeEntorno == null ? "" : stripeEntorno.trim();
        this.paypalIdEntorno = paypalIdEntorno == null ? "" : paypalIdEntorno.trim();
        this.paypalSecretoEntorno = paypalSecretoEntorno == null ? "" : paypalSecretoEntorno.trim();
        this.origenDireccion = origenDireccion;
        this.origenLat = origenLat;
        this.origenLng = origenLng;
    }

    /** La configuración guardada (o una nueva con los valores de application.properties). */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ConfiguracionTienda actual() {
        return repo.findById(ConfiguracionTienda.ID).orElseGet(() -> {
            ConfiguracionTienda c = new ConfiguracionTienda();
            c.setOrigenDireccion(origenDireccion);
            c.setOrigenLatitud(origenLat);
            c.setOrigenLongitud(origenLng);
            return c;
        });
    }

    // ---------- Llaves que usan las estrategias de pago ----------

    public String stripeSecreta() {
        String panel = cifrador.descifrar(actual().getStripeSecretaCifrada());
        return panel != null ? panel : stripeEntorno;
    }

    public String paypalClientId() {
        String panel = actual().getPaypalClientId();
        return panel != null && !panel.isBlank() ? panel.trim() : paypalIdEntorno;
    }

    public String paypalSecreta() {
        String panel = cifrador.descifrar(actual().getPaypalSecretaCifrada());
        return panel != null ? panel : paypalSecretoEntorno;
    }

    public boolean paypalProduccion() { return actual().isPaypalProduccion(); }

    /** La llave guardada ya no se puede leer (cambió AUTH_TOKEN_SECRET): hay que capturarla de nuevo. */
    public boolean isLlaveIlegible() {
        ConfiguracionTienda c = actual();
        return (c.getStripeSecretaCifrada() != null && cifrador.descifrar(c.getStripeSecretaCifrada()) == null)
                || (c.getPaypalSecretaCifrada() != null && cifrador.descifrar(c.getPaypalSecretaCifrada()) == null);
    }

    /** "sk_live_…1234" para mostrar sin revelar la llave. */
    public static String enmascarar(String llave) {
        if (llave == null || llave.isBlank()) return null;
        String l = llave.trim();
        int corte = l.startsWith("sk_") ? l.indexOf('_', 3) + 1 : 0;
        return l.substring(0, Math.max(0, corte)) + "••••" + l.substring(Math.max(corte, l.length() - 4));
    }

    // ---------- Guardar desde el panel ----------

    /**
     * Las llaves en blanco no cambian lo guardado (para no tener que volver a escribirlas);
     * "borrarStripe"/"borrarPaypal" las quitan y el pago vuelve al modo simulado.
     */
    @Transactional
    public void guardarPagos(String stripe, boolean borrarStripe, String paypalId, String paypalSecreta,
                             boolean paypalProduccion, boolean borrarPaypal, Usuario por) {
        ConfiguracionTienda c = actual();
        if (borrarStripe) c.setStripeSecretaCifrada(null);
        else if (!Textos.vacio(stripe)) {
            String s = stripe.trim();
            if (!s.matches("(sk|rk)_(test|live)_[A-Za-z0-9]{10,}")) {
                throw new ReglaNegocioException("La llave de Stripe debe ser la «Secret key» (empieza con sk_live_ o sk_test_)");
            }
            c.setStripeSecretaCifrada(cifrador.cifrar(s));
        }
        if (borrarPaypal) {
            c.setPaypalClientId(null);
            c.setPaypalSecretaCifrada(null);
        } else {
            if (!Textos.vacio(paypalId)) c.setPaypalClientId(paypalId.trim());
            if (!Textos.vacio(paypalSecreta)) c.setPaypalSecretaCifrada(cifrador.cifrar(paypalSecreta.trim()));
            boolean tieneId = !Textos.vacio(c.getPaypalClientId());
            boolean tieneSecreto = c.getPaypalSecretaCifrada() != null;
            if (tieneId != tieneSecreto) throw new ReglaNegocioException("Para PayPal escribe el Client ID y el Secret");
        }
        c.setPaypalProduccion(paypalProduccion);
        registrar(c, por);
    }

    /** Cambia el punto de partida: se ubica la dirección; también se pueden escribir las coordenadas. */
    @Transactional
    public void guardarOrigen(String direccion, Double lat, Double lng, Usuario por) {
        if (Textos.vacio(direccion)) throw new ReglaNegocioException("Escribe la dirección de la florería");
        ConfiguracionTienda c = actual();
        double[] punto = lat != null && lng != null ? new double[]{lat, lng} : geo.ubicar(direccion.trim()).orElse(null);
        if (punto == null) {
            throw new ReglaNegocioException("No encontramos esa dirección en el mapa. Escríbela con colonia, alcaldía y código postal, o captura la latitud y longitud.");
        }
        if (Math.abs(punto[0]) > 90 || Math.abs(punto[1]) > 180) throw new ReglaNegocioException("Coordenadas inválidas");
        c.setOrigenDireccion(direccion.trim());
        c.setOrigenLatitud(punto[0]);
        c.setOrigenLongitud(punto[1]);
        registrar(c, por);
    }

    private void registrar(ConfiguracionTienda c, Usuario por) {
        c.setActualizado(LocalDateTime.now());
        c.setActualizadoPor(por == null ? null : por.getNombre());
        repo.save(c);
    }
}
