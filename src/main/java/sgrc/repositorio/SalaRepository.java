package sgrc.repositorio;

import sgrc.modelo.Sala;
import java.util.Optional;
import java.util.List;

public interface SalaRepository {
	Optional<Sala> buscarPorId(int id);

	void guardar(Sala sala);
	List<Sala> obtenerSalasDisponibles();
}