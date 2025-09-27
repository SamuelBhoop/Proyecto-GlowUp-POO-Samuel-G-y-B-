package servicios;

import comercio.*;
import dominio.Cliente;
import pago.MetodoPago;

import java.util.*;

public class PurchaseService {
    private final ProductService productos;
    private final List<Compra> historial = new ArrayList<>();

    public PurchaseService(ProductService productos) { this.productos = productos; }

    public Compra checkout(Cliente cliente, Carrito carrito, MetodoPago mp) {
        List<LineaCompra> lineasCompra = new ArrayList<>();
        for (LineaCarrito lc : carrito.getLineas()) {
            Producto p = productos.buscarPorId(lc.getProducto().getId());
            if (p.getStock() < lc.getCantidad())
                throw new RuntimeException("Stock insuficiente para " + p.getNombre());
            p.setStock(p.getStock() - lc.getCantidad());
            lineasCompra.add(new LineaCompra(p, lc.getCantidad(), p.getPrecio()));
        }
        Compra compra = new Compra(UUID.randomUUID().toString(), cliente, lineasCompra, mp);
        historial.add(compra);
        carrito.limpiar();
        return compra;
    }

    public List<Compra> getHistorial() { return Collections.unmodifiableList(historial); }
}
