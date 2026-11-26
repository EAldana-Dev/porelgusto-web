package com.neksbyte.porelgustoweb.controller.admin;

import com.neksbyte.porelgustoweb.dto.CategoriaForm;
import com.neksbyte.porelgustoweb.dto.OpcionForm;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.Categoria;
import com.neksbyte.porelgustoweb.model.Modificacion;
import com.neksbyte.porelgustoweb.model.TipoCategoria;
import com.neksbyte.porelgustoweb.service.ArregloFloralService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Configuración del catálogo: categorías, grupos, temporadas y opciones de personalización. */
@Controller
@RequestMapping("/admin/catalogo")
public class AdminCatalogoController {
    private final ArregloFloralService catalogo;

    public AdminCatalogoController(ArregloFloralService catalogo) {
        this.catalogo = catalogo;
    }

    @GetMapping
    public String inicio(@RequestParam(defaultValue = "CATEGORIA") String tipo, @RequestParam(required = false) Integer editar, Model model) {
        if ("OPCION".equals(tipo)) {
            model.addAttribute("opciones", catalogo.opcionesGenerales());
            if (!model.containsAttribute("opcionForm")) {
                OpcionForm f = new OpcionForm();
                if (editar != null) {
                    Modificacion o = catalogo.opcion(editar);
                    f.setId(o.getIdModificacion());
                    f.setNombre(o.getNombre());
                    f.setDescripcion(o.getDescripcion());
                    f.setCosto(o.getIdCosto());
                    f.setTipo(o.getTipoModificacion());
                    f.setDisponible(o.isDisponible());
                    f.setOrden(o.getOrden());
                }
                model.addAttribute("opcionForm", f);
            }
        } else {
            TipoCategoria t;
            try {
                t = TipoCategoria.valueOf(tipo);
            } catch (IllegalArgumentException e) {
                t = TipoCategoria.CATEGORIA;
                tipo = t.name();
            }
            model.addAttribute("elementos", catalogo.categoriasPorTipo(t));
            if (!model.containsAttribute("categoriaForm")) {
                CategoriaForm f = new CategoriaForm();
                f.setTipo(tipo);
                if (editar != null) {
                    Categoria c = catalogo.categoria(editar);
                    f.setId(c.getIdCategoria());
                    f.setNombre(c.getNombre());
                    f.setDescripcion(c.getDescripcion());
                    f.setInicio(c.getFechaInicio());
                    f.setFin(c.getFechaFin());
                    f.setOrden(c.getOrden());
                }
                model.addAttribute("categoriaForm", f);
            }
        }
        model.addAttribute("tipo", tipo);
        model.addAttribute("tipos", TipoCategoria.values());
        model.addAttribute("modulo", "catalogo");
        return "admin/catalogo";
    }

    @PostMapping("/categorias")
    public String guardarCategoria(@Valid @ModelAttribute("categoriaForm") CategoriaForm form, BindingResult errores, Model model,
                                   RedirectAttributes flash) {
        if (!errores.hasErrors()) {
            try {
                Categoria c = catalogo.guardarCategoria(form);
                flash.addFlashAttribute("mensaje", c.getTipo().getEtiqueta() + " «" + c.getNombre() + "» guardada. Ya se puede asignar a los productos.");
                return "redirect:/admin/catalogo?tipo=" + form.getTipo();
            } catch (ReglaNegocioException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return inicio(form.getTipo(), null, model);
    }

    @PostMapping("/categorias/{id}/eliminar")
    public String eliminarCategoria(@PathVariable Integer id, @RequestParam String tipo, RedirectAttributes flash) {
        try {
            catalogo.eliminarCategoria(id);
            flash.addFlashAttribute("mensaje", "Elemento eliminado.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/catalogo?tipo=" + tipo;
    }

    @PostMapping("/opciones")
    public String guardarOpcion(@Valid @ModelAttribute("opcionForm") OpcionForm form, BindingResult errores, Model model,
                                RedirectAttributes flash) {
        if (!errores.hasErrors()) {
            try {
                Modificacion o = catalogo.guardarOpcion(form);
                flash.addFlashAttribute("mensaje", "Opción «" + o.getNombre() + "» guardada.");
                return "redirect:/admin/catalogo?tipo=OPCION";
            } catch (ReglaNegocioException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return inicio("OPCION", null, model);
    }

    @PostMapping("/opciones/{id}/eliminar")
    public String eliminarOpcion(@PathVariable Integer id, RedirectAttributes flash) {
        catalogo.eliminarOpcion(id);
        flash.addFlashAttribute("mensaje", "Opción eliminada.");
        return "redirect:/admin/catalogo?tipo=OPCION";
    }
}
