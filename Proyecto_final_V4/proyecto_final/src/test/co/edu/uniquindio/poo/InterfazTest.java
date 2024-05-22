
/**
 * Clase para probar el funcionamiento del Proyecto
 * @author Área de programación UQ
 * @since 2023-08
 * 
 * Licencia GNU/GPL V3.0 (https://raw.githubusercontent.com/grid-uq/poo/main/LICENSE) 
 */
package co.edu.uniquindio.poo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InterfazTest {

    private Interfaz interfaz;

    @BeforeEach
    public void setUp() {
        interfaz = new Interfaz();
    }

    @Test
    public void testCargarDatos() {
        // Preparar datos de prueba
        List<Cupon> cuponesPrueba = new ArrayList<>();
        cuponesPrueba.add(new Cupon("Test Cupon", "TEST123", LocalDate.now().plusDays(1), 10.0));
        interfaz.guardarObjeto("cupones_test.dat", cuponesPrueba);

        // Cargar los datos
        List<Cupon> cuponesCargados = interfaz.cargarObjeto("cupones_test.dat");

        // Verificar que los datos se cargaron correctamente
        assertEquals(cuponesPrueba.size(), cuponesCargados.size());
        assertEquals(cuponesPrueba.get(0).getNombre(), cuponesCargados.get(0).getNombre());

        // Limpiar
        new File("cupones_test.dat").delete();
    }

    @Test
    public void testGuardarDatos() {
        // Preparar datos de prueba
        List<Evento> eventosPrueba = new ArrayList<>();
        eventosPrueba.add(new Evento("Test Evento", "Test Ciudad", "Test Descripción", TipoEvento.CONCIERTO, "Test Imagen", LocalDate.now().plusDays(10), "Test Dirección", new ArrayList<>()));
        interfaz.guardarObjeto("eventos_test.dat", eventosPrueba);

        // Cargar los datos guardados
        List<Evento> eventosCargados = interfaz.cargarObjeto("eventos_test.dat");

        // Verificar que los datos se guardaron y cargaron correctamente
        assertEquals(eventosPrueba.size(), eventosCargados.size());
        assertEquals(eventosPrueba.get(0).getNombre(), eventosCargados.get(0).getNombre());

        // Limpiar
        new File("eventos_test.dat").delete();
    }

    @Test
    public void testEliminarCupon() {
        // Preparar los datos de entrada
        Cupon cupon = new Cupon("Cupón Test", "CODIGO_TEST", LocalDate.now().plusDays(10), 15.0);
        interfaz.getCupones().add(cupon);
    
        // Verificar que el cupón se ha añadido
        assertFalse(interfaz.getCupones().isEmpty());
    
        // Obtener los componentes de la UI
        ComboBox<Cupon> cuponComboBox = (ComboBox<Cupon>) lookup("#cuponComboBox").query();
        Button eliminarButton = (Button) lookup("#eliminarCuponButton").query();
    
        // Simular la selección del cupón y la acción de eliminar
        Platform.runLater(() -> {
            cuponComboBox.getSelectionModel().select(cupon);
            eliminarButton.fire();
        });
    
        // Esperar a que se complete la operación en el hilo de JavaFX
        WaitForAsyncUtils.waitForFxEvents();
    
        // Verificar que el cupón se haya eliminado de la lista
        assertTrue(interfaz.getCupones().isEmpty());
    }
}
