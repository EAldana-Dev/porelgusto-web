package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.dto.CategoriaForm;
import com.neksbyte.porelgustoweb.dto.FiltroCatalogo;
import com.neksbyte.porelgustoweb.dto.OpcionForm;
import com.neksbyte.porelgustoweb.dto.ProductoForm;
import com.neksbyte.porelgustoweb.dto.ProductoVista;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.model.Categoria;
import com.neksbyte.porelgustoweb.model.Modificacion;
import com.neksbyte.porelgustoweb.model.TipoCategoria;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/** Catálogo de arreglos florales: consulta, filtros y administración. */
public interface ArregloFloralService extends BaseService<ArregloFloral, Integer> {
    // ---------- Consulta ----------
    List<ProductoVista> buscar(FiltroCatalogo filtro);
    ProductoVista vista(ArregloFloral p);
    List<ProductoVista> vistas(Collection<ArregloFloral> productos);
    ArregloFloral porSku(String sku);
    ArregloFloral porId(Integer id);
    void registrarVista(ArregloFloral p);
    List<ProductoVista> relacionados(ArregloFloral p, int max);
    List<Categoria> categoriasMenu();
    List<Categoria> colecciones();
    Categoria categoriaPorSlug(String slug);
    List<String> etiquetas();
    BigDecimal precioMaximo();
    /** Opciones de personalización que se ofrecen para el producto. */
    List<Modificacion> opcionesPara(ArregloFloral p);

    // ---------- Administración de productos ----------
    List<ArregloFloral> todosLosProductos();
    ArregloFloral guardarProducto(ProductoForm form, String imagenUrl);
    /** Elimina el producto; si ya se vendió solo se desactiva. Regresa true si se borró. */
    boolean eliminarProducto(Integer id);
    List<ArregloFloral> bajoInventario();

    // ---------- Categorías, colecciones y personalización ----------
    List<Categoria> categoriasPorTipo(TipoCategoria tipo);
    List<Categoria> todasLasCategorias();
    Categoria categoria(Integer id);
    Categoria guardarCategoria(CategoriaForm form);
    void eliminarCategoria(Integer id);
    List<Modificacion> opcionesGenerales();
    Modificacion opcion(Integer id);
    Modificacion guardarOpcion(OpcionForm form);
    void eliminarOpcion(Integer id);
}
