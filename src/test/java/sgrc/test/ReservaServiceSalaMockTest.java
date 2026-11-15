package sgrc.test;

import sgrc.modelo.Sala;
import sgrc.repositorio.SalaRepository;
import sgrc.servicio.ReservaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * MOCK OBJECT (Mockito) de la clase del Model Sala.
 * Se aísla ReservaService de la lógica real de Sala: el test solo comprueba
 * que el servicio delega correctamente en la sala y que guarda el resultado.
 */
@ExtendWith(MockitoExtension.class)
public class ReservaServiceSalaMockTest {

    @Mock
    private Sala salaMock;

    @Mock
    private SalaRepository salaRepositoryMock;

    private ReservaService reservaService;

    @BeforeEach
    public void setUp() {
        reservaService = new ReservaService(salaRepositoryMock);
        when(salaRepositoryMock.buscarPorId(1)).thenReturn(Optional.of(salaMock));
    }

    @Test
    public void testRealizarReservaDelegaEnSala() {
        // La sala simulada acepta la reserva
        when(salaMock.reservarAsiento(3, 4)).thenReturn(true);

        assertTrue(reservaService.realizarReserva(1, 3, 4));
        verify(salaMock).reservarAsiento(3, 4);
        verify(salaRepositoryMock).guardar(salaMock);
    }

    @Test
    public void testRealizarReservaRechazadaPorSala() {
        // La sala simulada rechaza la reserva (asiento ocupado): no se guarda
        when(salaMock.reservarAsiento(3, 4)).thenReturn(false);

        assertFalse(reservaService.realizarReserva(1, 3, 4));
        verify(salaRepositoryMock, never()).guardar(salaMock);
    }

    @Test
    public void testCancelarReservaDelegaEnSala() {
        // La sala simulada libera el asiento y, después, lo indica como disponible
        // (necesario para cumplir la postcondición de cancelarReserva)
        when(salaMock.liberarAsiento(0, 0)).thenReturn(true);
        when(salaMock.estaAsientoDisponible(0, 0)).thenReturn(true);

        assertTrue(reservaService.cancelarReserva(1, 0, 0));
        verify(salaMock).liberarAsiento(0, 0);
        verify(salaRepositoryMock).guardar(salaMock);
    }
}
