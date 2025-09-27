package dominio;

public class AdministradorUsuario extends Usuario {
    private int nivelAcceso; // 1..5

    public AdministradorUsuario(String id, String nombre, String email, String password, int nivelAcceso) {
        super(id, nombre, email, password, Rol.ADMIN_USUARIO);
        this.nivelAcceso = nivelAcceso;
    }

    public int getNivelAcceso() { return nivelAcceso; }
    public void setNivelAcceso(int nivelAcceso) { this.nivelAcceso = nivelAcceso; }
}
