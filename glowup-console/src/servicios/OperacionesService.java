package servicios;

import dominio.Duena;
import dominio.Usuario;
import operaciones.ConsejoSombrio;
import operaciones.RegistroEsclavos;
import operaciones.TrabajadorEsclavizado;
import produccion.Fabrica;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OperacionesService {
    private final RegistroEsclavos registro;
    private ConsejoSombrio consejo;

    public OperacionesService(Duena duena) {
        this.registro = new RegistroEsclavos(duena, 3);
    }

    public RegistroEsclavos getRegistro() { return registro; }

    public TrabajadorEsclavizado registrarTrabajador(Duena duena, String nombre, String pais, int edad, LocalDate fecha, String salud) {
        TrabajadorEsclavizado t = new TrabajadorEsclavizado(UUID.randomUUID().toString(), nombre, pais, edad, fecha, salud);
        registro.registrar(duena, t);
        return t;
    }

    public List<TrabajadorEsclavizado> listarTrabajadores(Duena duena) {
        return registro.listar(duena);
    }

    public Optional<TrabajadorEsclavizado> buscarTrabajador(Duena duena, String id) {
        return registro.buscar(duena, id);
    }

    public void asignarAFabrica(Duena duena, String trabajadorId, Fabrica fabrica) {
        TrabajadorEsclavizado t = registro.buscar(duena, trabajadorId).orElseThrow(() -> new RuntimeException("Trabajador no existe"));
        fabrica.agregarTrabajador(t);
    }

    public ConsejoSombrio crearConsejoSiNoExiste(String id, String nombreClave) {
        if (consejo == null) consejo = new ConsejoSombrio(id, nombreClave);
        return consejo;
    }

    public ConsejoSombrio getConsejo() { return consejo; }

    public void agregarMiembroAlConsejo(Usuario u) {
        if (consejo == null) throw new RuntimeException("Consejo no existe");
        consejo.agregarMiembro(u);
    }
}
