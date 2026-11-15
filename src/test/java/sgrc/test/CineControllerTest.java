package sgrc.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sgrc.controlador.CineController;
import sgrc.modelo.Pelicula;
import sgrc.modelo.Sala;
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

    // ---------- Salas ----------

    @Test
    public void testOpcionVerSalasDisponibles() {
        List<Sala> salas = List.of(new Sala(1, 5, 5));
        when(reservaService.obtenerSalasDisponibles()).thenReturn(salas);

        assertTrue(cineController.procesarOpcion(CineController.OPCION_VER_SALAS));
        verify(vista).mostrarSalas(salas);
    }

    @Test
    public void testOpcionVerSalaMuestraElMapa() {
        Sala sala = new Sala(2, 5, 5);
        when(vista.leerEntero(anyString())).thenReturn(2);
        when(reservaService.obtenerSala(2)).thenReturn(sala);

        assertTrue(cineController.procesarOpcion(CineController.OPCION_VER_SALA));
        verify(vista).mostrarSala(sala);
    }

    @Test
    public void testOpcionVerSalaInexistenteMuestraError() {
        when(vista.leerEntero(anyString())).thenReturn(9);
        when(reservaService.obtenerSala(9)).thenThrow(new IllegalArgumentException("La sala no existe"));

        assertTrue(cineController.procesarOpcion(CineController.OPCION_VER_SALA));
        verify(vista).mostrarError("La sala no existe");
        verify(vista, never()).mostrarSala(any());
    }

    // ---------- Reservar y cancelar un asiento ----------
    // La Vista devuelve sala, fila y columna numeradas desde 1;
    // el controlador las convierte a índices desde 0 para el Model.

    @Test
    public void testOpcionReservarAsientoConvierteCoordenadas() {
        when(vista.leerEntero(anyString())).thenReturn(1, 3, 4); // sala 1, fila 3, columna 4
        when(reservaService.realizarReserva(1, 2, 3)).thenReturn(true);

        assertTrue(cineController.procesarOpcion(CineController.OPCION_RESERVAR));
        verify(reservaService).realizarReserva(1, 2, 3);
        verify(vista).mostrarMensaje(anyString());
    }

    @Test
    public void testOpcionReservarAsientoOcupadoMuestraError() {
        when(vista.leerEntero(anyString())).thenReturn(1, 1, 1);
        when(reservaService.realizarReserva(1, 0, 0)).thenReturn(false);

        assertTrue(cineController.procesarOpcion(CineController.OPCION_RESERVAR));
        verify(vista).mostrarError(anyString());
    }

    @Test
    public void testOpcionReservarAsientoFueraDeLimitesMuestraError() {
        // Fila 0 introducida por el usuario (valor límite inválido) -> índice -1 -> excepción del Model
        when(vista.leerEntero(anyString())).thenReturn(1, 0, 1);
        when(reservaService.realizarReserva(1, -1, 0)).thenThrow(new IllegalArgumentException("fuera"));

        assertTrue(cineController.procesarOpcion(CineController.OPCION_RESERVAR));
        verify(vista).mostrarError("fuera");
    }

    @Test
    public void testOpcionCancelarReserva() {
        when(vista.leerEntero(anyString())).thenReturn(2, 1, 1);
        when(reservaService.cancelarReserva(2, 0, 0)).thenReturn(true);

        assertTrue(cineController.procesarOpcion(CineController.OPCION_CANCELAR));
        verify(vista).mostrarMensaje(anyString());
    }

    @Test
    public void testOpcionCancelarReservaAsientoLibreMuestraError() {
        when(vista.leerEntero(anyString())).thenReturn(2, 1, 1);
        when(reservaService.cancelarReserva(2, 0, 0)).thenReturn(false);

        assertTrue(cineController.procesarOpcion(CineController.OPCION_CANCELAR));
        verify(vista).mostrarError(anyString());
    }

    // ---------- Reserva múltiple ----------

    @Test
    public void testOpcionReservaMultipleLeeTodosLosAsientos() {
        // sala 1, 2 asientos: (1,1) y (1,2) -> índices (0,0) y (0,1)
        when(vista.leerEntero(anyString())).thenReturn(1, 2, 1, 1, 1, 2);
        when(reservaService.realizarReservaMultiple(eq(1), anyList())).thenReturn(true);

        assertTrue(cineController.procesarOpcion(CineController.OPCION_RESERVAR_VARIOS));

        verify(reservaService).realizarReservaMultiple(eq(1), argThat(lista -> lista.size() == 2
                && lista.get(0)[0] == 0 && lista.get(0)[1] == 0
                && lista.get(1)[0] == 0 && lista.get(1)[1] == 1));
        verify(vista).mostrarMensaje(anyString());
    }

    @Test
    public void testOpcionReservaMultipleRechazadaMuestraError() {
        when(vista.leerEntero(anyString())).thenReturn(1, 1, 1, 1);
        when(reservaService.realizarReservaMultiple(eq(1), anyList())).thenReturn(false);

        assertTrue(cineController.procesarOpcion(CineController.OPCION_RESERVAR_VARIOS));
        verify(vista).mostrarError(anyString());
    }

    // ---------- Bucle principal ----------

    @Test
    public void testIniciarMuestraElMenuHastaQueElUsuarioSale() {
        // El usuario ve la cartelera y después sale
        when(vista.leerOpcion()).thenReturn(CineController.OPCION_VER_CARTELERA, CineController.OPCION_SALIR);
        when(carteleraService.obtenerCartelera()).thenReturn(List.of());

        cineController.iniciar();

        verify(vista, times(2)).mostrarMenu();
        verify(vista, times(2)).leerOpcion();
        verify(vista).mostrarCartelera(List.of());
    }

    // ---------- Precondiciones del constructor ----------

    @Test
    public void testConstructorConDependenciasNulas() {
        assertThrows(IllegalArgumentException.class, () -> new CineController(null, reservaService, vista));
        assertThrows(IllegalArgumentException.class, () -> new CineController(carteleraService, null, vista));
        assertThrows(IllegalArgumentException.class, () -> new CineController(carteleraService, reservaService, null));
    }
}
