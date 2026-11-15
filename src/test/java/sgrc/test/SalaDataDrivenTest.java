package sgrc.test;

import sgrc.modelo.Sala;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * AUTOMATIZACIÓN - DATA DRIVEN TESTING de Sala.estaAsientoDisponible.
 *
 * Los casos de prueba (particiones equivalentes y valores límite/frontera)
 * están en el fichero src/test/resources/datos/sala_estaAsientoDisponible.csv,
 * separados del código del test. Para añadir un caso basta con añadir una fila.
 * Sala usada en todos los casos: 5 x 5 (filas y columnas válidas de 0 a 4).
 */
public class SalaDataDrivenTest {

    private static final int FILAS = 5;
    private static final int COLUMNAS = 5;

    @ParameterizedTest(name = "[{0}] {1}: ({2},{3}) -> {5}")
    @CsvFileSource(resources = "/datos/sala_estaAsientoDisponible.csv", numLinesToSkip = 1)
    @DisplayName("Data driven: estaAsientoDisponible")
    public void testEstaAsientoDisponibleDataDriven(String tipo, String descripcion, int fila, int columna,
            boolean reservadoAntes, String esperado) {
        Sala sala = new Sala(1, FILAS, COLUMNAS);
        if (reservadoAntes) {
            sala.reservarAsiento(fila, columna);
        }

        if ("EXCEPCION".equals(esperado)) {
            assertThrows(IllegalArgumentException.class, () -> sala.estaAsientoDisponible(fila, columna));
        } else {
            assertEquals("LIBRE".equals(esperado), sala.estaAsientoDisponible(fila, columna));
        }
    }
}
