package com.neksbyte.porelgustoweb.controller.admin;

import com.neksbyte.porelgustoweb.dto.CuponForm;
import com.neksbyte.porelgustoweb.dto.PromocionForm;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.*;
import com.neksbyte.porelgustoweb.service.ArregloFloralService;
import com.neksbyte.porelgustoweb.service.CuponService;
import com.neksbyte.porelgustoweb.service.PromocionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Promociones, ofertas y eventos (se aplican solos) y cupones (el cliente escribe el código). */
@Controller
public class AdminPromocionesController {
    private final PromocionService promociones;
    private final CuponService cupones;
    private final ArregloFloralService catalogo;

    public AdminPromocionesController(PromocionService promociones, CuponService cupones, ArregloFloralService catalogo) {
        this.promociones = promociones;
        this.cupones = cupones;
        this.catalogo = catalogo;
    }

    @GetMapping("/admin/promociones")
    public String lista(Model model) {
        model.addAttribute("promociones", promociones.todas());
        model.addAttribute("cupones", cupones.todosRecientes());
        model.addAttribute("ahora", LocalDateTime.now());
        model.addAttribute("modulo", "promociones");
        return "admin/promociones";
    }

    // ---------------- Promociones, ofertas y eventos ----------------

    @GetMapping("/admin/promociones/nueva")
    public String nueva(@RequestParam(defaultValue = "PROMOCION") String tipo, Model model) {
        PromocionForm f = new PromocionForm();
        try {
            f.setTipo(TipoPromocion.valueOf(tipo).name());
        } catch (IllegalArgumentException e) {
            f.setTipo(TipoPromocion.PROMOCION.name());
        }
        f.setInicio(LocalDateTime.now().withSecond(0).withNano(0));
        f.setFin(LocalDateTime.now().plusDays(30).withHour(23).withMinute(59).withSecond(0).withNano(0));
        model.addAttribute("promocionForm", f);
        return formulario(model);
    }

    @GetMapping("/admin/promociones/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Promocion p = promociones.porId(id);
        PromocionForm f = new PromocionForm();
        f.setId(p.getIdPromocion());
        f.setNombre(p.getNombre());
        f.setDescripcion(p.getDescripcion());
        f.setTipo(p.getTipo().name());
        f.setTipoDescuento(p.getTipoDescuento().name());
        f.setValor(p.getValor());
        f.setCondiciones(p.getCondiciones());
        f.setInicio(p.getFechaInicio());
        f.setFin(p.getFechaFin());
        f.setCategoriaId(p.getCategoria() == null ? null : p.getCategoria().getIdCategoria());
        f.setProductos(p.getProductos().stream().map(ArregloFloral::getIdArregloFloral).toList());
        f.setActivo(p.isActiva());
        f.setNotificar(false);
        model.addAttribute("promocionForm", f);
        return formulario(model);
    }

    private String formulario(Model model) {
        model.addAttribute("tipos", TipoPromocion.values());
        model.addAttribute("tiposDescuento", TipoDescuento.values());
        model.addAttribute("categorias", catalogo.todasLasCategorias());
        model.addAttribute("productos", catalogo.todosLosProductos());
        model.addAttribute("modulo", "promociones");
        return "admin/promocion-form";
    }

    @PostMapping("/admin/promociones")
    public String guardar(@Valid @ModelAttribute("promocionForm") PromocionForm form, BindingResult errores, Model model,
                          RedirectAttributes flash) {
        if (!errores.hasErrors()) {
            try {
                boolean nueva = form.getId() == null;
                Promocion p = promociones.guardar(form);
                flash.addFlashAttribute("mensaje", p.getTipo().getEtiqueta() + " «" + p.getNombre() + "» guardada"
                        + (nueva && form.isNotificar() && p.isActiva() ? " y enviada por correo a los clientes." : "."));
                return "redirect:/admin/promociones";
            } catch (ReglaNegocioException e) {
                model.addAttribute("error", e.getMessage());
            }
        } else {
            model.addAttribute("error", "No se guardó. Revisa los campos marcados.");
        }
        return formulario(model);
    }

    @PostMapping("/admin/promociones/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, RedirectAttributes flash) {
        promociones.eliminar(id);
        flash.addFlashAttribute("mensaje", "Promoción eliminada.");
        return "redirect:/admin/promociones";
    }

    // ---------------- Cupones ----------------

    @GetMapping("/admin/cupones/nuevo")
    public String nuevoCupon(Model model) {
        CuponForm f = new CuponForm();
        f.setInicio(LocalDate.now());
        f.setVencimiento(LocalDate.now().plusMonths(1));
        f.setNotificar(true);
        model.addAttribute("cuponForm", f);
        model.addAttribute("modulo", "promociones");
        return "admin/cupon-form";
    }

    @GetMapping("/admin/cupones/{id}")
    public String editarCupon(@PathVariable Integer id, Model model) {
        Cupon c = cupones.porId(id);
        CuponForm f = new CuponForm();
        f.setId(c.getIdCupon());
        f.setNombre(c.getNombre());
        f.setCodigo(c.getCodigo());
        f.setDescripcion(c.getDescripcion());
        f.setTipoDescuento(c.isMonto() ? "MONTO" : "PORCENTAJE");
        f.setValor(c.isMonto() ? c.getMontoDescuento() : c.getPorcentajeDescuento());
        f.setCompraMinima(c.getCompraMinima());
        f.setInicio(c.getFechaInicio());
        f.setVencimiento(c.getFechaVencimiento());
        f.setLimiteDeUsos(c.getLimiteDeUsos());
        f.setActivo(c.isActivo());
        model.addAttribute("cuponForm", f);
        model.addAttribute("cupon", c);
        model.addAttribute("modulo", "promociones");
        return "admin/cupon-form";
    }

    @PostMapping("/admin/cupones")
    public String guardarCupon(@Valid @ModelAttribute("cuponForm") CuponForm form, BindingResult errores, Model model,
                               RedirectAttributes flash) {
        if (!errores.hasErrors()) {
            try {
                Cupon c = cupones.guardar(form);
                flash.addFlashAttribute("mensaje", "Cupón " + c.getCodigo() + " guardado"
                        + (form.getId() == null && form.isNotificar() && form.isActivo() ? " y enviado por correo a los clientes." : "."));
                return "redirect:/admin/promociones#cupones";
            } catch (ReglaNegocioException e) {
                model.addAttribute("error", e.getMessage());
            }
        } else {
            model.addAttribute("error", "No se guardó. Revisa los campos marcados.");
        }
        model.addAttribute("modulo", "promociones");
        return "admin/cupon-form";
    }

    @PostMapping("/admin/cupones/{id}/eliminar")
    public String eliminarCupon(@PathVariable Integer id, RedirectAttributes flash) {
        Cupon c = cupones.porId(id);
        if (!c.getPedidos().isEmpty()) {
            c.setEstado(Cupon.INACTIVO);
            cupones.save(c);
            flash.addFlashAttribute("mensaje", "El cupón ya se usó en pedidos: lo desactivamos para conservar el historial.");
        } else {
            cupones.deleteById(id);
            flash.addFlashAttribute("mensaje", "Cupón eliminado.");
        }
        return "redirect:/admin/promociones#cupones";
    }
}
