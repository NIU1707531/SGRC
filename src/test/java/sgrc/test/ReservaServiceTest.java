package sgrc.test;

import sgrc.modelo.Sala;
import sgrc.repositorio.SalaRepository;
import sgrc.servicio.ReservaService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private SalaRepository salaRepositoryMock;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    public void testRealizarReservaExitosamente() {
        Sala sala = new Sala(1, 5, 5);
        when(salaRepositoryMock.buscarPorId(1)).thenReturn(Optional.of(sala));

        boolean resultado = reservaService.realizarReserva(1, 2, 2);

        assertTrue(resultado);
        verify(salaRepositoryMock, times(1)).guardar(sala);
    }

    @Test
    public void testRealizarReservaSalaNoEncontrada() {
        when(salaRepositoryMock.buscarPorId(99)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            reservaService.realizarReserva(99, 1, 1);
        });
        verify(salaRepositoryMock, never()).guardar(any());
    }
}