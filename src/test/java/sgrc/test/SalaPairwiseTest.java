package sgrc.test;

import sgrc.modelo.Sala;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * CAJA NEGRA - PAIRWISE TESTING del constructor Sala(id, filas, columnas).
 *
 * Valores elegidos por particiones y valores límite de cada parámetro:
 *   id       -> {-1 (inválido), 0 (límite inválido), 1 (límite válido)}        = 3 valores
 *   filas    -> {-1 (inválido), 0 (límite inválido), 1 (límite válido), 10}    = 4 valores
 *   columnas -> {-1 (inválido), 0 (límite inválido), 1 (límite válido), 10}    = 4 valores
 *
 * Todas las combinaciones serían 3 x 4 x 4 = 48 casos. Con pairwise basta con
 * 16 casos (4 x 4) para cubrir TODAS las parejas de valores de cada par de
 * parámetros: (id, filas), (id, columnas) y (filas, columnas).
 * Construcción: se recorren todas las parejas (filas, columnas) y el id se
 * asigna de forma rotativa (índice filas + índice columnas) mod 3.
 */
public class SalaPairwiseTest {

    @ParameterizedTest(name = "Sala(id={0}, filas={1}, columnas={2}) válida={3}")
    @CsvSource({
        "-1, -1, -1, false",
        " 0, -1,  0, false",
        " 1, -1,  1, false",
        "-1, -1, 10, false",
        " 0,  0, -1, false",
        " 1,  0,  0, false",
        "-1,  0,  1, false",
        " 0,  0, 10, false",
        " 1,  1, -1, false",
        "-1,  1,  0, false",
        " 0,  1,  1, false",
        " 1,  1, 10, true",
        "-1, 10, -1, false",
        " 0, 10,  0, false",
        " 1, 10,  1, true",
        "-1, 10, 10, false"
    })
    @DisplayName("Pairwise del constructor de Sala")
    public void testConstructorPairwise(int id, int filas, int columnas, boolean esValida) {
        if (esValida) {
            assertDoesNotThrow(() -> new Sala(id, filas, columnas));
        } else {
            assertThrows(IllegalArgumentException.class, () -> new Sala(id, filas, columnas));
        }
    }
}
