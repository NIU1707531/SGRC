package sgrc.repositorio;

import sgrc.modelo.Pelicula;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de películas (BD ficticia).
 * Los títulos se comparan sin distinguir mayúsculas ni espacios exteriores.
 */
public class PeliculaRepositoryImpl implements PeliculaRepository {

    // Simulación de la base de datos en memoria (BD Ficticia)
    private final List<Pelicula> cartelera = new ArrayList<>();

    @Override
    public void guardar(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("La película no puede ser nula");
        }
        // Si ya existe una película con el mismo título, la reemplazamos
        cartelera.removeIf(p -> mismoTitulo(p, pelicula.getTitulo()));
        cartelera.add(pelicula);
    }

    @Override
    public Optional<Pelicula> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return Optional.empty();
        }
        return cartelera.stream()
                .filter(p -> mismoTitulo(p, titulo))
                .findFirst();
    }

    @Override
    public List<Pelicula> obtenerTodas() {
        // Devolvemos una copia para proteger la lista interna de modificaciones externas
        return new ArrayList<>(cartelera);
    }

    @Override
    public void eliminar(String titulo) {
        if (titulo != null) {
            cartelera.removeIf(p -> mismoTitulo(p, titulo));
        }
    }

    // Compara títulos sin distinguir mayúsculas ni espacios exteriores
    private boolean mismoTitulo(Pelicula pelicula, String titulo) {
        return pelicula.getTitulo().equalsIgnoreCase(titulo.trim());
    }
}
