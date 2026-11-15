package sgrc.test;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import sgrc.vista.ConsolaVista;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
	
	
	
	
	

}
