
/**
 * Clase para probar el funcionamiento del Proyecto
 * @author Área de programación UQ
 * @since 2023-08
 * 
 * Licencia GNU/GPL V3.0 (https://raw.githubusercontent.com/grid-uq/poo/main/LICENSE) 
 */
package co.edu.uniquindio.poo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

public class AdministradorTest {
    /**
     * Instancia para el manejo de logs
     */
    private static final Logger LOG = Logger.getLogger(AdministradorTest.class.getName());

    /**
     * Verificar que la clase Administrador tenga una unica instancia
     * 
     */
    @Test
    public void datosCompletos() {
        LOG.info("Inicio de prueba...");
        Torneo torneo = new Torneo();

        // Recuperación y verificación de datos
        assertEquals("admin@unieventos.com", Administrador.getUsuario());
        assertEquals("admin123", Administrador.getContraseña());

        LOG.info("Fin de prueba...");
    }
}