package sgrc.repositorio;

import sgrc.modelo.Pelicula;
import java.util.List;
import java.util.Optional;

public interface PeliculaRepository {
    
    // Guarda o actualiza una película en el sistema
    void guardar(Pelicula pelicula);
    
    // Busca una película por su título (retorna Optional para evitar nulos)
    Optional<Pelicula> buscarPorTitulo(String titulo);
    
    // Obtiene el listado completo de la cartelera
    List<Pelicula> obtenerTodas();
    
    // Elimina una película del repositorio por su título
    void eliminar(String titulo);
}