package servicios;

import comercio.Carrito;
import dominio.Cliente;

import java.util.*;

public class CartService {
    private final Map<String, Carrito> carritoPorCliente = new HashMap<>(); // clienteId -> carrito

    public Carrito obtenerCarrito(Cliente c) {
        return carritoPorCliente.computeIfAbsent(c.getId(), k -> new Carrito(UUID.randomUUID().toString()));
    }
}
