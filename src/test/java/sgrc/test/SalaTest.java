package sgrc.test;

import sgrc.modelo.Sala;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class SalaTest {

	@Test
	public void testConstructor()
	{
		
		assertThrows(IllegalArgumentException.class, () -> {
			new Sala(1,0,0);
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			new Sala(1,-1,5);
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			new Sala(1,-1,-1);
		});
		
	
		assertThrows(IllegalArgumentException.class, () -> {
		    new Sala(1,5,0);
		});

		
		assertDoesNotThrow(() -> {
		   new Sala(1,1,1);
		});
		
	}
	
    @Test
    @DisplayName("Debe calcular correctamente la capacidad total de la sala")
    public void testCapacidadTotal() {
        // Arrange
        Sala sala = new Sala(1, 10, 12);

        // Act
        int capacidad = sala.getCapacidadTotal();

        // Assert
        assertEquals(120, capacidad, "La capacidad debe ser filas x columnas");
    }

    @Test
    @DisplayName("Debe permitir reservar un asiento disponible")
    public void testReservarAsientoExitoso() {
        // Arrange
        Sala sala = new Sala(1, 5, 5);

        // Act & Assert
        assertTrue(sala.estaAsientoDisponible(2, 2));
        boolean reservado = sala.reservarAsiento(2, 2);

        assertTrue(reservado, "La reserva debe ser exitosa");
        assertFalse(sala.estaAsientoDisponible(2, 2), "El asiento ya no debe estar disponible");
    }

    @Test
    @DisplayName("No debe permitir reservar un asiento no disponible")
    public void testReservarAsientoNoDisponible()
    {
    	Sala sala=new Sala(1,2,2);
    	
    	sala.reservarAsiento(0, 0);
    	assertFalse(sala.reservarAsiento(0, 0));
    }
    
    
    @Test
    @DisplayName("Debe lanzar excepci�n al intentar acceder a un asiento fuera de limites")
    public void testAsientoFueraDeLimites() {
        // Arrange
        Sala sala = new Sala(1, 5, 5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sala.estaAsientoDisponible(10, 10);
        });
    }
        
        @Test
        @DisplayName("Debe lanzar excepci�n al intentar acceder a un asiento fuera de limites (colum -1)")
        public void testAsientoColumnaInvalidaNegativa() {
            // Arrange
            Sala sala = new Sala(1, 5, 5);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                sala.estaAsientoDisponible(2, -1);
            });
    }
        
        @Test
        @DisplayName("Debe lanzar excepci�n al intentar acceder a un asiento fuera de limites (colum 5)")
        public void testAsientoColumnaInvalida() {
            // Arrange
            Sala sala = new Sala(1, 5, 5);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                sala.estaAsientoDisponible(2, 5);
            });
    }
     
        @Test
        @DisplayName("Debe lanzar excepci�n al intentar acceder a un asiento fuera de limites (fila -1)")
        public void testAsientoFilaInvalidaNegativa() {
            // Arrange
            Sala sala = new Sala(1, 5, 5);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                sala.estaAsientoDisponible(-1, 2);
            });
    }
        
        @Test
        @DisplayName("Debe lanzar excepci�n al intentar acceder a un asiento fuera de limites (fila 5)")
        public void testAsientoFilaInvalida() {
            // Arrange
            Sala sala = new Sala(1, 5, 5);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                sala.estaAsientoDisponible(5, 2);
            });
    }
        
        @Test
        @DisplayName("Intentar acceder a un asiento frontera (0)")
        public void testAsientoFronteraInteriorValida() {
            // Arrange
            Sala sala = new Sala(1, 5, 5);

            assertTrue(sala.estaAsientoDisponible(0, 0));
    }
        @Test
        @DisplayName("Intentar acceder a un asiento frontera (4")
        public void testAsientoFronteraExteriorValida() {
            // Arrange
            Sala sala = new Sala(1, 5, 5);

            assertTrue(sala.estaAsientoDisponible(4,4));
    }
    
        @Test
        public void testLiberar()
        {
        	Sala sala=new Sala(1,2,2);
        	
        	sala.reservarAsiento(0, 0);
        	assertTrue(sala.liberarAsiento(0, 0));
        	assertTrue(sala.estaAsientoDisponible(0, 0));
        }
        
        @Test
        public void testLiberarAsientoYaLibre() {

        	Sala sala=new Sala(1,2,2);

        	assertFalse(sala.liberarAsiento(0, 0));

        	assertTrue(sala.estaAsientoDisponible(0, 0));
            
        }
        
        
    
   
}