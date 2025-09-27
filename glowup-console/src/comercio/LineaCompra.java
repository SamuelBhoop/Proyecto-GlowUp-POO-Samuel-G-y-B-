package comercio;

public class LineaCompra {
    private final Producto producto;
    private final int cantidad;
    private final double precioUnit;

    public LineaCompra(Producto producto, int cantidad, double precioUnit) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnit = precioUnit;
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnit() { return precioUnit; }
    public double getSubtotal() { return precioUnit * cantidad; }
}
