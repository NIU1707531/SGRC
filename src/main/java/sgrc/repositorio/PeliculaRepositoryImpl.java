package sgrc.repositorio;

import sgrc.modelo.Pelicula;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PeliculaRepositoryImpl implements PeliculaRepository {
    
    // Simulación de la base de datos en memoria (BD Ficticia)
    private List<Pelicula> cartelera = new ArrayList<>();

    @Override
    public void guardar(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("La película no puede ser nula");
        }
        // Si ya existe una película con el mismo título, la actualizamos/reemplazamos
        cartelera.removeIf(p -> p.getTitulo().equalsIgnoreCase(pelicula.getTitulo()));
        cartelera.add(pelicula);
    }

    @Override
    public Optional<Pelicula> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return Optional.empty();
        }
        return cartelera.stream()
                .filter(p -> p.getTitulo().equalsIgnoreCase(titulo))
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
            cartelera.removeIf(p -> p.getTitulo().equalsIgnoreCase(titulo));
        }
    }
}