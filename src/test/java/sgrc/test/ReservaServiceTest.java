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
	
	@Test
	public void testReservaMultipleCoordenadasDuplicadas() {

	    Sala sala = new Sala(1, 1, 3);

	    when(salaRepositoryMock.buscarPorId(1))
	        .thenReturn(Optional.of(sala));

	    List<int[]> asientos = List.of(
	        new int[]{0, 0},
	        new int[]{0, 1},
	        new int[]{0, 0}
	    );

	    boolean resultado = reservaService.realizarReservaMultiple(1, asientos);

	    assertFalse(resultado);

	    assertTrue(sala.estaAsientoDisponible(0, 0));
	    assertTrue(sala.estaAsientoDisponible(0, 1));
	    assertTrue(sala.estaAsientoDisponible(0, 2));

	    verify(salaRepositoryMock, never()).guardar(any());
	}
	
	// =====================================================================
	// CAJA BLANCA - PATH COVERAGE de realizarReservaMultiple
	// (diagrama de flujo en docs/GUIA-INFORME.md)
	//   Camino 1: la sala no existe                         -> excepción
	//   Camino 2: lista null o vacía                         -> excepción
	//   Camino 3: posición null o longitud != 2              -> excepción
	//   Camino 4: coordenada duplicada                       -> false
	//   Camino 5: asiento ocupado                            -> false
	//   Camino 6: asiento fuera de límites                   -> excepción (desde Sala)
	//   Camino 7: todos libres -> reserva + guardar          -> true
	// Caminos 2, 4, 5, 6 y 7: tests anteriores (ListaVacia, CoordenadasDuplicadas,
	// ConAsientoOcupado, CoordenadasInvalidas y ReservaMultipleExitosa).
	// =====================================================================

	@Test
	public void testReservaMultipleSalaNoExiste() {
		// Camino 1: buscarPorId devuelve vacío
		when(salaRepositoryMock.buscarPorId(7)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class,
				() -> reservaService.realizarReservaMultiple(7, List.of(new int[]{0, 0})));
		verify(salaRepositoryMock, never()).guardar(any());
	}

	// CAJA BLANCA - DECISION + CONDITION COVERAGE de las decisiones de realizarReservaMultiple
	// D2: (asientos == null || asientos.isEmpty())   -> C1 || C2
	//     null -> C1=T | vacía -> C1=F,C2=T | con elementos -> C1=F,C2=F
	// D3: (posicion == null || posicion.length != 2) -> C3 || C4
	//     null -> C3=T | {0} -> C3=F,C4=T | {0,0,0} -> C3=F,C4=T | {0,0} -> C3=F,C4=F

	@Test
	public void testReservaMultipleListaNula() {
		// Camino 2 con C1 = true
		when(salaRepositoryMock.buscarPorId(1)).thenReturn(Optional.of(new Sala(1, 2, 2)));

		assertThrows(IllegalArgumentException.class, () -> reservaService.realizarReservaMultiple(1, null));
		verify(salaRepositoryMock, never()).guardar(any());
	}

	@Test
	public void testReservaMultiplePosicionNula() {
		// Camino 3 con C3 = true
		when(salaRepositoryMock.buscarPorId(1)).thenReturn(Optional.of(new Sala(1, 2, 2)));
		List<int[]> asientos = new java.util.ArrayList<>();
		asientos.add(null);

		assertThrows(IllegalArgumentException.class, () -> reservaService.realizarReservaMultiple(1, asientos));
		verify(salaRepositoryMock, never()).guardar(any());
	}

	@Test
	public void testReservaMultiplePosicionUnaCoordenada() {
		// Camino 3 con C3 = false, C4 = true (longitud 1: frontera inferior de 2)
		when(salaRepositoryMock.buscarPorId(1)).thenReturn(Optional.of(new Sala(1, 2, 2)));

		assertThrows(IllegalArgumentException.class,
				() -> reservaService.realizarReservaMultiple(1, List.of(new int[]{0})));
		verify(salaRepositoryMock, never()).guardar(any());
	}

	@Test
	public void testReservaMultiplePosicionTresCoordenadas() {
		// Camino 3 con C3 = false, C4 = true (longitud 3: frontera superior de 2)
		when(salaRepositoryMock.buscarPorId(1)).thenReturn(Optional.of(new Sala(1, 2, 2)));

		assertThrows(IllegalArgumentException.class,
				() -> reservaService.realizarReservaMultiple(1, List.of(new int[]{0, 0, 0})));
		verify(salaRepositoryMock, never()).guardar(any());
	}

	// ===================== Contrato: precondición del constructor =====================

	@Test
	public void testConstructorRepositorioNulo() {
		// Partición inválida: sin repositorio el servicio no puede funcionar
		assertThrows(IllegalArgumentException.class, () -> new ReservaService(null));
	}

	// ===================== cancelarReserva =====================
	// Particiones: asiento ocupado (se libera) | asiento libre (no hay nada que cancelar)
	//              | sala inexistente | asiento fuera de límites

	@Test
	public void testCancelarReservaExitosa() {
		Sala sala = new Sala(1, 2, 2);
		sala.reservarAsiento(1, 1);
		when(salaRepositoryMock.buscarPorId(1)).thenReturn(Optional.of(sala));

		assertTrue(reservaService.cancelarReserva(1, 1, 1));
		assertTrue(sala.estaAsientoDisponible(1, 1));
		verify(salaRepositoryMock, times(1)).guardar(sala);
	}

	@Test
	public void testCancelarReservaAsientoLibre() {
		Sala sala = new Sala(1, 2, 2);
		when(salaRepositoryMock.buscarPorId(1)).thenReturn(Optional.of(sala));

		assertFalse(reservaService.cancelarReserva(1, 0, 0));
		verify(salaRepositoryMock, never()).guardar(any());
	}

	@Test
	public void testCancelarReservaSalaNoExiste() {
		when(salaRepositoryMock.buscarPorId(99)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> reservaService.cancelarReserva(99, 0, 0));
		verify(salaRepositoryMock, never()).guardar(any());
	}

	@Test
	public void testCancelarReservaFueraDeLimites() {
		// Valor límite exterior: columna = columnas
		when(salaRepositoryMock.buscarPorId(1)).thenReturn(Optional.of(new Sala(1, 2, 2)));

		assertThrows(IllegalArgumentException.class, () -> reservaService.cancelarReserva(1, 0, 2));
		verify(salaRepositoryMock, never()).guardar(any());
	}
}