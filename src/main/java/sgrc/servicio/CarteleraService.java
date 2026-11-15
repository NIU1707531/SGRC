package sgrc.servicio;

import sgrc.modelo.Pelicula;
import sgrc.repositorio.PeliculaRepository;
import java.util.List;

public class CarteleraService {
    private final PeliculaRepository peliculaRepository;

    public CarteleraService(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    public List<Pelicula> obtenerCartelera() {
        return peliculaRepository.obtenerTodas();
    }

    public void agregarPelicula(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("La película no puede ser nula");
        }
        if (peliculaRepository.buscarPorTitulo(pelicula.getTitulo()).isPresent()) {
            throw new IllegalStateException("Ya existe una película con el título " + pelicula.getTitulo());
        }
        peliculaRepository.guardar(pelicula);
    }
}