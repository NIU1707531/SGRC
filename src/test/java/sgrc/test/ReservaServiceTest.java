package sgrc.test;

import sgrc.modelo.Sala;
import java.util.List;
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
	
	@Test
	public void testRealizarReservaAsientoOcupado() {

	    // ARRANGE: crear una sala y ocupar el asiento (0,0)
	    Sala sala = new Sala(1, 2, 2);
	    sala.reservarAsiento(0, 0);

	    // Configurar el mock para devolver esta sala
	    when(salaRepositoryMock.buscarPorId(1))
	        .thenReturn(Optional.of(sala));

	    // ACT: intentar reservar de nuevo el asiento (0,0)
	    boolean resultado = reservaService.realizarReserva(1, 0, 0);
	   
	

	    // ASSERT: comprobar que la reserva devuelve false
	    assertFalse(resultado);

	    // ASSERT: comprobar con verify(..., never())
	    verify(salaRepositoryMock, never()).guardar(any());
	 
	    
	}
	
	@Test
	public void testRealizarReservaCoordenadasInvalidas() {

	    // ARRANGE: crear una sala de 2x2
	    Sala sala = new Sala(1, 2, 2);

	    // Configurar el mock para devolver esa sala
	    when(salaRepositoryMock.buscarPorId(1))
	        .thenReturn(Optional.of(sala));

	
	
	   
	    assertThrows(IllegalArgumentException.class, () -> {
	        reservaService.realizarReserva(1, 2, 0);
	    });

	    // ASSERT: comprobar que no se guarda la reserva
	    verify(salaRepositoryMock, never()).guardar(any());
	}
	
	@Test
	public void testReservaMultipleConAsientoOcupado() {

	    // ARRANGE
	    Sala sala = new Sala(1, 1, 3);
	    sala.reservarAsiento(0, 2);

	    when(salaRepositoryMock.buscarPorId(1))
	        .thenReturn(Optional.of(sala));

	    List<int[]> asientos = List.of(
	        new int[]{0, 0},
	        new int[]{0, 1},
	        new int[]{0, 2}
	    );


	    boolean resultado = reservaService.realizarReservaMultiple(1, asientos);

	    // ASSERT
	    // Comprobar que el resultado es false.
	    assertFalse(resultado);
	    assertTrue(sala.estaAsientoDisponible(0, 0));
	    assertTrue(sala.estaAsientoDisponible(0, 1));
	    assertFalse(sala.estaAsientoDisponible(0, 2));

	    verify(salaRepositoryMock, never()).guardar(any());
	}
	
	@Test
	public void testReservaMultipleExitosa() {

	    
	    Sala sala = new Sala(1, 1, 3);

	   
	    when(salaRepositoryMock.buscarPorId(1))
	        .thenReturn(Optional.of(sala));

	   
	    List<int[]> asientos = List.of(
	        new int[]{0, 0},
	        new int[]{0, 1}
	    );

	    
	    boolean resultado = reservaService.realizarReservaMultiple(1, asientos);

	   
	    assertTrue(resultado);

	  
	    assertFalse(sala.estaAsientoDisponible(0, 0));
	    assertFalse(sala.estaAsientoDisponible(0, 1));

	 
	    assertTrue(sala.estaAsientoDisponible(0, 2));

	  
	    verify(salaRepositoryMock, times(1)).guardar(sala);
	}
	
	@Test
	public void testReservaMultipleListaVacia() {

	  
	    Sala sala = new Sala(1, 2, 2);

	    when(salaRepositoryMock.buscarPorId(1))
	        .thenReturn(Optional.of(sala));

	   
	    List<int[]> asientos = List.of();

	    assertThrows(IllegalArgumentException.class, () -> {
	    	reservaService.realizarReservaMultiple(1, asientos);
	    });
	 
	    verify(salaRepositoryMock, never()).guardar(any());
	}
	
	@Test
	public void testReservaMultipleCoordenadasInvalidas() {

	    Sala sala = new Sala(1, 2, 2);

	    when(salaRepositoryMock.buscarPorId(1))
	        .thenReturn(Optional.of(sala));

	    List<int[]> asientos = List.of(
	        new int[]{0, 0},
	        new int[]{2, 0}
	    );

	    assertThrows(IllegalArgumentException.class, () -> {
	        reservaService.realizarReservaMultiple(1, asientos);
	    });

	    assertTrue(sala.estaAsientoDisponible(0, 0));

	    verify(salaRepositoryMock, never()).guardar(any());
	}
	
}