package sgrc.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sgrc.controlador.CineController;
import sgrc.modelo.Pelicula;
import sgrc.servicio.CarteleraService;
import sgrc.servicio.ReservaService;
import sgrc.vista.Vista;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests del Controlador con MOCK OBJECTS de Mockito:
 *  - CarteleraService y ReservaService (Model) -> el controlador se prueba sin el Model real.
 *  - Vista -> se simula la interacción con el usuario (entradas y salidas).
 */
@ExtendWith(MockitoExtension.class)
public class CineControllerTest {

    @Mock
    private CarteleraService carteleraService; // Mockeamos el servicio de cartelera

    @Mock
    private ReservaService reservaService; // Mockeamos el servicio de reservas

    @Mock
    private Vista vista; // Mockeamos la vista

    private CineController cineController;

    @BeforeEach
    public void setUp() {
        cineController = new CineController(carteleraService, reservaService, vista);
    }

    @Test
    public void testListarCarteleraControlador() {
        // Arrange
        Pelicula p1 = new Pelicula("Inception", 148);
        when(carteleraService.obtenerCartelera()).thenReturn(Arrays.asList(p1));

        // Act
        List<Pelicula> resultado = cineController.mostrarCartelera();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Inception", resultado.get(0).getTitulo());
        verify(carteleraService, times(1)).obtenerCartelera();
    }

    // ===================== procesarOpcion =====================

    @Test
    public void testOpcionVerCarteleraMuestraLasPeliculasEnLaVista() {
        List<Pelicula> peliculas = List.of(new Pelicula("Inception", 148));
        when(carteleraService.obtenerCartelera()).thenReturn(peliculas);

        assertTrue(cineController.procesarOpcion(CineController.OPCION_VER_CARTELERA));
        verify(vista).mostrarCartelera(peliculas);
    }

    @Test
    public void testOpcionSalirDevuelveFalse() {
        assertFalse(cineController.procesarOpcion(CineController.OPCION_SALIR));
    }

    @Test
    public void testOpcionInvalidaMuestraError() {
        // Valor fuera del rango de opciones (partición inválida)
        assertTrue(cineController.procesarOpcion(99));
        verify(vista).mostrarError(anyString());
    }
}
