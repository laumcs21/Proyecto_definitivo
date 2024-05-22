package co.edu.uniquindio.poo;
/**
 * Clase para probar el funcionamiento del Proyecto
 * @author Área de programación UQ
 * @since 2023-08
 * 
 * Licencia GNU/GPL V3.0 (https://raw.githubusercontent.com/grid-uq/poo/main/LICENSE) 
 */

 import static org.junit.jupiter.api.Assertions.*;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 
 import java.io.*;
 import java.util.HashMap;
 import java.util.Map;
 
 public class LoginClienteTest {
     private LoginCliente loginCliente;
 
     @BeforeEach
     public void setUp() {
         loginCliente = new LoginCliente();
         // Crear un archivo temporal de usuarios para pruebas
         Map<String, Cliente> clientesRegistrados = new HashMap<>();
         Cliente cliente = new Cliente("Juan", "12345678", "555-5555", "juan", "password");
         clientesRegistrados.put(cliente.getUsuario(), cliente);
 
         try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(LoginCliente.getArchivoUsuarios()))) {
             out.writeObject(clientesRegistrados);
         } catch (IOException e) {
             e.printStackTrace();
         }
         loginCliente = new LoginCliente(); // Recargar los datos desde el archivo temporal
     }
 
     @Test
     public void testLoginExitoso() {
         assertTrue(loginCliente.login("juan", "password"));
         assertNotNull(loginCliente.getUsuarioActual());
         assertEquals("juan", loginCliente.getUsuarioActual().getUsuario());
     }
 
     @Test
     public void testLoginFallido() {
         assertFalse(loginCliente.login("juan", "wrongpassword"));
         assertNull(loginCliente.getUsuarioActual());
     }
 }