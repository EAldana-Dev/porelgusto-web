package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.CuponDao;
import com.neksbyte.porelgustoweb.dto.CuponForm;
import com.neksbyte.porelgustoweb.exception.NoEncontradoException;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.Cupon;
import com.neksbyte.porelgustoweb.service.CuponService;
import com.neksbyte.porelgustoweb.service.PromocionService;
import com.neksbyte.porelgustoweb.service.Textos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class CuponServiceImpl extends BaseServiceImpl<Cupon, Integer> implements CuponService {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final CuponDao cupones;
    private final PromocionService promociones;

    public CuponServiceImpl(CuponDao dao, PromocionService promociones) {
        super(dao);
        this.cupones = dao;
        this.promociones = promociones;
    }

    @Override
    @Transactional(readOnly = true)
    public Cupon validar(String codigo, BigDecimal subtotal) {
        if (Textos.vacio(codigo)) throw new ReglaNegocioException("Escribe un código de cupón");
        Cupon c = cupones.findByCodigo(codigo.trim())
                .orElseThrow(() -> new ReglaNegocioException("El cupón «" + codigo.trim() + "» no existe"));
        if (!c.isActivo()) throw new ReglaNegocioException("El cupón ya no está activo");
        if (c.getFechaInicio() != null && LocalDate.now().isBefore(c.getFechaInicio())) {
            throw new ReglaNegocioException("El cupón es válido a partir del " + c.getFechaInicio().format(FECHA));
        }
        if (c.isVencido()) throw new ReglaNegocioException("El cupón venció el " + c.getFechaVencimiento().format(FECHA));
        if (c.getLimiteDeUsos() != null && c.getUsos() >= c.getLimiteDeUsos()) {
            throw new ReglaNegocioException("El cupón ya alcanzó su límite de usos");
        }
        if (c.getCompraMinima() != null && subtotal.compareTo(c.getCompraMinima()) < 0) {
            throw new ReglaNegocioException("El cupón aplica en compras desde $" + c.getCompraMinima().stripTrailingZeros().toPlainString());
        }
        return c;
    }

    @Override
    public void registrarUso(Cupon cupon) { cupon.setUsos(cupon.getUsos() + 1); }

    @Override
    @Transactional(readOnly = true)
    public List<Cupon> todosRecientes() { return cupones.findAllRecientes(); }

    @Override
    @Transactional(readOnly = true)
    public Cupon porId(Integer id) {
        return cupones.findById(id).orElseThrow(() -> new NoEncontradoException("El cupón no existe"));
    }

    @Override
    public Cupon guardar(CuponForm f) {
        String codigo = f.getCodigo().trim().toUpperCase(Locale.ROOT);
        cupones.findByCodigo(codigo).filter(otro -> !otro.getIdCupon().equals(f.getId()))
                .ifPresent(otro -> { throw new ReglaNegocioException("Ya existe un cupón con el código " + codigo); });
        boolean porcentaje = "PORCENTAJE".equals(f.getTipoDescuento());
        if (porcentaje && f.getValor().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new ReglaNegocioException("El porcentaje no puede ser mayor a 100");
        }
        if (f.getInicio() != null && f.getVencimiento().isBefore(f.getInicio())) {
            throw new ReglaNegocioException("La fecha de vencimiento debe ser posterior a la de inicio");
        }
        boolean nuevo = f.getId() == null;
        Cupon c = nuevo ? new Cupon() : porId(f.getId());
        c.setNombre(f.getNombre().trim());
        c.setCodigo(codigo);
        c.setDescripcion(Textos.limpio(f.getDescripcion()));
        c.setPorcentajeDescuento(porcentaje ? f.getValor() : null);
        c.setMontoDescuento(porcentaje ? null : f.getValor());
        c.setCompraMinima(f.getCompraMinima());
        c.setFechaInicio(f.getInicio());
        c.setFechaVencimiento(f.getVencimiento());
        c.setLimiteDeUsos(f.getLimiteDeUsos());
        c.setEstado(f.isActivo() ? Cupon.ACTIVO : Cupon.INACTIVO);
        cupones.save(c);
        if (nuevo && f.isNotificar() && f.isActivo()) {
            promociones.avisarClientes("Nuevo cupón en Por el Gusto: " + c.getNombre(),
                    c.getNombre() + "\n\n" + (c.getDescripcion() == null ? "" : c.getDescripcion() + "\n\n")
                            + "Usa el código " + c.getCodigo() + " y obtén " + c.getDescuentoTexto() + " de descuento."
                            + (c.getCompraMinima() == null ? "" : "\nCompra mínima: $" + c.getCompraMinima().stripTrailingZeros().toPlainString())
                            + "\nVálido hasta el " + c.getFechaVencimiento().format(FECHA) + ".");
        }
        return c;
    }
}
