package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.model.Correo;
import com.neksbyte.porelgustoweb.repository.CorreoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Envío de correos. Si hay servidor SMTP (variable MAIL_HOST) se envían de verdad; siempre se guarda una copia
 * en la tabla Correo, que el administrador ve en el panel (Correos enviados).
 */
@Service
public class CorreoService {
    private static final Logger log = LoggerFactory.getLogger(CorreoService.class);
    private final CorreoRepository correos;
    private final JavaMailSender smtp;
    private final String remitente;
    /** El envío por SMTP se hace en segundo plano para no hacer esperar al cliente. */
    private final ExecutorService fondo = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "envio-correos");
        t.setDaemon(true);
        return t;
    });

    public CorreoService(CorreoRepository correos, ObjectProvider<JavaMailSender> smtp,
                         @Value("${spring.mail.host:}") String host,
                         @Value("${app.correo.remitente:contacto@porelgusto.mx}") String remitente) {
        this.correos = correos;
        this.smtp = host == null || host.isBlank() ? null : smtp.getIfAvailable();
        this.remitente = remitente;
        if (this.smtp == null) log.info("Correos: sin servidor SMTP (MAIL_HOST); se guardan pero no se envían.");
    }

    /** true si los correos salen de verdad. */
    public boolean isEnvioReal() { return smtp != null; }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void enviar(String para, String asunto, String cuerpo) {
        if (Textos.vacio(para)) return;
        String a = asunto.length() > 200 ? asunto.substring(0, 200) : asunto;
        String c = cuerpo.length() > 4000 ? cuerpo.substring(0, 4000) : cuerpo;
        Correo m = new Correo();
        m.setPara(para);
        m.setAsunto(a);
        m.setCuerpo(c);
        m.setFechaHora(LocalDateTime.now());
        correos.save(m);
        if (smtp == null) return;
        fondo.submit(() -> {
            try {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setFrom("Por el Gusto <" + remitente + ">");
                msg.setTo(para);
                msg.setSubject(a);
                msg.setText(c + "\n\n—\nPor el Gusto · Envío de flores");
                smtp.send(msg);
            } catch (RuntimeException e) {
                log.error("No se pudo enviar el correo «{}» a {}: {}", a, para, e.getMessage());
            }
        });
    }

    @Transactional(readOnly = true)
    public List<Correo> recientes() { return correos.findTop50ByOrderByFechaHoraDescIdCorreoDesc(); }
}
