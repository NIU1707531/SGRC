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

    // ---------- Añadir y eliminar película ----------

    @Test
    public void testOpcionAnadirPeliculaValida() {
        when(vista.leerTexto(anyString())).thenReturn("Matrix");
        when(vista.leerEntero(anyString())).thenReturn(136);

        assertTrue(cineController.procesarOpcion(CineController.OPCION_ANADIR_PELICULA));

        verify(carteleraService).agregarPelicula(argThat(p -> p.getTitulo().equals("Matrix")
                && p.getDuracionMinutos() == 136));
        verify(vista).mostrarMensaje(anyString());
    }

    @Test
    public void testOpcionAnadirPeliculaDatosInvalidosMuestraError() {
        // Duración 0 (valor límite inválido): el Model lanza excepción y el controlador la muestra
        when(vista.leerTexto(anyString())).thenReturn("Matrix");
        when(vista.leerEntero(anyString())).thenReturn(0);

        assertTrue(cineController.procesarOpcion(CineController.OPCION_ANADIR_PELICULA));

        verify(carteleraService, never()).agregarPelicula(any());
        verify(vista).mostrarError(anyString());
    }

    @Test
    public void testOpcionAnadirPeliculaDuplicadaMuestraError() {
        when(vista.leerTexto(anyString())).thenReturn("Matrix");
        when(vista.leerEntero(anyString())).thenReturn(136);
        doThrow(new IllegalStateException("duplicada")).when(carteleraService).agregarPelicula(any());

        assertTrue(cineController.procesarOpcion(CineController.OPCION_ANADIR_PELICULA));
        verify(vista).mostrarError("duplicada");
    }

    @Test
    public void testOpcionEliminarPelicula() {
        when(vista.leerTexto(anyString())).thenReturn("Matrix");
        when(carteleraService.eliminarPelicula("Matrix")).thenReturn(true);

        assertTrue(cineController.procesarOpcion(CineController.OPCION_ELIMINAR_PELICULA));
        verify(vista).mostrarMensaje(anyString());
    }

    @Test
    public void testOpcionEliminarPeliculaInexistente() {
        when(vista.leerTexto(anyString())).thenReturn("Matrix");
        when(carteleraService.eliminarPelicula("Matrix")).thenReturn(false);

        assertTrue(cineController.procesarOpcion(CineController.OPCION_ELIMINAR_PELICULA));
        verify(vista).mostrarError(anyString());
    }
}
