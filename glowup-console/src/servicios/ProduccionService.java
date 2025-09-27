package servicios;

import produccion.Fabrica;

import java.util.*;

public class ProduccionService {
    private final Map<String, Fabrica> fabricas = new HashMap<>();

    public Fabrica crearFabrica(String id, String pais, String ciudad, int capacidad, String nivelAutomatizacion) {
        if (fabricas.containsKey(id)) throw new RuntimeException("ID de fábrica en uso");
        Fabrica f = new Fabrica(id, pais, ciudad, capacidad, nivelAutomatizacion);
        fabricas.put(id, f);
        return f;
    }

    public Fabrica get(String id) {
        Fabrica f = fabricas.get(id);
        if (f == null) throw new RuntimeException("Fábrica no encontrada");
        return f;
    }

    public List<Fabrica> listar() {
        return fabricas.values().stream()
                .sorted(Comparator.comparing(Fabrica::getId))
                .toList();
    }
}
