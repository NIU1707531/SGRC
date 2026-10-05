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
}
