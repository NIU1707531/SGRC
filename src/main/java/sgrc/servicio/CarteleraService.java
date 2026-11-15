package sgrc.servicio;

import sgrc.modelo.Pelicula;
import sgrc.repositorio.PeliculaRepository;
import java.util.List;

/**
 * Servicio del Model que gestiona la cartelera de películas.
 */
public class CarteleraService {
    private final PeliculaRepository peliculaRepository;

    /** Pre: peliculaRepository != null. */
    public CarteleraService(PeliculaRepository peliculaRepository) {
        if (peliculaRepository == null) {
            throw new IllegalArgumentException("El repositorio de películas no puede ser nulo");
        }
        this.peliculaRepository = peliculaRepository;
    }

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
        if (peliculaRepository.buscarPorTitulo(pelicula.getTitulo()).isPresent()) {
            throw new IllegalStateException("Ya existe una película con el título " + pelicula.getTitulo());
        }
        peliculaRepository.guardar(pelicula);
    }

    /** Elimina la película. Devuelve false si no existía. */
    public boolean eliminarPelicula(String titulo) {
        if (peliculaRepository.buscarPorTitulo(titulo).isEmpty()) {
            return false;
        }
        peliculaRepository.eliminar(titulo);
        return true;
    }
}
