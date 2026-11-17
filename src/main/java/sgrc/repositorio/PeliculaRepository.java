package sgrc.repositorio;

import sgrc.modelo.Pelicula;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de películas (capa de acceso a datos del Model).
 *
 * Es una interfaz para que CarteleraService no dependa de cómo se guardan los datos:
 * en la aplicación se usa PeliculaRepositoryImpl (en memoria) y en los tests se
 * sustituye por un mock (PeliculaRepositoryMock o Mockito).
 */
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