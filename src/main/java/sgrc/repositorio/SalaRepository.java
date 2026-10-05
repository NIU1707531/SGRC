package sgrc.repositorio;

import sgrc.modelo.Sala;
import java.util.Optional;

public interface SalaRepository {
	Optional<Sala> buscarPorId(int id);

	void guardar(Sala sala);
}