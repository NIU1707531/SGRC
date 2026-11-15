package sgrc.repositorio;

import sgrc.modelo.Sala;
import java.util.Optional;
import java.util.List;

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
