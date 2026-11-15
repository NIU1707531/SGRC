package sgrc.test;

import sgrc.modelo.Pelicula;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class PeliculaTest {

	@Test
	@DisplayName("Debe crear una pelicula y recuperar sus atributos correctamente")
	public void testCrearPelicula() {
		// Arrange & Act
		Pelicula pelicula = new Pelicula("Inception", 148);

		// Assert
		assertEquals("Inception", pelicula.getTitulo());
		assertEquals(148, pelicula.getDuracionMinutos());
	}

	@Test
	@DisplayName("Debe lanzar excepcion si el titulo es nulo o vacio")
	public void testTituloInvalido() {
		assertThrows(IllegalArgumentException.class, () -> new Pelicula("", 120));
		assertThrows(IllegalArgumentException.class, () -> new Pelicula(null, 120));
	}

	// ============ CAJA NEGRA: duración, límite superior ============
	// Nuevo requisito: duración máxima de 600 minutos.
	// Particiones: [1, 600] válida | [601, +inf) inválida. Límites: 600 y 601.

	@Test
	@DisplayName("Duración 600 (valor límite interior superior) es válida")
	public void testDuracionMaximaLimite() {
		assertEquals(600, new Pelicula("Shoah", 600).getDuracionMinutos());
	}

	@Test
	@DisplayName("Duración 601 (valor límite exterior superior) lanza excepción")
	public void testDuracionSuperaMaximoLimite() {
		assertThrows(IllegalArgumentException.class, () -> new Pelicula("Demasiado larga", 601));
	}
}
