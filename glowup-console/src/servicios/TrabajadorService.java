package servicios;

import operaciones.TrabajadorEsclavizado;

import java.time.LocalDate;
import java.util.*;

public class TrabajadorService {
    private final Map<String, TrabajadorEsclavizado> personas = new HashMap<>(); // id -> trabajador

    public TrabajadorEsclavizado crear(String id, String nombre, String paisOrigen, int edad,
                                       LocalDate fechaCaptura, String salud) {
        TrabajadorEsclavizado t = new TrabajadorEsclavizado(id, nombre, paisOrigen, edad, fechaCaptura, salud);
        personas.put(t.getId(), t);
        return t;
    }

    public TrabajadorEsclavizado buscar(String id) {
        TrabajadorEsclavizado t = personas.get(id);
        if (t == null) throw new RuntimeException("Trabajador no encontrado");
        return t;
    }

    public List<TrabajadorEsclavizado> listar() {
        return new ArrayList<>(personas.values());
    }
}
