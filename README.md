<h1 align="center"> GlowUp — POO en Java (Consola)</h1>

<p align="center">
  App de consola para una tienda de cosméticos: catálogo, carrito y compras.<br/>
  Demuestra <b>abstracción, herencia, polimorfismo, encapsulamiento, interfaces</b>, lambdas y colecciones.
</p>

<p align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-17+-red?logo=java" />
  <img alt="Status" src="https://img.shields.io/badge/status-demo-green" />
  <img alt="Type" src="https://img.shields.io/badge/type-académico-blue" />
</p>

---

## Funcionalidades

- 👤 **Usuarios**: registro y log-in (Cliente).  
- 🧾 **Productos**: alta (AdminContenido/Dueña) y listado.  
- 🛒 **Carrito**: agregar ítems y **checkout** (genera compra y descuenta stock).  
- 🔐 **Roles**: `Cliente`, `AdministradorContenido`, `AdministradorUsuario`, `Duena`, `DesarrolladorProducto`.  
- 🕴️ **Operaciones** (submenú solo para `Duena`):  
  - `Fabrica` (planta física)  
  - `TrabajadorEsclavizado` (asignación a fábrica)  
  - `RegistroEsclavos` (control exclusivo de `Duena`)  
  - `ConsejoSombrio` (miembros: AdminContenido/AdminUsuario)

> Proyecto **sin persistencia**: los datos viven en memoria (se crean “seed” al iniciar) para concentrarse en el diseño POO.

---
## 🧱 Paquetes

| Paquete     | Clases principales |
|-------------|--------------------|
| **app**     | Main |
| **dominio** | Usuario (abstracta), Cliente, AdministradorContenido, AdministradorUsuario, Duena, DesarrolladorProducto, Rol |
| **comercio**| Producto, Categoria, Carrito, LineaCarrito, Compra, LineaCompra, EstadoCompra |
| **pago**    | MetodoPago, TipoMetodoPago |
| **servicios** | AuthService, ProductService, CartService, PurchaseService, FabricaService, TrabajadorService, ConsejoSombrioService, RegistroEsclavosService |
| **produccion** | Fabrica |
| **operaciones** | TrabajadorEsclavizado, RegistroEsclavos, ConsejoSombrio |


