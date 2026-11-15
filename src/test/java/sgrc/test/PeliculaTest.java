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

	// ============ CAJA NEGRA: duración, límite inferior ============
	// Particiones: (-inf, 0] inválida | [1, 600] válida. Límites: 0 y 1.

	@Test
	@DisplayName("Duración 0 (valor límite exterior inferior) lanza excepción")
	public void testDuracionCeroLimite() {
		assertThrows(IllegalArgumentException.class, () -> new Pelicula("Corto", 0));
	}

	@Test
	@DisplayName("Duración 1 (valor límite interior inferior) es válida")
	public void testDuracionUnoLimite() {
		assertEquals(1, new Pelicula("Corto", 1).getDuracionMinutos());
	}

	@Test
	@DisplayName("Duración negativa (partición inválida) lanza excepción")
	public void testDuracionNegativaParticion() {
		assertThrows(IllegalArgumentException.class, () -> new Pelicula("Corto", -30));
	}

	// ============ CAJA NEGRA: título ============
	// Particiones: null | vacío "" | solo espacios "   " | texto válido.

	@Test
	@DisplayName("Título con solo espacios (partición inválida) lanza excepción")
	public void testTituloSoloEspaciosParticion() {
		assertThrows(IllegalArgumentException.class, () -> new Pelicula("   ", 120));
	}

	@Test
	@DisplayName("Título de un carácter (valor límite interior) es válido")
	public void testTituloUnCaracterLimite() {
		assertEquals("M", new Pelicula("M", 99).getTitulo());
	}

	// ============ CAJA BLANCA: decision coverage + condition coverage ============
	// D1: (titulo == null || titulo.trim().isEmpty())                -> C1 || C2
	// D2: (duracion <= 0 || duracion > DURACION_MAXIMA)              -> C3 || C4
	//   Caso | C1 | C2         | C3         | C4         | D1 | D2
	//   A    | T  | (no eval.) | -          | -          | T  | -
	//   B    | F  | T          | -          | -          | T  | -
	//   C    | F  | F          | T          | (no eval.) | F  | T
	//   D    | F  | F          | F          | T          | F  | T
	//   E    | F  | F          | F          | F          | F  | F
	@Test
	@DisplayName("Caja blanca: decision y condition coverage del constructor de Pelicula")
	public void testConstructorDecisionConditionCoverage() {
		assertThrows(IllegalArgumentException.class, () -> new Pelicula(null, 100));  // A
		assertThrows(IllegalArgumentException.class, () -> new Pelicula(" ", 100));   // B
		assertThrows(IllegalArgumentException.class, () -> new Pelicula("X", 0));     // C
		assertThrows(IllegalArgumentException.class, () -> new Pelicula("X", 601));   // D
		assertDoesNotThrow(() -> new Pelicula("X", 100));                            // E
	}
}
