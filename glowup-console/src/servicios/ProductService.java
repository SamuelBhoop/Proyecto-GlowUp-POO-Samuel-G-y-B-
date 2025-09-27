package servicios;

import comercio.Categoria;
import comercio.Producto;

import java.util.*;

public class ProductService {
    private final Map<String, Producto> productos = new HashMap<>(); // id -> producto
    private final Map<String, Categoria> categoriasPorNombre = new HashMap<>(); // nombre -> categoria

    public void agregarProducto(Producto p) {
        if (productos.containsKey(p.getId())) throw new RuntimeException("ID de producto en uso");
        productos.put(p.getId(), p);
    }

    public List<Producto> listarOrdenadosPorNombre() {
        return productos.values().stream()
                .sorted(Comparator.comparing(Producto::getNombre)) // lambda
                .toList();
    }

    public Producto buscarPorId(String id) {
        Producto p = productos.get(id);
        if (p == null) throw new RuntimeException("Producto no encontrado");
        return p;
    }

    public Categoria obtenerOCrearCategoria(String nombre, String desc) {
        return categoriasPorNombre.computeIfAbsent(nombre.toLowerCase(), k ->
                new Categoria(UUID.randomUUID().toString(), nombre, desc));
    }
}
