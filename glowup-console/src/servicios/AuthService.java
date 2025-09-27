package servicios;

import dominio.*;
import java.util.*;

public class AuthService {
    private final Map<String, Usuario> usuariosPorEmail = new HashMap<>(); // email -> usuario

    public void registrar(Usuario u) {
        if (usuariosPorEmail.containsKey(u.getEmail()))
            throw new RuntimeException("Email ya registrado");
        usuariosPorEmail.put(u.getEmail(), u);
    }

    public Usuario login(String email, String password) {
        Usuario u = usuariosPorEmail.get(email);
        if (u == null || !u.getPasswordHash().equals(password))
            throw new RuntimeException("Credenciales inváli das");
        if (!u.isActivo())
            throw new RuntimeException("Usuario suspendido");
        return u;
    }

    public Usuario buscarPorEmail(String email) {
        return usuariosPorEmail.get(email);
    }
}