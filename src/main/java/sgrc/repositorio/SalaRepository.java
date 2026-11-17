package sgrc.repositorio;

import sgrc.modelo.Sala;
import java.util.Optional;
import java.util.List;

/**
 * Repositorio de salas (capa de acceso a datos del Model).
 *
 * Es una interfaz para que ReservaService no dependa de cómo se guardan las salas:
 * en la aplicación se usa EnMemoriaSalaRepository y en los tests se sustituye por
 * un mock (SalaRepositoryMock o Mockito).
 */
public interface SalaRepository {
	// Busca una sala por su id (Optional vacío si no existe)
	Optional<Sala> buscarPorId(int id);

	// Guarda una sala; si ya existe una con el mismo id, la reemplaza
	void guardar(Sala sala);

	// Devuelve las salas que tienen al menos un asiento libre
	List<Sala> obtenerSalasDisponibles();

	// Devuelve todas las salas (también las llenas)
	List<Sala> obtenerTodas();
}
