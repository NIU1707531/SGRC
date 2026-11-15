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
		
	}
	
	
	

}
