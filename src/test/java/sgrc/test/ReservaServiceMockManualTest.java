package sgrc.test;

import sgrc.modelo.Sala;
import sgrc.servicio.ReservaService;
import sgrc.test.mocks.SalaRepositoryMock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de ReservaService usando el MOCK MANUAL SalaRepositoryMock
 * (test del Model sin Mockito).
 */
public class ReservaServiceMockManualTest {

    private SalaRepositoryMock repositorioMock;
    private ReservaService reservaService;

    @BeforeEach
    public void setUp() {
        repositorioMock = new SalaRepositoryMock();
        reservaService = new ReservaService(repositorioMock);
    }

    @Test
    public void testReservaExitosaGuardaLaSala() {
        Sala sala = new Sala(1, 3, 3);
        repositorioMock.setSalaADevolver(sala);

        assertTrue(reservaService.realizarReserva(1, 0, 0));

        // Verificación manual de las interacciones
        assertEquals(List.of(1), repositorioMock.getIdsBuscados());
        assertEquals(1, repositorioMock.getVecesGuardar());
        assertSame(sala, repositorioMock.getSalasGuardadas().get(0));
    }

    @Test
    public void testReservaAsientoOcupadoNoGuarda() {
        Sala sala = new Sala(1, 3, 3);
        sala.reservarAsiento(0, 0);
        repositorioMock.setSalaADevolver(sala);

        assertFalse(reservaService.realizarReserva(1, 0, 0));
        assertEquals(0, repositorioMock.getVecesGuardar());
    }

    @Test
    public void testReservaSalaInexistente() {
        // El mock no tiene sala configurada: buscarPorId devuelve vacío
        assertThrows(IllegalArgumentException.class, () -> reservaService.realizarReserva(5, 0, 0));
        assertEquals(List.of(5), repositorioMock.getIdsBuscados());
        assertEquals(0, repositorioMock.getVecesGuardar());
    }

    @Test
    public void testReservaMultipleGuardaUnaSolaVez() {
        Sala sala = new Sala(1, 2, 3);
        repositorioMock.setSalaADevolver(sala);

        assertTrue(reservaService.realizarReservaMultiple(1, List.of(new int[]{0, 0}, new int[]{1, 2})));
        assertEquals(1, repositorioMock.getVecesGuardar());
        assertEquals(4, sala.contarAsientosLibres());
    }

    @Test
    public void testCancelarReservaGuardaLaSala() {
        Sala sala = new Sala(1, 2, 2);
        sala.reservarAsiento(1, 0);
        repositorioMock.setSalaADevolver(sala);

        assertTrue(reservaService.cancelarReserva(1, 1, 0));
        assertEquals(1, repositorioMock.getVecesGuardar());
    }

    @Test
    public void testObtenerSalasDisponiblesDevuelveLoDelRepositorio() {
        List<Sala> salas = List.of(new Sala(1, 1, 1), new Sala(2, 1, 1));
        repositorioMock.setSalasADevolver(salas);

        assertEquals(salas, reservaService.obtenerSalasDisponibles());
        assertEquals(salas, reservaService.obtenerSalas());
    }
}
