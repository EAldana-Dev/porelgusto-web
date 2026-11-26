package com.neksbyte.porelgustoweb.service.pago;

import com.neksbyte.porelgustoweb.model.FormaPago;
import org.springframework.stereotype.Service;

import java.util.List;

/** Elige la estrategia de pago según la forma de pago. */
@Service
public class PagoService {
    private final List<EstrategiaPago> estrategias;
    /** Permite pagos simulados cuando no hay llaves (solo desarrollo y pruebas: perfil h2). */
    private final boolean simulados;

    public PagoService(List<EstrategiaPago> estrategias,
                       @org.springframework.beans.factory.annotation.Value("${app.pagos.simulados:false}") boolean simulados) {
        this.estrategias = estrategias;
        this.simulados = simulados;
    }

    /** Se puede ofrecer al cliente: tiene llaves, o se permiten pagos simulados. */
    public boolean isDisponible(FormaPago forma) {
        EstrategiaPago e = estrategia(forma);
        return e.isConfigurado() || simulados;
    }

    public EstrategiaPago estrategia(FormaPago forma) {
        return estrategias.stream().filter(e -> e.forma() == forma).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Forma de pago no soportada: " + forma));
    }

    public boolean isSimulado(FormaPago forma) { return estrategia(forma).isSimulado(); }

    public boolean isSimulacionPermitida() { return simulados; }
}
