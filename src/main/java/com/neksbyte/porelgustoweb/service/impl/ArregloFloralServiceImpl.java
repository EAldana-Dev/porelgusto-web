package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.ArregloFloralDao;
import com.neksbyte.porelgustoweb.dao.ContenidoCarritoDao;
import com.neksbyte.porelgustoweb.dao.ImagenDao;
import com.neksbyte.porelgustoweb.dao.ModificacionDao;
import com.neksbyte.porelgustoweb.dto.*;
import com.neksbyte.porelgustoweb.exception.NoEncontradoException;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.*;
import com.neksbyte.porelgustoweb.repository.CategoriaRepository;
import com.neksbyte.porelgustoweb.service.ArregloFloralService;
import com.neksbyte.porelgustoweb.service.ListaDeDeseosService;
import com.neksbyte.porelgustoweb.service.PromocionService;
import com.neksbyte.porelgustoweb.service.Textos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArregloFloralServiceImpl extends BaseServiceImpl<ArregloFloral, Integer> implements ArregloFloralService {

    private final ArregloFloralDao arreglos;
    private final CategoriaRepository categorias;
    private final ModificacionDao modificaciones;
    private final ImagenDao imagenes;
    private final ContenidoCarritoDao contenidos;
    private final PromocionService promociones;
    private final ListaDeDeseosService deseos;

    public ArregloFloralServiceImpl(ArregloFloralDao dao, CategoriaRepository categorias, ModificacionDao modificaciones,
                                    ImagenDao imagenes, ContenidoCarritoDao contenidos, PromocionService promociones,
                                    ListaDeDeseosService deseos) {
        super(dao);
        this.arreglos = dao;
        this.categorias = categorias;
        this.modificaciones = modificaciones;
        this.imagenes = imagenes;
        this.contenidos = contenidos;
        this.promociones = promociones;
        this.deseos = deseos;
    }

    // ======================= Consulta =======================

    /** Productos que se muestran en la tienda (activos y que no son de prueba). */
    private List<ArregloFloral> activos() {
        return arreglos.findAll().stream().filter(ArregloFloral::isVisibleEnCatalogo).toList();
    }

    /** Orden de las categorías del menú (por nombre). */
    private Map<String, Integer> ordenCategorias() {
        Map<String, Integer> m = new HashMap<>();
        for (Categoria c : categorias.findByTipoOrderByOrdenAscNombreAsc(TipoCategoria.CATEGORIA)) m.put(Textos.slug(c.getNombre()), c.getOrden());
        return m;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoVista> buscar(FiltroCatalogo f) {
        List<Promocion> vigentes = promociones.vigentes();
        String q = Textos.normalizar(f.getQ());
        List<String> palabras = q.isEmpty() ? List.of() : Arrays.asList(q.split("\\s+"));
        Set<String> etiquetas = f.getEtiqueta().stream().map(Textos::normalizar).collect(Collectors.toSet());
        List<ProductoVista> res = activos().stream()
                .filter(p -> palabras.isEmpty() || palabras.stream().allMatch(w -> textoBusqueda(p).contains(w)))
                .filter(p -> Textos.vacio(f.getCategoria()) || p.getCategoriaSlug().equals(f.getCategoria()))
                .filter(p -> Textos.vacio(f.getColeccion()) || p.getColecciones().stream().anyMatch(c -> f.getColeccion().equals(c.getSlug())))
                .filter(p -> etiquetas.isEmpty() || p.getEtiquetas().stream().map(Textos::normalizar).anyMatch(etiquetas::contains))
                .filter(p -> !f.isDisponibles() || p.isDisponible())
                .map(p -> new ProductoVista(p, promociones.mejorOferta(p, vigentes)))
                .filter(v -> !f.isOfertas() || v.isConDescuento())
                .filter(v -> f.getPrecioMin() == null || v.getPrecioFinal().compareTo(f.getPrecioMin()) >= 0)
                .filter(v -> f.getPrecioMax() == null || v.getPrecioFinal().compareTo(f.getPrecioMax()) <= 0)
                .collect(Collectors.toCollection(ArrayList::new));
        Map<String, Integer> orden = ordenCategorias();
        Comparator<ProductoVista> criterio = switch (f.getOrden()) {
            case "precio-asc" -> Comparator.comparing(ProductoVista::getPrecioFinal);
            case "precio-desc" -> Comparator.comparing(ProductoVista::getPrecioFinal).reversed();
            case "nombre" -> Comparator.comparing(v -> Textos.normalizar(v.getProducto().getNombre()));
            case "novedades" -> Comparator.comparing((ProductoVista v) -> v.getProducto().getFechaAlta(),
                    Comparator.nullsFirst(Comparator.naturalOrder())).reversed();
            case "populares" -> Comparator.comparingLong((ProductoVista v) -> v.getProducto().getVentas() * 20L + v.getProducto().getVistas()).reversed();
            default -> Comparator.comparing((ProductoVista v) -> orden.getOrDefault(v.getProducto().getCategoriaSlug(), 99))
                    .thenComparing(v -> v.getProducto().getSku() == null ? "" : v.getProducto().getSku());
        };
        res.sort(Comparator.comparing((ProductoVista v) -> !v.getProducto().isDisponible()).thenComparing(criterio));
        return res;
    }

    private String textoBusqueda(ArregloFloral p) {
        return Textos.normalizar(p.getNombre() + " " + p.getDetalles() + " " + p.getSku() + " "
                + p.getCategoria() + " " + p.getEtiquetasTexto());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoVista vista(ArregloFloral p) {
        return new ProductoVista(p, promociones.mejorOferta(p, promociones.vigentes()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoVista> vistas(Collection<ArregloFloral> lista) {
        List<Promocion> vigentes = promociones.vigentes();
        return lista.stream().map(p -> new ProductoVista(p, promociones.mejorOferta(p, vigentes))).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ArregloFloral porSku(String sku) {
        return arreglos.findBySku(sku == null ? "" : sku.trim().toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new NoEncontradoException("No encontramos el producto " + sku));
    }

    @Override
    @Transactional(readOnly = true)
    public ArregloFloral porId(Integer id) {
        return arreglos.findById(id).orElseThrow(() -> new NoEncontradoException("No encontramos el producto"));
    }

    @Override
    public void registrarVista(ArregloFloral p) { p.setVistas(p.getVistas() + 1); }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoVista> relacionados(ArregloFloral p, int max) {
        return vistas(activos().stream()
                .filter(x -> !x.equals(p) && x.isDisponible() && Objects.equals(x.getCategoria(), p.getCategoria()))
                .sorted(Comparator.comparingInt(ArregloFloral::getVentas).reversed())
                .limit(max).toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> categoriasMenu() { return categorias.findByTipoOrderByOrdenAscNombreAsc(TipoCategoria.CATEGORIA); }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> colecciones() {
        List<Categoria> res = new ArrayList<>(categorias.findByTipoOrderByOrdenAscNombreAsc(TipoCategoria.TEMPORADA));
        res.addAll(categorias.findByTipoOrderByOrdenAscNombreAsc(TipoCategoria.GRUPO));
        return res.stream().filter(Categoria::isVigente).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria categoriaPorSlug(String slug) { return Textos.vacio(slug) ? null : categorias.findBySlug(slug).orElse(null); }

    @Override
    @Transactional(readOnly = true)
    public List<String> etiquetas() {
        return activos().stream().flatMap(p -> p.getEtiquetas().stream()).distinct()
                .sorted(Comparator.comparing(Textos::normalizar)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal precioMaximo() {
        return activos().stream().map(ArregloFloral::getCosto).filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(BigDecimal.valueOf(5000));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Modificacion> opcionesPara(ArregloFloral p) {
        if (!p.isPersonalizable()) return List.of();
        return modificaciones.findAll().stream()
                .filter(m -> m.isGeneral() || p.equals(m.getArregloFloral()))
                .sorted(Comparator.comparing(Modificacion::isGeneral).thenComparingInt(Modificacion::getOrden).thenComparing(Modificacion::getNombre))
                .toList();
    }

    // ======================= Administración de productos =======================

    @Override
    @Transactional(readOnly = true)
    public List<ArregloFloral> todosLosProductos() { return arreglos.findAllRecientes(); }

    @Override
    public ArregloFloral guardarProducto(ProductoForm f, String imagenUrl) {
        boolean nuevo = f.getId() == null;
        ArregloFloral p = nuevo ? new ArregloFloral() : porId(f.getId());
        if (nuevo && imagenUrl == null) throw new ReglaNegocioException("Sube una imagen del producto");
        Categoria cat = categorias.findFirstByNombreIgnoreCase(f.getCategoria())
                .filter(c -> c.getTipo() == TipoCategoria.CATEGORIA)
                .orElseThrow(() -> new ReglaNegocioException("Elige una categoría válida"));
        boolean estabaDisponible = !nuevo && p.isDisponible();
        if (nuevo) {
            p.setSku(siguienteSku());
            p.setFechaAlta(LocalDateTime.now());
            p.setVentas(0);
            p.setVistas(0L);
            p.setIdTipoArreglo(1);
        }
        p.setNombre(f.getNombre().trim());
        p.setDetalles(Textos.limpio(f.getDescripcion()));
        p.setCosto(f.getPrecio());
        p.setCategoria(cat.getNombre());
        p.setEtiquetas(Arrays.stream(Optional.ofNullable(f.getEtiquetas()).orElse("").split(","))
                .map(String::trim).filter(s -> !s.isEmpty())
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1)).toList());
        p.setColecciones(new HashSet<>(categorias.findAllById(f.getColecciones())));
        p.setActivo(f.isActivo());
        p.setPersonalizable(f.isPersonalizable());
        p.setPermiteQR(f.isPermiteQR());
        p.setPrueba(f.isPrueba());
        p.setExistencias(f.getExistencias());
        p.setPuntoReorden(f.getPuntoReorden());
        arreglos.save(p);
        if (imagenUrl != null) {
            Imagen img = p.getImagenes().isEmpty() ? new Imagen() : p.getImagenes().get(0);
            img.setNombre(p.getNombre());
            img.setDireccionImg(imagenUrl);
            img.setArregloFloral(p);
            imagenes.save(img);
            if (p.getImagenes().isEmpty()) p.getImagenes().add(img);
        }
        // Si vuelve a estar disponible, se avisa a quien lo tenía en su lista de deseos.
        if (!nuevo && !estabaDisponible && p.isDisponible()) deseos.avisarDisponible(p);
        return p;
    }

    private String siguienteSku() {
        int max = arreglos.findAll().stream().map(ArregloFloral::getSku)
                .filter(s -> s != null && s.matches("PG-\\d+"))
                .mapToInt(s -> Integer.parseInt(s.substring(3))).max().orElse(0);
        return String.format("PG-%03d", max + 1);
    }

    @Override
    public boolean eliminarProducto(Integer id) {
        ArregloFloral p = porId(id);
        if (contenidos.existsByArreglo(p)) {
            p.setActivo(false);
            return false;
        }
        deseos.quitarDeTodas(p);
        promociones.todas().forEach(pr -> pr.getProductos().remove(p));
        p.getColecciones().clear();
        p.getImagenes().forEach(i -> imagenes.deleteById(i.getIdImagen()));
        modificaciones.findAll().stream().filter(m -> p.equals(m.getArregloFloral())).forEach(m -> modificaciones.deleteById(m.getIdModificacion()));
        arreglos.deleteById(p.getIdArregloFloral());
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArregloFloral> bajoInventario() {
        return arreglos.findAll().stream().filter(p -> p.isActivo() && p.isBajoInventario())
                .sorted(Comparator.comparingInt(ArregloFloral::getExistencias)).toList();
    }

    // ======================= Categorías y personalización =======================

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> categoriasPorTipo(TipoCategoria tipo) { return categorias.findByTipoOrderByOrdenAscNombreAsc(tipo); }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> todasLasCategorias() {
        List<Categoria> res = new ArrayList<>();
        for (TipoCategoria t : TipoCategoria.values()) res.addAll(categoriasPorTipo(t));
        return res;
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria categoria(Integer id) {
        return categorias.findById(id).orElseThrow(() -> new NoEncontradoException("No existe el elemento"));
    }

    @Override
    public Categoria guardarCategoria(CategoriaForm f) {
        TipoCategoria tipo = TipoCategoria.valueOf(f.getTipo());
        if (f.getInicio() != null && f.getFin() != null && f.getFin().isBefore(f.getInicio())) {
            throw new ReglaNegocioException("La fecha de fin debe ser posterior a la de inicio");
        }
        String slug = Textos.slug(f.getNombre());
        categorias.findBySlug(slug).filter(c -> !c.getIdCategoria().equals(f.getId()))
                .ifPresent(c -> { throw new ReglaNegocioException("Ya existe un elemento llamado «" + f.getNombre().trim() + "»"); });
        Categoria c = f.getId() == null ? new Categoria() : categoria(f.getId());
        String nombreAnterior = c.getNombre();
        c.setNombre(f.getNombre().trim());
        c.setSlug(slug);
        c.setDescripcion(Textos.limpio(f.getDescripcion()));
        c.setTipo(tipo);
        c.setFechaInicio(tipo == TipoCategoria.CATEGORIA ? null : f.getInicio());
        c.setFechaFin(tipo == TipoCategoria.CATEGORIA ? null : f.getFin());
        c.setOrden(f.getOrden());
        categorias.save(c);
        // Si se renombró una categoría, sus productos la siguen teniendo.
        if (tipo == TipoCategoria.CATEGORIA && nombreAnterior != null && !nombreAnterior.equals(c.getNombre())) {
            arreglos.findAll().stream().filter(p -> nombreAnterior.equalsIgnoreCase(p.getCategoria()))
                    .forEach(p -> p.setCategoria(c.getNombre()));
        }
        return c;
    }

    @Override
    public void eliminarCategoria(Integer id) {
        Categoria c = categoria(id);
        if (c.getTipo() == TipoCategoria.CATEGORIA
                && arreglos.findAll().stream().anyMatch(p -> c.getNombre().equalsIgnoreCase(p.getCategoria()))) {
            throw new ReglaNegocioException("No se puede eliminar: hay productos en esta categoría. Muévelos primero.");
        }
        arreglos.findAll().forEach(p -> p.getColecciones().remove(c));
        promociones.todas().stream().filter(pr -> c.equals(pr.getCategoria())).forEach(pr -> pr.setCategoria(null));
        categorias.delete(c);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Modificacion> opcionesGenerales() {
        return modificaciones.findAll().stream()
                .sorted(Comparator.comparing(Modificacion::isGeneral).reversed()
                        .thenComparingInt(Modificacion::getOrden).thenComparing(Modificacion::getNombre))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Modificacion opcion(Integer id) {
        return modificaciones.findById(id).orElseThrow(() -> new NoEncontradoException("No existe la opción"));
    }

    @Override
    public Modificacion guardarOpcion(OpcionForm f) {
        modificaciones.findAll().stream()
                .filter(o -> o.isGeneral() && o.getNombre().equalsIgnoreCase(f.getNombre().trim()) && !o.getIdModificacion().equals(f.getId()))
                .findAny().ifPresent(o -> { throw new ReglaNegocioException("Ya existe una opción llamada «" + f.getNombre().trim() + "»"); });
        Modificacion o = f.getId() == null ? new Modificacion() : opcion(f.getId());
        o.setNombre(f.getNombre().trim());
        o.setDescripcion(Textos.limpio(f.getDescripcion()));
        o.setIdCosto(f.getCosto());
        o.setTipoModificacion(Textos.vacio(f.getTipo()) ? "Extra" : f.getTipo().trim());
        o.setDisponible(f.isDisponible());
        o.setOrden(f.getOrden());
        return modificaciones.save(o);
    }

    @Override
    public void eliminarOpcion(Integer id) { modificaciones.deleteById(opcion(id).getIdModificacion()); }
}
