package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.dao.ArregloFloralDao;
import com.neksbyte.porelgustoweb.dao.UsuarioDao;
import com.neksbyte.porelgustoweb.dto.PromocionForm;
import com.neksbyte.porelgustoweb.exception.NoEncontradoException;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.*;
import com.neksbyte.porelgustoweb.repository.CategoriaRepository;
import com.neksbyte.porelgustoweb.repository.PromocionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/** Promociones, ofertas y eventos que bajan el precio de los productos automáticamente. */
@Service
@Transactional
public class PromocionService {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final PromocionRepository promociones;
    private final CategoriaRepository categorias;
    private final ArregloFloralDao arreglos;
    private final UsuarioDao usuarios;
    private final CorreoService correo;

    public PromocionService(PromocionRepository promociones, CategoriaRepository categorias, ArregloFloralDao arreglos,
                            UsuarioDao usuarios, CorreoService correo) {
        this.promociones = promociones;
        this.categorias = categorias;
        this.arreglos = arreglos;
        this.usuarios = usuarios;
        this.correo = correo;
    }

    @Transactional(readOnly = true)
    public List<Promocion> todas() { return promociones.findAllByOrderByFechaInicioDesc(); }

    @Transactional(readOnly = true)
    public List<Promocion> vigentes() { return promociones.findVigentes(LocalDateTime.now()); }

    @Transactional(readOnly = true)
    public Promocion porId(Integer id) {
        return promociones.findById(id).orElseThrow(() -> new NoEncontradoException("La promoción no existe"));
    }

    /** La promoción vigente que más descuenta a este producto (o null). */
    public Promocion mejorOferta(ArregloFloral p, List<Promocion> vigentes) {
        BigDecimal precio = p.getCosto() == null ? BigDecimal.ZERO : p.getCosto();
        return vigentes.stream()
                .filter(pr -> pr.aplicaA(p))
                .max(Comparator.comparing(pr -> pr.descuentoDe(precio)))
                .orElse(null);
    }

    public Promocion guardar(PromocionForm f) {
        if (!f.getFin().isAfter(f.getInicio())) throw new ReglaNegocioException("La fecha de fin debe ser posterior a la de inicio");
        TipoDescuento tipoDesc = TipoDescuento.valueOf(f.getTipoDescuento());
        if (tipoDesc == TipoDescuento.PORCENTAJE && f.getValor().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new ReglaNegocioException("El porcentaje no puede ser mayor a 100");
        }
        boolean nueva = f.getId() == null;
        Promocion p = nueva ? new Promocion() : porId(f.getId());
        p.setNombre(f.getNombre().trim());
        p.setDescripcion(Textos.limpio(f.getDescripcion()));
        p.setTipo(TipoPromocion.valueOf(f.getTipo()));
        p.setTipoDescuento(tipoDesc);
        p.setValor(f.getValor());
        p.setCondiciones(Textos.limpio(f.getCondiciones()));
        p.setFechaInicio(f.getInicio());
        p.setFechaFin(f.getFin());
        p.setActiva(f.isActivo());
        p.setCategoria(f.getCategoriaId() == null ? null : categorias.findById(f.getCategoriaId()).orElse(null));
        p.setProductos(new HashSet<>(f.getProductos().stream().map(arreglos::findById)
                .flatMap(java.util.Optional::stream).toList()));
        promociones.save(p);
        if (nueva && f.isNotificar() && p.isActiva()) {
            avisarClientes("Nueva " + p.getTipo().getEtiqueta().toLowerCase() + " en Por el Gusto: " + p.getNombre(),
                    p.getNombre() + "\n\n" + (p.getDescripcion() == null ? "" : p.getDescripcion() + "\n\n")
                            + "Descuento: " + p.getDescuentoTexto()
                            + "\nVigencia: del " + p.getFechaInicio().format(FECHA) + " al " + p.getFechaFin().format(FECHA)
                            + (p.getCondiciones() == null ? "" : "\nCondiciones: " + p.getCondiciones()));
        }
        return p;
    }

    public void eliminar(Integer id) { promociones.delete(porId(id)); }

    /** Envía un aviso por correo a todos los clientes activos. */
    public void avisarClientes(String asunto, String cuerpo) {
        usuarios.findByTipos(List.of(Permisos.CLIENTE)).stream().filter(Usuario::isActivo)
                .forEach(u -> correo.enviar(u.getCorreoElectronico(), asunto, cuerpo));
    }
}
