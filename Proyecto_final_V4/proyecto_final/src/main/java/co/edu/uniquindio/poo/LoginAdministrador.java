package co.edu.uniquindio.poo;

// Implementa la funcionalidad de autenticación para un administrador en un sistema
public class LoginAdministrador {
    private Administrador administrador;

    public LoginAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

     // Método para realizar el login, verificando usuario y contraseña
    public boolean login(String usuario, String contraseña) {
        if (administrador != null) {
            return administrador.getUsuario().equals(usuario) && administrador.getContraseña().equals(contraseña);
        }
        return false;
    }

    // Método para establecer un nuevo administrador
    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public static void main(String[] args) {
        Administrador administrador = Administrador.obtenerInstancia();
        LoginAdministrador login1 = new LoginAdministrador(administrador);
        // login1.setAdministrador(administrador);
        System.out.println(login1.login(administrador.getUsuario(), administrador.getContraseña()));
    }
}