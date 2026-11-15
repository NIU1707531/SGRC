package sgrc.test;

import sgrc.modelo.Pelicula;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * AUTOMATIZACIÓN - DATA DRIVEN TESTING del constructor de Pelicula.
 *
 * Los datos (particiones equivalentes y valores límite del título y de la
 * duración) están en src/test/resources/datos/pelicula_constructor.csv.
 * Una celda vacía sin comillas se interpreta como null y "" como cadena vacía.
 */
public class PeliculaDataDrivenTest {

    @ParameterizedTest(name = "[{0}] {1} -> {4}")
    @CsvFileSource(resources = "/datos/pelicula_constructor.csv", numLinesToSkip = 1)
    @DisplayName("Data driven: constructor de Pelicula")
    public void testConstructorDataDriven(String tipo, String descripcion, String titulo, int duracion,
            String esperado) {
        if ("EXCEPCION".equals(esperado)) {
            assertThrows(IllegalArgumentException.class, () -> new Pelicula(titulo, duracion));
        } else {
            Pelicula pelicula = new Pelicula(titulo, duracion);
            assertEquals(titulo.trim(), pelicula.getTitulo());
            assertEquals(duracion, pelicula.getDuracionMinutos());
        }
    }
}
