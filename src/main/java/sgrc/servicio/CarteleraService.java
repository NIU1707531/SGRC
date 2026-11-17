package sgrc.servicio;

import sgrc.modelo.Pelicula;
import sgrc.repositorio.PeliculaRepository;
import java.util.List;

/**
 * Servicio del Model que gestiona la cartelera de películas.
 *
 * Contiene las reglas de negocio de la cartelera (por ejemplo, que no haya dos
 * películas con el mismo título) y delega el almacenamiento en el repositorio.
 */
public class CarteleraService {
    // Se recibe por el constructor para poder usar un mock en los tests
    private final PeliculaRepository peliculaRepository;

    /** Pre: peliculaRepository != null. */
    public CarteleraService(PeliculaRepository peliculaRepository) {
        if (peliculaRepository == null) {
            throw new IllegalArgumentException("El repositorio de películas no puede ser nulo");
        }
        this.peliculaRepository = peliculaRepository;
    }

    // Devuelve todas las películas de la cartelera (lista vacía si no hay ninguna)
    public List<Pelicula> obtenerCartelera() {
        return peliculaRepository.obtenerTodas();
    }

    /**
     * Pre: pelicula != null (IllegalArgumentException) y su título no existe
     * todavía en la cartelera (IllegalStateException).
     */
    public void agregarPelicula(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("La película no puede ser nula");
        }
        // Regla de negocio: no se permiten títulos repetidos (sin distinguir mayúsculas)
        if (peliculaRepository.buscarPorTitulo(pelicula.getTitulo()).isPresent()) {
            throw new IllegalStateException("Ya existe una película con el título " + pelicula.getTitulo());
        }
        peliculaRepository.guardar(pelicula);
    }

    /** Elimina la película. Devuelve false si no existía. */
    public boolean eliminarPelicula(String titulo) {
        // Se comprueba antes si existe para poder informar al usuario con el valor devuelto
        if (peliculaRepository.buscarPorTitulo(titulo).isEmpty()) {
            return false;
        }
        peliculaRepository.eliminar(titulo);
        return true;
    }
}
