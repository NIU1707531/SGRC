package sgrc.test.mocks;

import sgrc.modelo.Pelicula;
import sgrc.repositorio.PeliculaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MOCK OBJECT MANUAL (sin Mockito) de PeliculaRepository.
 *
 * - Las respuestas se configuran desde el test (películas "precargadas").
 * - Registra cada llamada para poder verificar las interacciones.
 */
public class PeliculaRepositoryMock implements PeliculaRepository {

    // Respuestas configuradas por el test
    private final List<Pelicula> peliculasPrecargadas = new ArrayList<>();

    // Registro de llamadas
    private final List<Pelicula> guardadas = new ArrayList<>();
    private final List<String> eliminadas = new ArrayList<>();
    private int vecesObtenerTodas = 0;

    // ---------- Configuración (stubbing) ----------

    public void precargar(Pelicula pelicula) {
        peliculasPrecargadas.add(pelicula);
    }

    // ---------- Implementación simulada ----------

    @Override
    public void guardar(Pelicula pelicula) {
        guardadas.add(pelicula);
    }

    @Override
    public Optional<Pelicula> buscarPorTitulo(String titulo) {
        return peliculasPrecargadas.stream()
                .filter(p -> p.getTitulo().equalsIgnoreCase(titulo))
                .findFirst();
    }

    @Override
    public List<Pelicula> obtenerTodas() {
        vecesObtenerTodas++;
        return new ArrayList<>(peliculasPrecargadas);
    }

    @Override
    public void eliminar(String titulo) {
        eliminadas.add(titulo);
    }

    // ---------- Verificación ----------

    public List<Pelicula> getGuardadas() {
        return guardadas;
    }

    public List<String> getEliminadas() {
        return eliminadas;
    }

    public int getVecesObtenerTodas() {
        return vecesObtenerTodas;
    }
}
