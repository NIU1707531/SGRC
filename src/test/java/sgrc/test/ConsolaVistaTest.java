package sgrc.test;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import sgrc.vista.ConsolaVista;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import sgrc.modelo.Pelicula;
import java.util.List;
import sgrc.modelo.Sala;

public class ConsolaVistaTest {
	
	@Test
	public void testMostrarMenu() {
		PrintStream salidaOriginal = System.out;

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		//usamos buffer porque necesitamos verificar lo que sale y hay en la pantalla
		System.setOut(new PrintStream(buffer));
		String texto;
		try {
			ConsolaVista vista = new ConsolaVista();
			vista.mostrarMenu();
			texto = buffer.toString();
		}
		finally {
		    System.setOut(salidaOriginal);

		}
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
	
	@Test
	public void testLeerOpcion() {
	    ByteArrayInputStream entrada = new ByteArrayInputStream("2\n".getBytes());
	    InputStream entradaOriginal = System.in;

	    try {
	        System.setIn(entrada);

	        ConsolaVista vista = new ConsolaVista();
	        int opcion = vista.leerOpcion();

	        assertEquals(2, opcion);

	    } finally {
	        System.setIn(entradaOriginal);
	    }
	}
	
	@Test
	public void testLeerTexto() {
	    ByteArrayInputStream entrada =
	            new ByteArrayInputStream("Titanic\n".getBytes());

	    InputStream entradaOriginal = System.in;

	    try {
	        System.setIn(entrada);

	        ConsolaVista vista = new ConsolaVista();
	        String texto = vista.leerTexto("Título de la película: ");

	        assertEquals("Titanic", texto);

	    } finally {
	        System.setIn(entradaOriginal);
	    }
	}
	
	@Test
	public void testLeerEntero() {
	    ByteArrayInputStream entrada =
	            new ByteArrayInputStream("2\n".getBytes());

	    InputStream entradaOriginal = System.in;

	    try {
	        System.setIn(entrada);

	        ConsolaVista vista = new ConsolaVista();
	        int texto = vista.leerEntero("Duración de la película en minutos: ");

	        assertEquals(2, texto);

	    } finally {
	        System.setIn(entradaOriginal);
	    }
	}
	
	@Test
	public void testMostrarMensaje() {

	    PrintStream salidaOriginal = System.out;
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	    try {
	        System.setOut(new PrintStream(buffer));

	        ConsolaVista vista = new ConsolaVista();
	        vista.mostrarMensaje("Reserva realizada");

	    } finally {
	        System.setOut(salidaOriginal);
	    }

	    assertTrue(buffer.toString().contains("Reserva realizada"));
	}
	
	@Test
	public void testMostrarError() {

	    PrintStream salidaOriginal = System.err;
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	    try {
	        System.setErr(new PrintStream(buffer));

	        ConsolaVista vista = new ConsolaVista();
	        vista.mostrarError("El asiento ya está ocupado");

	    } finally {
	        System.setErr(salidaOriginal);
	    }

	    assertTrue(buffer.toString().contains("El asiento ya está ocupado"));
	}
	
	@Test
	public void testMostrarCartelera() {

	    Pelicula pelicula = new Pelicula("Titanic", 195);

	    List<Pelicula> peliculas = List.of(pelicula);
	    
	    PrintStream salidaOriginal = System.out;
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	    try {
	        System.setOut(new PrintStream(buffer));

	        ConsolaVista vista = new ConsolaVista();
	        vista.mostrarCartelera(peliculas);

	    } finally {
	        System.setOut(salidaOriginal);
	    }

	    assertTrue(buffer.toString().contains("Titanic"));
	    assertTrue(buffer.toString().contains("195 minutos"));

	}
	
	@Test
	public void testMostrarSalas() {

	    Sala sala = new Sala(7, 3, 4);
	    List<Sala> salas = List.of(sala);

	    PrintStream salidaOriginal = System.out;
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	    try {
	        System.setOut(new PrintStream(buffer));

	        ConsolaVista vista = new ConsolaVista();
	        vista.mostrarSalas(salas);

	    } finally {
	        System.setOut(salidaOriginal);
	    }

	    assertTrue(buffer.toString().contains("Sala 7"));
	}
	
	@Test
	public void testMostrarSala() {

	    Sala sala = new Sala(7, 2, 3);

	    sala.reservarAsiento(0, 1);
	    PrintStream salidaOriginal = System.out;
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	    try {
	        System.setOut(new PrintStream(buffer));

	        ConsolaVista vista = new ConsolaVista();
	        vista.mostrarSala(sala);

	    } finally {
	        System.setOut(salidaOriginal);
	    }

	    assertTrue(buffer.toString().contains("Sala 7"));

	}
	
	
	
	

}
