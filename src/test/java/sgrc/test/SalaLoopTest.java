package sgrc.test;

import sgrc.modelo.Sala;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CAJA BLANCA - LOOP TESTING de los dos bucles anidados de Sala:
 *   - contarAsientosLibres(): recorre siempre filas x columnas.
 *   - tieneAsientosDisponibles(): sale en cuanto encuentra un asiento libre.
 *
 * Estrategia para bucles anidados: se prueba primero el bucle interior
 * (columnas) fijando el exterior (filas) en su mínimo, y después el bucle
 * exterior fijando el interior en un valor típico. El número mínimo de
 * iteraciones es 1, porque el constructor no admite dimensiones 0
 * (por eso "0 iteraciones" no es un caso alcanzable).
 */
public class SalaLoopTest {

    // Ocupa todos los asientos de la sala (helper de los tests)
    private void llenar(Sala sala, int filas, int columnas) {
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                sala.reservarAsiento(f, c);
            }
        }
    }

    // ---------------- contarAsientosLibres ----------------

    // Bucle interior: exterior fijo en el mínimo (1 fila) y el interior con
    // 1 (mínimo), 2 (mínimo+1) y 7 (valor típico) iteraciones.
    @ParameterizedTest(name = "interior: 1 fila x {0} columnas")
    @CsvSource({"1", "2", "7"})
    @DisplayName("Loop testing interior de contarAsientosLibres")
    public void testContarLibresBucleInterior(int columnas) {
        Sala sala = new Sala(1, 1, columnas);
        assertEquals(columnas, sala.contarAsientosLibres());
    }

    // Bucle exterior: interior fijo en un valor típico (3 columnas) y el
    // exterior con 1 (mínimo), 2 (mínimo+1) y 6 (valor típico) iteraciones.
    @ParameterizedTest(name = "exterior: {0} filas x 3 columnas")
    @CsvSource({"1", "2", "6"})
    @DisplayName("Loop testing exterior de contarAsientosLibres")
    public void testContarLibresBucleExterior(int filas) {
        Sala sala = new Sala(1, filas, 3);
        assertEquals(filas * 3, sala.contarAsientosLibres());
    }

    @Test
    @DisplayName("Loop testing: recorrido completo con todos los asientos ocupados")
    public void testContarLibresSalaLlena() {
        Sala sala = new Sala(1, 4, 5);
        llenar(sala, 4, 5);
        assertEquals(0, sala.contarAsientosLibres());
    }

    @Test
    @DisplayName("Loop testing: el único libre está en la última posición (n-ésima iteración)")
    public void testContarLibresUltimaPosicion() {
        Sala sala = new Sala(1, 4, 5);
        llenar(sala, 4, 5);
        sala.liberarAsiento(3, 4);
        assertEquals(1, sala.contarAsientosLibres());
    }

    // ---------------- tieneAsientosDisponibles ----------------
    // El número de iteraciones depende de dónde está el primer asiento libre.

    @Test
    @DisplayName("Loop testing: libre en la 1a posición -> 1 iteración")
    public void testTieneDisponiblesUnaIteracion() {
        Sala sala = new Sala(1, 3, 3);
        assertTrue(sala.tieneAsientosDisponibles());
    }

    @Test
    @DisplayName("Loop testing: libre en la 2a posición -> 2 iteraciones del bucle interior")
    public void testTieneDisponiblesDosIteraciones() {
        Sala sala = new Sala(1, 3, 3);
        sala.reservarAsiento(0, 0);
        assertTrue(sala.tieneAsientosDisponibles());
    }

    @Test
    @DisplayName("Loop testing: libre en la 2a fila -> 2 iteraciones del bucle exterior")
    public void testTieneDisponiblesSegundaFila() {
        Sala sala = new Sala(1, 3, 3);
        for (int c = 0; c < 3; c++) {
            sala.reservarAsiento(0, c);
        }
        assertTrue(sala.tieneAsientosDisponibles());
    }

    @Test
    @DisplayName("Loop testing: libre en la última posición -> n iteraciones")
    public void testTieneDisponiblesUltimaPosicion() {
        Sala sala = new Sala(1, 3, 3);
        llenar(sala, 3, 3);
        sala.liberarAsiento(2, 2);
        assertTrue(sala.tieneAsientosDisponibles());
    }

    @Test
    @DisplayName("Loop testing: sin libres -> recorrido completo y devuelve false")
    public void testTieneDisponiblesSalaLlena() {
        Sala sala = new Sala(1, 3, 3);
        llenar(sala, 3, 3);
        assertFalse(sala.tieneAsientosDisponibles());
    }

    @Test
    @DisplayName("Loop testing: sala mínima 1x1 (mínimo de ambos bucles)")
    public void testTieneDisponiblesSalaMinima() {
        Sala sala = new Sala(1, 1, 1);
        assertTrue(sala.tieneAsientosDisponibles());
        sala.reservarAsiento(0, 0);
        assertFalse(sala.tieneAsientosDisponibles());
    }
}
