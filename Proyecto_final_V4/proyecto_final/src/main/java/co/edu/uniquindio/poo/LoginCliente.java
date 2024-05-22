package co.edu.uniquindio.poo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

//Gestiona la autenticación de clientes en una aplicación. Utiliza un archivo serializado para cargar y almacenar los datos de los usuarios registrados. 
public class LoginCliente {
    private static final String ARCHIVO_USUARIOS = "usuarios.ser";
    private Map<String, Cliente> clientesRegistrados;
    private Cliente usuarioActual; // Variable para almacenar el usuario actual

    public LoginCliente() {
        cargarClientesRegistrados();
    }

    //ntenta cargar un mapa de clientes registrados desde un archivo en el disco. Si tiene éxito, asigna el mapa de clientes registrados cargado al campo clientesRegistrados. Si ocurre una excepción durante el proceso de carga, inicializa clientesRegistrados con un nuevo HashMap<>();. Esto asegura que siempre haya un mapa de clientes registrados disponible, ya sea cargado desde el archivo o un mapa vacío si el archivo no existe o no se puede leer correctamente.
    private void cargarClientesRegistrados() {
       
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO_USUARIOS))) {
            clientesRegistrados = (Map<String, Cliente>) in.readObject(); //ntenta leer un objeto del archivo y asignarlo a clientesRegistrados
        } 
        //Si ocurre una excepción (como que el archivo no exista o el objeto no sea de la clase esperada), se inicializa clientesRegistrados con un nuevo HashMap vacío.
        catch (IOException | ClassNotFoundException e) {
            clientesRegistrados = new HashMap<>();
        }
    }

    //Método público que verifica las credenciales del usuario.
    public boolean login(String usuario, String contraseña) {
        Cliente cliente = clientesRegistrados.get(usuario);
        if (cliente != null && cliente.getContraseña().equals(contraseña)) {
            usuarioActual = cliente; // Actualiza el usuario actual
            return true;//Para login exitoso
        }
        return false;
    }

    // Método público que devuelve el cliente actualmente autenticado
    public Cliente getUsuarioActual() {
        return usuarioActual;
    }

    // Método estático que devuelve el nombre del archivo donde se almacenan los datos de los usuarios
    public static String getArchivoUsuarios() {
        return ARCHIVO_USUARIOS;
    }

    //Método público que devuelve el mapa de clientes registrados 
    public Map<String, Cliente> getClientesRegistrados() {
        return clientesRegistrados;
    }
}
