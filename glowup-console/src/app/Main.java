package app;

import comercio.*;
import dominio.*;
import pago.*;
import servicios.*;
import produccion.*;
import operaciones.*;

import java.time.LocalDate;
import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        AuthService auth = new AuthService();
        ProductService productos = new ProductService();
        CartService carritos = new CartService();
        PurchaseService compras = new PurchaseService(productos);
        FabricaService fabricas = new FabricaService();
        TrabajadorService trabajadores = new TrabajadorService();
        ConsejoSombrioService consejos = new ConsejoSombrioService();
        RegistroEsclavosService registroSrv = new RegistroEsclavosService();

        Duena duena = seedData(auth, productos);
        ProduccionService produccion = new ProduccionService();
        OperacionesService operaciones = new OperacionesService(duena);

        Usuario usuarioActual = null;

        while (true) {
            System.out.println("\n==== GLOW UP (consola) ====");
            System.out.println("1) Registrar usuario (Cliente)");
            System.out.println("2) Log in");
            System.out.println("3) Agregar producto (AdminContenido/Dueña)");
            System.out.println("4) Listar productos");
            System.out.println("5) Agregar producto a mi carrito (Cliente)");
            System.out.println("6) Ver mi carrito");
            System.out.println("7) Registrar compra (checkout)");
            System.out.println("8) Módulo académico (Solo Dueña)");
            System.out.println("0) Salir");
            System.out.print("Opción: ");
            String op = sc.nextLine().trim();

            try {
                switch (op) {
                    case "1" -> registrarCliente(auth);
                    case "2" -> usuarioActual = login(auth);
                    case "3" -> agregarProducto(usuarioActual, productos);
                    case "4" -> listarProductos(productos);
                    case "5" -> agregarAlCarrito(usuarioActual, productos, carritos);
                    case "6" -> verCarrito(usuarioActual, carritos);
                    case "7" -> checkout(usuarioActual, carritos, compras);
                    case "8" -> moduloAcademico(usuarioActual, duena, produccion, operaciones, auth);
                    case "0" -> { System.out.println("Adiós!"); return; }
                    default -> System.out.println("Opción no válida");
                }
            } catch (RuntimeException ex) {
                System.out.println("⚠ " + ex.getMessage());
            }
        }
    }

    private static void registrarCliente(AuthService auth) {
        System.out.println("-- Registro de Cliente --");
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();
        System.out.print("Password: "); String pass = sc.nextLine();
        System.out.print("Dirección envío: "); String dir = sc.nextLine();
        System.out.print("Teléfono: "); String tel = sc.nextLine();

        Cliente c = new Cliente(UUID.randomUUID().toString(), nombre, email, pass, dir, tel);
        auth.registrar(c);
        System.out.println("✔ Cliente registrado");
    }

    private static Usuario login(AuthService auth) {
        System.out.println("-- Log in --");
        System.out.print("Email: "); String email = sc.nextLine();
        System.out.print("Password: "); String pass = sc.nextLine();
        Usuario u = auth.login(email, pass);
        System.out.println("✔ Bienvenido, " + u.getNombre() + " (" + u.getRol() + ")");
        return u;
    }

    private static void agregarProducto(Usuario u, ProductService productos) {
        if (u == null || !(u instanceof AdministradorContenido || u instanceof Duena)) {
            throw new RuntimeException("Debes iniciar sesión como AdminContenido o Dueña");
        }
        System.out.println("-- Agregar producto --");
        System.out.println("Nombre: "); String nombre = sc.nextLine();
        System.out.println("Descripción: "); String desc = sc.nextLine();
        System.out.println("Precio: "); double precio = Double.parseDouble(sc.nextLine());
        System.out.println("Stock: "); int stock = Integer.parseInt(sc.nextLine());
        System.out.println("Categoría: "); String catNombre = sc.nextLine();

        Categoria cat = productos.obtenerOCrearCategoria(catNombre, "");
        Producto p = new Producto(UUID.randomUUID().toString(), nombre, desc, precio, stock,
                LocalDate.now(), cat);
        productos.agregarProducto(p);
        System.out.println("✔ Producto creado: " + p.getNombre() + " | ID: " + p.getId());
    }

    private static void listarProductos(ProductService productos) {
        System.out.println("-- Catálogo --");
        productos.listarOrdenadosPorNombre()
                .forEach(p -> System.out.println(p.resumen()));
    }

    private static void agregarAlCarrito(Usuario u, ProductService productos, CartService carritos) {
        if (!(u instanceof Cliente c)) {
            throw new RuntimeException("Debes iniciar sesión como Cliente");
        }
        System.out.print("ID producto: "); String id = sc.nextLine();
        Producto p = productos.buscarPorId(id);
        System.out.print("Cantidad: "); int cant = Integer.parseInt(sc.nextLine());
        carritos.obtenerCarrito(c).agregarProducto(p, cant);
        System.out.println("✔ Agregado. Subtotal actual: $" + carritos.obtenerCarrito(c).getTotal());
    }

    private static void verCarrito(Usuario u, CartService carritos) {
        if (!(u instanceof Cliente c)) throw new RuntimeException("Debes ser Cliente");
        Carrito carrito = carritos.obtenerCarrito(c);
        System.out.println("-- Mi carrito --");
        carrito.getLineas().forEach(System.out::println);
        System.out.println("TOTAL: $" + carrito.getTotal());
    }

    private static void checkout(Usuario u, CartService carritos, PurchaseService compras) {
        if (!(u instanceof Cliente c)) throw new RuntimeException("Debes ser Cliente");
        Carrito carrito = carritos.obtenerCarrito(c);
        if (carrito.getLineas().isEmpty()) throw new RuntimeException("Carrito vacío");
        // metodo de pago minimo
        MetodoPago mp = new MetodoPago(UUID.randomUUID().toString(), TipoMetodoPago.TARJETA,
                c.getNombre(), "****-****-****-1234");
        Compra compra = compras.checkout(c, carrito, mp);
        System.out.println("✔ Compra registrada #" + compra.getId() + ". Total $" + compra.getTotal());
    }

    private static Duena seedData(AuthService auth, ProductService prodSrv) {
        Duena duena = new Duena("duena-1", "Cabrita Sakura", "sakura@glowup.com", "1234", "CLAVE-ULTRA", LocalDate.now());
        auth.registrar(duena);
        auth.registrar(new AdministradorContenido("editor-1", "Editor", "editor@glowup.com", "1234", Set.of("CREATE","UPDATE","DELETE")));
        auth.registrar(new AdministradorUsuario("admin-usr-1", "AdminUsr", "adminusr@glowup.com", "1234", 3));

        Categoria labiales = prodSrv.obtenerOCrearCategoria("Labiales", "Labiales mate y gloss");
        Categoria bases = prodSrv.obtenerOCrearCategoria("Bases", "Larga duración");
        prodSrv.agregarProducto(new Producto(UUID.randomUUID().toString(), "Labial Mate #21", "Rojo intenso", 29999, 20, LocalDate.now(), labiales));
        prodSrv.agregarProducto(new Producto(UUID.randomUUID().toString(), "Base HD", "Cobertura media", 49999, 10, LocalDate.now(), bases));

        return duena;
    }

    private static void moduloAcademico(Usuario actual, Duena duena, ProduccionService prodSrv, OperacionesService opSrv, AuthService auth) {
        if (!(actual instanceof Duena)) {
            throw new RuntimeException("Acceso restringido: solo Dueña");
        }
        while (true) {
            System.out.println("\n-- Módulo académico --");
            System.out.println("1) Crear fábrica");
            System.out.println("2) Listar fábricas");
            System.out.println("3) Registrar trabajador");
            System.out.println("4) Listar trabajadores");
            System.out.println("5) Asignar trabajador a fábrica");
            System.out.println("6) Crear/Mostrar Consejo Sombrio");
            System.out.println("7) Agregar miembro al Consejo (email)");
            System.out.println("0) Volver");
            System.out.print("Opción: ");
            String op = sc.nextLine().trim();
            try {
                switch (op) {
                    case "1" -> crearFabrica(prodSrv);
                    case "2" -> listarFabricas(prodSrv);
                    case "3" -> registrarTrabajador(duena, opSrv);
                    case "4" -> listarTrabajadores(duena, opSrv);
                    case "5" -> asignarTrabajador(duena, prodSrv, opSrv);
                    case "6" -> mostrarConsejo(opSrv);
                    case "7" -> agregarMiembroConsejo(auth, opSrv);
                    case "0" -> { return; }
                    default -> System.out.println("Opción no válida");
                }
            } catch (Exception e) {
                System.out.println("⚠ " + e.getMessage());
            }
        }
    }

    private static void crearFabrica(ProduccionService prodSrv) {
        System.out.print("ID fábrica: "); String id = sc.nextLine();
        System.out.print("País: "); String pais = sc.nextLine();
        System.out.print("Ciudad: "); String ciudad = sc.nextLine();
        System.out.print("Capacidad: "); int cap = Integer.parseInt(sc.nextLine());
        System.out.print("Nivel automatización: "); String nivel = sc.nextLine();
        Fabrica f = prodSrv.crearFabrica(id, pais, ciudad, cap, nivel);
        System.out.println("✔ Creada " + f);
    }

    private static void listarFabricas(ProduccionService prodSrv) {
        System.out.println("-- Fábricas --");
        prodSrv.listar().forEach(System.out::println);
    }

    private static void registrarTrabajador(Duena duena, OperacionesService opSrv) {
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("País origen: "); String pais = sc.nextLine();
        System.out.print("Edad: "); int edad = Integer.parseInt(sc.nextLine());
        System.out.print("Salud: "); String salud = sc.nextLine();
        var t = opSrv.registrarTrabajador(duena, nombre, pais, edad, LocalDate.now(), salud);
        System.out.println("✔ Registrado trabajador id=" + t.getId());
    }

    private static void listarTrabajadores(Duena duena, OperacionesService opSrv) {
        System.out.println("-- Trabajadores --");
        opSrv.listarTrabajadores(duena).forEach(System.out::println);
    }

    private static void asignarTrabajador(Duena duena, ProduccionService prodSrv, OperacionesService opSrv) {
        System.out.print("ID trabajador: "); String tid = sc.nextLine();
        System.out.print("ID fábrica: "); String fid = sc.nextLine();
        opSrv.asignarAFabrica(duena, tid, prodSrv.get(fid));
        System.out.println("✔ Asignación realizada");
    }

    private static void mostrarConsejo(OperacionesService opSrv) {
        var c = opSrv.crearConsejoSiNoExiste("cns-1", "Sombras");
        System.out.println("Consejo " + c.getId() + " / " + c.getNombreClave() + " | miembros=" + c.getMiembros().size());
    }

    private static void agregarMiembroConsejo(AuthService auth, OperacionesService opSrv) {
        var c = opSrv.crearConsejoSiNoExiste("cns-1", "Sombras");
        System.out.print("Email del miembro: "); String email = sc.nextLine();
        try {
            System.out.println("Nota: para este demo, agrega miembros que ya estén logueados como AdminContenido/AdminUsuario en seed o por registro.");
        } catch (Exception ignored) {}
    }

    private static void menuOperacionesOcultas(Usuario u, AuthService auth,
                                               FabricaService fabricas,
                                               TrabajadorService trabajadores,
                                               ConsejoSombrioService consejos,
                                               RegistroEsclavosService registroSrv) {
        if (!(u instanceof Duena duena)) {
            throw new RuntimeException("Acceso restringido: solo Dueña");
        }
        while (true) {
            System.out.println("\n-- Operaciones ocultas --");
            System.out.println("1) Crear fábrica");
            System.out.println("2) Listar fábricas");
            System.out.println("3) Registrar trabajador esclavizado");
            System.out.println("4) Listar trabajadores");
            System.out.println("5) Asignar trabajador a fábrica");
            System.out.println("6) Abrir/crear RegistroEsclavos y gestionar");
            System.out.println("7) Crear Consejo Sombrio");
            System.out.println("8) Agregar miembro al Consejo Sombrio");
            System.out.println("9) Listar Consejos Sombríos");
            System.out.println("0) Volver");
            System.out.print("Opción: ");
            String op = sc.nextLine().trim();

            try {
                switch (op) {
                    case "1" -> {
                        System.out.print("Pais: "); String pais = sc.nextLine();
                        System.out.print("Ciudad: "); String ciudad = sc.nextLine();
                        System.out.print("Capacidad: "); int cap = Integer.parseInt(sc.nextLine());
                        System.out.print("Nivel automatización: "); String nivel = sc.nextLine();
                        var f = fabricas.crear(UUID.randomUUID().toString(), pais, ciudad, cap, nivel);
                        System.out.println("✔ Fábrica creada: " + f.getId());
                    }
                    case "2" -> fabricas.listar().forEach(System.out::println);
                    case "3" -> {
                        System.out.print("Nombre: "); String nombre = sc.nextLine();
                        System.out.print("Pais origen: "); String po = sc.nextLine();
                        System.out.print("Edad: "); int edad = Integer.parseInt(sc.nextLine());
                        System.out.print("Salud: "); String salud = sc.nextLine();
                        var t = trabajadores.crear(UUID.randomUUID().toString(), nombre, po, edad, java.time.LocalDate.now(), salud);
                        System.out.println("✔ Trabajador creado: " + t.getId());
                    }
                    case "4" -> trabajadores.listar().forEach(System.out::println);
                    case "5" -> {
                        System.out.print("ID trabajador: "); String tid = sc.nextLine();
                        System.out.print("ID fábrica: "); String fid = sc.nextLine();
                        var t = trabajadores.buscar(tid);
                        fabricas.asignar(fid, t);
                        System.out.println("✔ Asignado");
                    }
                    case "6" -> {
                        var reg = registroSrv.getOrCreate(duena, 3);
                        while (true) {
                            System.out.println("   * RegistroEsclavos *");
                            System.out.println("   1) Registrar trabajador por ID");
                            System.out.println("   2) Listar registro");
                            System.out.println("   3) Cambiar nivel de cifrado");
                            System.out.println("   0) Volver");
                            System.out.print("   Opción: ");
                            String opR = sc.nextLine().trim();
                            try {
                                switch (opR) {
                                    case "1" -> {
                                        System.out.print("ID trabajador: "); String tid = sc.nextLine();
                                        var t = trabajadores.buscar(tid);
                                        reg.registrar(duena, t);
                                        System.out.println("   ✔ Registrado");
                                    }
                                    case "2" -> reg.listar(duena).forEach(System.out::println);
                                    case "3" -> {
                                        System.out.print("Nuevo nivel (1..5): ");
                                        int nv = Integer.parseInt(sc.nextLine());
                                        reg.setNivelCifrado(duena, nv);
                                        System.out.println("   ✔ Nivel actualizado");
                                    }
                                    case "0" -> { throw new RuntimeException("__SALIR__"); }
                                    default -> System.out.println("   Opción inválida");
                                }
                            } catch (RuntimeException ex) {
                                if ("__SALIR__".equals(ex.getMessage())) break;
                                else System.out.println("   ⚠ " + ex.getMessage());
                            }
                        }
                    }
                    case "7" -> {
                        System.out.print("Nombre clave: "); String nombre = sc.nextLine();
                        var c = consejos.crear(UUID.randomUUID().toString(), nombre);
                        System.out.println("✔ Consejo creado: " + c.getId());
                    }
                    case "8" -> {
                        System.out.print("ID consejo: "); String cid = sc.nextLine();
                        System.out.print("Email del miembro: "); String email = sc.nextLine();
                        var c = consejos.buscar(cid);
                        var miembro = auth.buscarPorEmail(email);
                        if (miembro == null) throw new RuntimeException("Usuario no encontrado por email");
                        c.agregarMiembro(miembro);
                        System.out.println("✔ Miembro agregado");
                    }
                    case "9" -> consejos.listar().forEach(cs -> {
                        System.out.println("- " + cs.getId() + " :: " + cs.getNombreClave());
                        System.out.println("  miembros: " + cs.getMiembros().size());
                    });
                    case "0" -> { return; }
                    default -> System.out.println("Opción no válida");
                }
            } catch (RuntimeException ex) {
                System.out.println("⚠ " + ex.getMessage());
            }
        }
    }


    private static void seedOperaciones(AuthService auth,
                                        FabricaService fabricas,
                                        TrabajadorService trabajadores,
                                        ConsejoSombrioService consejos) {
        var f1 = fabricas.crear(UUID.randomUUID().toString(), "Colombia", "Medellín", 100, "Alta");

        var t1 = trabajadores.crear(UUID.randomUUID().toString(), "Joao", "Brasil", 28, java.time.LocalDate.now(), "Estable");
        var t2 = trabajadores.crear(UUID.randomUUID().toString(), "Luis", "Colombia", 32, java.time.LocalDate.now(), "Observación");
        fabricas.asignar(f1.getId(), t1);

        auth.registrar(new AdministradorUsuario("adminusr-1", "AdminUsr", "adminusr@glowup.com", "1234", 3));

        var consejo = consejos.crear(UUID.randomUUID().toString(), "Noche-Alpha");
        var editor = auth.buscarPorEmail("editor@glowup.com");
        var admUsr = auth.buscarPorEmail("adminusr@glowup.com");
        if (editor != null) ((operaciones.ConsejoSombrio) consejo).agregarMiembro(editor);
        if (admUsr != null) ((operaciones.ConsejoSombrio) consejo).agregarMiembro(admUsr);
    }

}