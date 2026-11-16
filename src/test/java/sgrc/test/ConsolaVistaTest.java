package sgrc.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sgrc.modelo.Pelicula;
import sgrc.modelo.Sala;
import sgrc.vista.ConsolaVista;

/**
 * Tests de la Vista por consola. El enunciado no exige testear la Vista,
 * pero se comprueba lo que se escribe en pantalla redirigiendo System.out / System.err
 * a un buffer y lo que se lee simulando System.in.
 */
public class ConsolaVistaTest {

	private final PrintStream salidaOriginal = System.out;
	private final PrintStream errorOriginal = System.err;
	private final InputStream entradaOriginal = System.in;

	// Sin terminal real (Eclipse, Maven) la pantalla se "limpia" con muchas líneas en blanco
	private static final String PANTALLA_LIMPIA = "\n".repeat(30);

	// usamos buffers porque necesitamos verificar lo que sale por pantalla
	private ByteArrayOutputStream salida;
	private ByteArrayOutputStream error;

	@BeforeEach
	public void redirigirSalida() {
		salida = new ByteArrayOutputStream();
		error = new ByteArrayOutputStream();
		System.setOut(new PrintStream(salida, true, StandardCharsets.UTF_8));
		System.setErr(new PrintStream(error, true, StandardCharsets.UTF_8));
	}

	@AfterEach
	public void restaurarConsola() {
		System.setOut(salidaOriginal);
		System.setErr(errorOriginal);
		System.setIn(entradaOriginal);
	}

	// Crea una vista que leerá el texto indicado como si lo escribiera el usuario
	private ConsolaVista vistaConEntrada(String texto) {
		System.setIn(new ByteArrayInputStream(texto.getBytes(StandardCharsets.UTF_8)));
		return new ConsolaVista();
	}

	private String textoSalida() {
		return salida.toString(StandardCharsets.UTF_8);
	}

	private String textoError() {
		return error.toString(StandardCharsets.UTF_8);
	}

	// ---------- Menú ----------

	@Test
	public void testMostrarMenu() {
		new ConsolaVista().mostrarMenu();
		String texto = textoSalida();

		assertTrue(texto.contains("MENÚ PRINCIPAL"));
		assertTrue(texto.contains("1. Ver cartelera"));
		assertTrue(texto.contains("2. Añadir película"));
		assertTrue(texto.contains("3. Eliminar película"));
		assertTrue(texto.contains("4. Ver salas disponibles"));
		assertTrue(texto.contains("5. Ver asientos de una sala"));
		assertTrue(texto.contains("6. Reservar asiento"));
		assertTrue(texto.contains("7. Reservar varios asientos"));
		assertTrue(texto.contains("8. Cancelar reserva"));
		assertTrue(texto.contains("0. Salir"));
	}

	// El cartel de bienvenida se muestra solo la primera vez que aparece el menú
	@Test
	public void testBienvenidaSoloLaPrimeraVez() {
		ConsolaVista vista = vistaConEntrada("\n");
		vista.mostrarMenu();
		vista.mostrarMenu();
		String texto = textoSalida();

		int primera = texto.indexOf("Gestión de Reservas de Cine");
		assertTrue(primera >= 0);
		assertEquals(-1, texto.indexOf("Gestión de Reservas de Cine", primera + 1));
	}

	// La primera vez el menú sale directamente, sin pausa
	@Test
	public void testPrimerMenuSinPausa() {
		vistaConEntrada("").mostrarMenu();
		assertFalse(textoSalida().contains("Pulsa Enter"));
	}

	// A partir de la segunda vez se espera a que el usuario lea el resultado antes de limpiar
	@Test
	public void testMenuEsperaEnterYLimpiaPantalla() {
		ConsolaVista vista = vistaConEntrada("\nsiguiente\n");
		vista.mostrarMenu();
		salida.reset();
		vista.mostrarMenu();
		String texto = textoSalida();

		assertTrue(texto.contains("Pulsa Enter para volver al menú"));
		assertTrue(texto.contains(PANTALLA_LIMPIA));
		assertTrue(texto.indexOf(PANTALLA_LIMPIA) < texto.indexOf("MENÚ PRINCIPAL"));
		// La pausa consume solo una línea: la siguiente queda para la próxima lectura
		assertEquals("siguiente", vista.leerTexto(""));
	}

	// Al elegir una opción válida se limpia la pantalla para mostrar solo su resultado
	@Test
	public void testLeerOpcionLimpiaPantalla() {
		ConsolaVista vista = vistaConEntrada("1\n");
		vista.leerOpcion();
		assertTrue(textoSalida().contains(PANTALLA_LIMPIA));
	}

	@Test
	public void testMostrarDespedida() {
		new ConsolaVista().mostrarDespedida();
		assertTrue(textoSalida().contains("¡Hasta pronto!"));
	}

	// ---------- Lectura de datos ----------

	@Test
	public void testLeerOpcion() {
		ConsolaVista vista = vistaConEntrada("2\n");
		assertEquals(2, vista.leerOpcion());
		assertTrue(textoSalida().contains("Elige una opción"));
	}

	// Partición no válida: texto que no es un número -> se avisa y se vuelve a pedir
	@Test
	public void testLeerOpcionNoNumericaVuelveAPedir() {
		ConsolaVista vista = vistaConEntrada("abc\n\n3\n");
		assertEquals(3, vista.leerOpcion());
		assertTrue(textoError().contains("Introduce un número"));
	}

	// Fin de la entrada (Ctrl+Z / Ctrl+D): se interpreta como "Salir" en lugar de lanzar una excepción
	@Test
	public void testLeerOpcionSinEntradaDevuelveSalir() {
		ConsolaVista vista = vistaConEntrada("");
		assertEquals(0, vista.leerOpcion());
	}

	@Test
	public void testLeerTexto() {
		ConsolaVista vista = vistaConEntrada("Titanic\n");
		assertEquals("Titanic", vista.leerTexto("Título de la película: "));
		assertTrue(textoSalida().contains("Título de la película: "));
	}

	@Test
	public void testLeerEntero() {
		ConsolaVista vista = vistaConEntrada("2\n");
		assertEquals(2, vista.leerEntero("Duración de la película en minutos: "));
	}

	// Valor límite: número negativo con espacios -> se acepta, la validación es del Model
	@Test
	public void testLeerEnteroNegativoConEspacios() {
		ConsolaVista vista = vistaConEntrada("  -5  \n");
		assertEquals(-5, vista.leerEntero("Fila: "));
	}

	@Test
	public void testLeerEnteroNoNumericoVuelveAPedir() {
		ConsolaVista vista = vistaConEntrada("dos\n2\n");
		assertEquals(2, vista.leerEntero("Fila: "));
		assertTrue(textoError().contains("Introduce un número"));
	}

	@Test
	public void testLeerEnteroSinEntradaLanzaExcepcion() {
		ConsolaVista vista = vistaConEntrada("");
		assertThrows(IllegalStateException.class, () -> vista.leerEntero("Fila: "));
	}

	// ---------- Mensajes ----------

	@Test
	public void testMostrarMensaje() {
		new ConsolaVista().mostrarMensaje("Reserva realizada");
		assertTrue(textoSalida().contains("Reserva realizada"));
	}

	@Test
	public void testMostrarError() {
		new ConsolaVista().mostrarError("El asiento ya está ocupado");
		assertTrue(textoError().contains("El asiento ya está ocupado"));
	}

	// ---------- Cartelera ----------

	@Test
	public void testMostrarCartelera() {
		List<Pelicula> peliculas = List.of(new Pelicula("Titanic", 195), new Pelicula("Up", 96));
		new ConsolaVista().mostrarCartelera(peliculas);
		String texto = textoSalida();

		assertTrue(texto.contains("CARTELERA"));
		assertTrue(texto.contains("Titanic"));
		assertTrue(texto.contains("195 minutos"));
		assertTrue(texto.contains("3h 15min"));
		assertTrue(texto.contains("Up"));
		assertTrue(texto.contains("1h 36min"));
	}

	// Valor límite: lista vacía
	@Test
	public void testMostrarCarteleraVacia() {
		new ConsolaVista().mostrarCartelera(List.of());
		assertTrue(textoSalida().contains("La cartelera está vacía"));
	}

	// Un título más largo que la columna se recorta para no romper la tabla
	@Test
	public void testMostrarCarteleraTituloLargoSeRecorta() {
		String titulo = "El Señor de los Anillos: La Comunidad del Anillo (versión extendida)";
		new ConsolaVista().mostrarCartelera(List.of(new Pelicula(titulo, 228)));
		String texto = textoSalida();

		assertFalse(texto.contains(titulo));
		assertTrue(texto.contains("El Señor de los Anillos"));
		assertTrue(texto.contains("…"));
	}

	// ---------- Salas ----------

	@Test
	public void testMostrarSalas() {
		Sala sala = new Sala(7, 3, 4);
		sala.reservarAsiento(0, 0);
		new ConsolaVista().mostrarSalas(List.of(sala));
		String texto = textoSalida();

		assertTrue(texto.contains("Sala 7"));
		assertTrue(texto.contains("3 x 4"));
		assertTrue(texto.contains("11 / 12"));
	}

	@Test
	public void testMostrarSalasVacia() {
		new ConsolaVista().mostrarSalas(List.of());
		assertTrue(textoSalida().contains("No hay salas con asientos disponibles"));
	}

	@Test
	public void testMostrarSala() {
		Sala sala = new Sala(7, 2, 3);
		sala.reservarAsiento(0, 1);
		new ConsolaVista().mostrarSala(sala);
		String texto = textoSalida();

		assertTrue(texto.contains("Sala 7"));
		assertTrue(texto.contains("PANTALLA"));
		assertTrue(texto.contains("Fila  1 │  ○  ●  ○"));
		assertTrue(texto.contains("Fila  2 │  ○  ○  ○"));
		assertTrue(texto.contains("5 de 6 asientos libres"));
	}
}
