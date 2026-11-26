package com.neksbyte.porelgustoweb.controller.admin;

import com.neksbyte.porelgustoweb.config.TiendaConfig;
import com.neksbyte.porelgustoweb.dto.ProductoForm;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.model.Categoria;
import com.neksbyte.porelgustoweb.model.TipoCategoria;
import com.neksbyte.porelgustoweb.service.ArregloFloralService;
import com.neksbyte.porelgustoweb.service.Textos;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Productos e inventario: alta, edición (con imagen, existencias y etiquetas) y eliminación. */
@Controller
@RequestMapping("/admin/productos")
public class AdminProductosController {
    private static final Map<String, String> TIPOS = Map.of("image/jpeg", "jpg", "image/png", "png", "image/webp", "webp");
    private final ArregloFloralService catalogo;
    private final TiendaConfig tienda;

    public AdminProductosController(ArregloFloralService catalogo, TiendaConfig tienda) {
        this.catalogo = catalogo;
        this.tienda = tienda;
    }

    @GetMapping
    public String lista(@RequestParam(required = false) String q, Model model) {
        String filtro = Textos.normalizar(q);
        model.addAttribute("productos", catalogo.todosLosProductos().stream()
                .filter(p -> filtro.isEmpty() || Textos.normalizar(p.getNombre() + " " + p.getSku()).contains(filtro))
                .toList());
        model.addAttribute("q", q);
        model.addAttribute("modulo", "productos");
        return "admin/productos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("productoForm", new ProductoForm());
        return formulario(model, null);
    }

    @GetMapping("/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        ArregloFloral p = catalogo.porId(id);
        ProductoForm f = new ProductoForm();
        f.setId(p.getIdArregloFloral());
        f.setNombre(p.getNombre());
        f.setDescripcion(p.getDetalles());
        f.setPrecio(p.getCosto());
        f.setCategoria(p.getCategoria());
        f.setColecciones(p.getColecciones().stream().map(Categoria::getIdCategoria).toList());
        f.setEtiquetas(p.getEtiqueta());
        f.setExistencias(p.getExistencias());
        f.setPuntoReorden(p.getPuntoReorden());
        f.setActivo(p.isActivo());
        f.setPersonalizable(p.isPersonalizable());
        f.setPermiteQR(p.isPermiteQR());
        f.setPrueba(p.isPrueba());
        model.addAttribute("productoForm", f);
        return formulario(model, p);
    }

    private String formulario(Model model, ArregloFloral p) {
        model.addAttribute("producto", p);
        model.addAttribute("categorias", catalogo.categoriasPorTipo(TipoCategoria.CATEGORIA));
        List<Categoria> col = new ArrayList<>(catalogo.categoriasPorTipo(TipoCategoria.TEMPORADA));
        col.addAll(catalogo.categoriasPorTipo(TipoCategoria.GRUPO));
        model.addAttribute("colecciones", col);
        model.addAttribute("modulo", "productos");
        return "admin/producto-form";
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute("productoForm") ProductoForm form, BindingResult errores,
                          @RequestParam(required = false) MultipartFile imagen, Model model, RedirectAttributes flash) {
        ArregloFloral actual = form.getId() == null ? null : catalogo.porId(form.getId());
        if (!errores.hasErrors()) {
            try {
                String url = guardarImagen(imagen);
                ArregloFloral p = catalogo.guardarProducto(form, url);
                flash.addFlashAttribute("mensaje", "Producto «" + p.getNombre() + "» guardado (" + p.getSku() + "). Ya aparece en el catálogo.");
                return "redirect:/admin/productos";
            } catch (ReglaNegocioException e) {
                model.addAttribute("error", e.getMessage());
            }
        } else {
            model.addAttribute("error", "No se guardó. Revisa los campos marcados.");
        }
        return formulario(model, actual);
    }

    /** Valida el tipo (JPG, PNG o WEBP por su contenido real) y guarda la imagen en la carpeta de uploads. */
    private String guardarImagen(MultipartFile imagen) {
        if (imagen == null || imagen.isEmpty()) return null;
        String ext = TIPOS.get(imagen.getContentType());
        if (ext == null || !firmaValida(imagen, ext)) throw new ReglaNegocioException("Imagen no permitida: sube un archivo JPG, PNG o WEBP.");
        try {
            Path carpeta = Path.of(tienda.getUploadsDir(), "productos");
            Files.createDirectories(carpeta);
            String nombre = UUID.randomUUID() + "." + ext;
            try (InputStream in = imagen.getInputStream()) {
                Files.copy(in, carpeta.resolve(nombre));
            }
            return "/uploads/productos/" + nombre;
        } catch (IOException e) {
            throw new ReglaNegocioException("No se pudo guardar la imagen. Intenta de nuevo.");
        }
    }

    private boolean firmaValida(MultipartFile f, String ext) {
        try (InputStream in = f.getInputStream()) {
            byte[] b = in.readNBytes(12);
            if (b.length < 12) return false;
            return switch (ext) {
                case "jpg" -> (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8;
                case "png" -> (b[0] & 0xFF) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G';
                case "webp" -> b[0] == 'R' && b[1] == 'I' && b[2] == 'F' && b[3] == 'F' && b[8] == 'W' && b[9] == 'E' && b[10] == 'B' && b[11] == 'P';
                default -> false;
            };
        } catch (IOException e) {
            return false;
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, RedirectAttributes flash) {
        boolean borrado = catalogo.eliminarProducto(id);
        flash.addFlashAttribute("mensaje", borrado
                ? "Producto eliminado del catálogo y de la base de datos."
                : "El producto tiene pedidos registrados: lo desactivamos para conservar el historial. Ya no aparece en el catálogo.");
        return "redirect:/admin/productos";
    }
}
