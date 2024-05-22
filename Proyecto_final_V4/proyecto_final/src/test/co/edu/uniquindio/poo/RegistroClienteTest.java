
/**
 * Clase para probar el funcionamiento del Proyecto
 * @author Área de programación UQ
 * @since 2023-08
 * 
 * Licencia GNU/GPL V3.0 (https://raw.githubusercontent.com/grid-uq/poo/main/LICENSE) 
 */
package co.edu.uniquindio.poo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistroClienteTest {
    
    @Test
    void testVerificarCodigoVerificacion() {
        EnviarCorreo enviarCorreo = new EnviarCorreo();
        RegistroCliente registroCliente = new RegistroCliente(enviarCorreo);
        
        // Registro de un nuevo usuario
        String usuario = "test@example.com";
        registroCliente.registrarUsuario("Nombre", "Cedula", "Telefono", usuario, "Contraseña");
        
        // Obtener el código de verificación enviado
        String codigoVerificacion = registroCliente.getClientesRegistrados().get(usuario).getCodigoVerificacion();
        
        // Verificar el código correcto
        assertTrue(registroCliente.verificarCodigoVerificacion(usuario, codigoVerificacion));
        
        // Verificar un código incorrecto
        assertFalse(registroCliente.verificarCodigoVerificacion(usuario, "codigoIncorrecto"));
    }
}
