package dominio;

import java.util.HashSet;
import java.util.Set;

public class AdministradorContenido extends Usuario {
    private final Set<String> permisosEdicion = new HashSet<>();

    public AdministradorContenido(String id, String nombre, String email, String password, Set<String> permisos) {
        super(id, nombre, email, password, Rol.ADMIN_CONTENIDO);
        if (permisos != null) permisosEdicion.addAll(permisos);
    }

    public Set<String> getPermisosEdicion() { return permisosEdicion; }
}
