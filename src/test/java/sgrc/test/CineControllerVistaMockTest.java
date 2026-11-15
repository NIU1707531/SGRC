package sgrc.test;

import sgrc.controlador.CineController;
import sgrc.modelo.Sala;
import sgrc.repositorio.EnMemoriaSalaRepository;
import sgrc.repositorio.PeliculaRepositoryImpl;
import sgrc.servicio.CarteleraService;
import sgrc.servicio.ReservaService;
import sgrc.test.mocks.VistaMock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests del Controlador con el MOCK MANUAL VistaMock (simula la interacción con la Vista).
 * El Model es real (repositorios en memoria), así que también son tests de integración
 * Controlador-Model sin necesidad de la consola.
 */
public class CineControllerVistaMockTest {

    private static final int SALIR = CineController.OPCION_SALIR;

    private VistaMock vista;
    private EnMemoriaSalaRepository salas;
    private CineController controlador;

    @BeforeEach
    public void setUp() {
        vista = new VistaMock();
        salas = new EnMemoriaSalaRepository();
        salas.guardar(new Sala(1, 3, 3));
        controlador = new CineController(new CarteleraService(new PeliculaRepositoryImpl()),
                new ReservaService(salas), vista);
    }

    // ========== LOOP TESTING del bucle while de iniciar() ==========
    // Mínimo: 1 iteración (el usuario sale nada más empezar); 2 iteraciones; n iteraciones.

    @Test
    public void testIniciarUnaIteracion() {
        vista.programarOpciones(SALIR);
        controlador.iniciar();
        assertEquals(1, vista.getVecesMenu());
        assertTrue(vista.entradasConsumidas());
    }

    @Test
    public void testIniciarDosIteraciones() {
        vista.programarOpciones(CineController.OPCION_VER_CARTELERA, SALIR);
        controlador.iniciar();
        assertEquals(2, vista.getVecesMenu());
        assertTrue(vista.getUltimaCartelera().isEmpty());
    }

    @Test
    public void testIniciarNIteraciones() {
        // 5 opciones (una inválida incluida) antes de salir -> 6 iteraciones
        vista.programarOpciones(1, 4, 99, 1, 4, SALIR);
        controlador.iniciar();
        assertEquals(6, vista.getVecesMenu());
        assertEquals(1, vista.getErrores().size());
    }

    // ========== LOOP TESTING del bucle for de la reserva múltiple ==========
    // 0 iteraciones (cantidad 0), 1 iteración y n iteraciones (sala completa: 9 asientos).

    @Test
    public void testReservaMultipleCeroAsientos() {
        // 0 iteraciones: lista vacía -> el Model rechaza la reserva y se muestra error
        vista.programarEnteros(1, 0);
        controlador.procesarOpcion(CineController.OPCION_RESERVAR_VARIOS);
        assertEquals(1, vista.getErrores().size());
        assertEquals(9, salas.buscarPorId(1).get().contarAsientosLibres());
    }

    @Test
    public void testReservaMultipleUnAsiento() {
        vista.programarEnteros(1, 1, 2, 2);
        controlador.procesarOpcion(CineController.OPCION_RESERVAR_VARIOS);
        assertFalse(salas.buscarPorId(1).get().estaAsientoDisponible(1, 1));
        assertEquals(1, vista.getMensajes().size());
    }

    @Test
    public void testReservaMultipleSalaCompleta() {
        vista.programarEnteros(1, 9, 1, 1, 1, 2, 1, 3, 2, 1, 2, 2, 2, 3, 3, 1, 3, 2, 3, 3);
        controlador.procesarOpcion(CineController.OPCION_RESERVAR_VARIOS);
        assertEquals(0, salas.buscarPorId(1).get().contarAsientosLibres());
        assertTrue(vista.entradasConsumidas());
    }

    // ========== Flujo completo de usuario ==========

    @Test
    public void testFlujoAnadirPeliculaYVerCartelera() {
        vista.programarOpciones(CineController.OPCION_ANADIR_PELICULA, CineController.OPCION_VER_CARTELERA, SALIR);
        vista.programarTextos("Dune");
        vista.programarEnteros(155);
        controlador.iniciar();
        assertEquals("Dune", vista.getUltimaCartelera().get(0).getTitulo());
    }

    @Test
    public void testFlujoReservarYCancelar() {
        vista.programarOpciones(CineController.OPCION_RESERVAR, CineController.OPCION_RESERVAR,
                CineController.OPCION_CANCELAR, SALIR);
        vista.programarEnteros(1, 1, 1, 1, 1, 1, 1, 1, 1); // misma butaca 3 veces
        controlador.iniciar();
        // 1a reserva OK, 2a ocupado (error), cancelación OK
        assertEquals(2, vista.getMensajes().size());
        assertEquals(1, vista.getErrores().size());
        assertTrue(salas.buscarPorId(1).get().estaAsientoDisponible(0, 0));
    }

    @Test
    public void testFlujoVerSalaYSalasDisponibles() {
        vista.programarOpciones(CineController.OPCION_VER_SALA, CineController.OPCION_VER_SALAS, SALIR);
        vista.programarEnteros(1);
        controlador.iniciar();
        assertEquals(1, vista.getUltimaSala().getId());
        assertEquals(1, vista.getUltimasSalas().size());
    }

    // ========== registrarPelicula (método público del controlador) ==========

    @Test
    public void testRegistrarPeliculaValida() {
        controlador.registrarPelicula("Matrix", 136);
        assertEquals(1, controlador.mostrarCartelera().size());
    }
}
