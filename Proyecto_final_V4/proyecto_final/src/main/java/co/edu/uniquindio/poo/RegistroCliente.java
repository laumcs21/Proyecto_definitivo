package co.edu.uniquindio.poo;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroCliente {
    private static final String ARCHIVO_USUARIOS = "usuarios.ser";
    private static final String CORREO_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private Map<String, Cliente> clientesRegistrados;
    private EnviarCorreo enviarCorreo;

    public RegistroCliente(EnviarCorreo correoSender) {
        this.enviarCorreo = correoSender;
        clientesRegistrados = cargarClientesRegistrados();
    }

    private Map<String, Cliente> cargarClientesRegistrados() {
        Map<String, Cliente> clientesRegistrados = new HashMap<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO_USUARIOS))) {
            clientesRegistrados = (Map<String, Cliente>) in.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("El archivo de usuarios registrados no existe.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar clientes registrados: " + e.getMessage());
        }
        return clientesRegistrados;
    }

    public boolean registrarUsuario(String nombre, String cedula, String telefono, String usuario, String contraseña) {
        if (!clientesRegistrados.containsKey(usuario)) {
            Cliente cliente = new Cliente(nombre, cedula, telefono, usuario, contraseña);
            String codigoVerificacion = enviarCorreo.enviarCorreo(usuario);
            System.out.println("Código de verificación enviado: " + codigoVerificacion); // Depuración
            cliente.setCodigoVerificacion(codigoVerificacion); // Guarda el código en el cliente
            cliente.setVerificado(false); // Marca el usuario como no verificado
            clientesRegistrados.put(usuario, cliente);
            guardarClientesRegistrados();

            // Enviar correo con código de cupón
            enviarCorreo.enviarCorreoConCupon(usuario);

            return true;
        }
        return false;
    }

    private void guardarClientesRegistrados() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            out.writeObject(clientesRegistrados);
        } catch (IOException e) {
            System.err.println("Error al guardar clientes registrados: " + e.getMessage());
        }
    }

    public boolean validarCorreoElectronico(String correo) {
        Pattern pattern = Pattern.compile(CORREO_REGEX);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }

    public boolean haIniciadoSesionAntes(String usuario) {
        Cliente cliente = clientesRegistrados.get(usuario);
        boolean resultado = cliente != null && cliente.isVerificado();
        System.out.println("haIniciadoSesionAntes para " + usuario + ": " + resultado); // Depuración
        return resultado;
    }

    public boolean verificarCodigoVerificacion(String usuario, String codigoVerificacion) {
        Cliente cliente = clientesRegistrados.get(usuario);
        if (cliente != null) {
            String codigoAlmacenado = cliente.getCodigoVerificacion();
            System.out.println("Código almacenado: " + codigoAlmacenado); // Depuración
            System.out.println("Código ingresado: " + codigoVerificacion); // Depuración
            if (codigoAlmacenado != null && codigoAlmacenado.equals(codigoVerificacion)) {
                cliente.setVerificado(true); // Marca al usuario como verificado
                guardarClientesRegistrados();
                return true; // Código de verificación válido
            }
        }
        return false; // Código de verificación inválido
    }
}
