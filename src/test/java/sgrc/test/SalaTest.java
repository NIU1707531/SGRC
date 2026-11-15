package sgrc.test;

import sgrc.modelo.Sala;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class SalaTest {

    // ============ CAJA NEGRA: constructor, parámetros filas y columnas ============
    // Particiones: (-inf, 0] inválida | [1, +inf) válida. Límites/frontera: 0 y 1.

    @Test
    @DisplayName("Constructor: filas = 0 (valor límite exterior) lanza excepción")
    public void testConstructorFilasCeroLimite() {
        assertThrows(IllegalArgumentException.class, () -> new Sala(1, 0, 5));
    }

    @Test
    @DisplayName("Constructor: filas negativas (partición inválida) lanza excepción")
    public void testConstructorFilasNegativasParticion() {
        assertThrows(IllegalArgumentException.class, () -> new Sala(1, -1, 5));
    }

    @Test
    @DisplayName("Constructor: columnas = 0 (valor límite exterior) lanza excepción")
    public void testConstructorColumnasCeroLimite() {
        assertThrows(IllegalArgumentException.class, () -> new Sala(1, 5, 0));
    }

    @Test
    @DisplayName("Constructor: columnas negativas (partición inválida) lanza excepción")
    public void testConstructorColumnasNegativasParticion() {
        assertThrows(IllegalArgumentException.class, () -> new Sala(1, 5, -1));
    }

    @Test
    @DisplayName("Constructor: filas y columnas negativas (ambas inválidas) lanza excepción")
    public void testConstructorAmbasNegativasParticion() {
        assertThrows(IllegalArgumentException.class, () -> new Sala(1, -1, -1));
    }

    @Test
    @DisplayName("Constructor: sala 1x1 (valores límite interiores) es válida")
    public void testConstructorMinimoValidoLimite() {
        Sala sala = new Sala(1, 1, 1);
        assertEquals(1, sala.getCapacidadTotal());
    }

    @Test
    @DisplayName("Constructor: sala 10x12 (partición válida, valor típico) es válida")
    public void testConstructorValorTipicoParticion() {
        assertDoesNotThrow(() -> new Sala(3, 10, 12));
    }

    // ============ CAJA BLANCA: decision coverage + condition coverage ============
    // Decisión D1: (id <= 0)                      -> C1
    // Decisión D2: (filas <= 0 || columnas <= 0)  -> C2 || C3
    //   Caso | C1 | C2 | C3         | D1 | D2
    //   A    | T  | -  | -          | T  | -    new Sala(0, 2, 2)
    //   B    | F  | T  | (no eval.) | F  | T    new Sala(1, 0, 2)
    //   C    | F  | F  | T          | F  | T    new Sala(1, 2, 0)
    //   D    | F  | F  | F          | F  | F    new Sala(1, 2, 2)
    // Cada condición y cada decisión toma los valores true y false.
    @Test
    @DisplayName("Caja blanca: decision y condition coverage del constructor")
    public void testConstructorDecisionConditionCoverage() {
        assertThrows(IllegalArgumentException.class, () -> new Sala(0, 2, 2)); // A
        assertThrows(IllegalArgumentException.class, () -> new Sala(1, 0, 2)); // B
        assertThrows(IllegalArgumentException.class, () -> new Sala(1, 2, 0)); // C
        assertDoesNotThrow(() -> new Sala(1, 2, 2));                          // D
    }

    // ===================== CAJA NEGRA: constructor, parámetro id =====================
    // Particiones de id: (-inf, 0] inválida | [1, +inf) válida. Límite/frontera: 0 y 1.

    @Test
    @DisplayName("Constructor: id = 0 (valor límite exterior) lanza excepción")
    public void testConstructorIdCeroLimite() {
        assertThrows(IllegalArgumentException.class, () -> new Sala(0, 2, 2));
    }

    @Test
    @DisplayName("Constructor: id negativo (partición inválida) lanza excepción")
    public void testConstructorIdNegativoParticion() {
        assertThrows(IllegalArgumentException.class, () -> new Sala(-5, 2, 2));
    }

    @Test
    @DisplayName("Constructor: id = 1 (valor límite interior) es válido")
    public void testConstructorIdUnoLimite() {
        Sala sala = new Sala(1, 2, 2);
        assertEquals(1, sala.getId());
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
    @DisplayName("Debe lanzar excepción al intentar acceder a un asiento fuera de limites")
    public void testAsientoFueraDeLimites() {
        // Arrange
        Sala sala = new Sala(1, 5, 5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sala.estaAsientoDisponible(10, 10);
        });
    }
        
        @Test
        @DisplayName("Debe lanzar excepción al intentar acceder a un asiento fuera de limites (colum -1)")
        public void testAsientoColumnaInvalidaNegativa() {
            // Arrange
            Sala sala = new Sala(1, 5, 5);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                sala.estaAsientoDisponible(2, -1);
            });
    }
        
        @Test
        @DisplayName("Debe lanzar excepción al intentar acceder a un asiento fuera de limites (colum 5)")
        public void testAsientoColumnaInvalida() {
            // Arrange
            Sala sala = new Sala(1, 5, 5);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                sala.estaAsientoDisponible(2, 5);
            });
    }
     
        @Test
        @DisplayName("Debe lanzar excepción al intentar acceder a un asiento fuera de limites (fila -1)")
        public void testAsientoFilaInvalidaNegativa() {
            // Arrange
            Sala sala = new Sala(1, 5, 5);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                sala.estaAsientoDisponible(-1, 2);
            });
    }
        
        @Test
        @DisplayName("Debe lanzar excepción al intentar acceder a un asiento fuera de limites (fila 5)")
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
        @DisplayName("Intentar acceder a un asiento frontera (4)")
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
        
        @Test
        public void testLiberarAsientoFueraDeLimites()
        {
        	Sala sala=new Sala(1,2,2);

        	assertThrows(IllegalArgumentException.class,()->
        	{sala.liberarAsiento(0, 2);
        		});
     
        }
        @Test
        public void testTieneAsientosDisponibles() {

            // ARRANGE
            Sala sala = new Sala(1, 1, 1);

            // ASSERT
            assertTrue(sala.tieneAsientosDisponibles());

            // ACT
            sala.reservarAsiento(0, 0);

            // ASSERT
            assertFalse(sala.tieneAsientosDisponibles());
        }
        
        
    
   

    // ===================== contarAsientosLibres =====================

    @Test
    @DisplayName("Sala nueva: todos los asientos están libres")
    public void testContarAsientosLibresSalaNueva() {
        Sala sala = new Sala(1, 3, 4);
        assertEquals(12, sala.contarAsientosLibres());
    }

    @Test
    @DisplayName("Tras reservar un asiento, hay uno libre menos")
    public void testContarAsientosLibresTrasReservar() {
        Sala sala = new Sala(1, 3, 4);
        sala.reservarAsiento(1, 2);
        assertEquals(11, sala.contarAsientosLibres());
    }
}