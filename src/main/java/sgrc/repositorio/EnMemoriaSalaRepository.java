package sgrc.repositorio;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import sgrc.modelo.Sala;

/**
 * Repositorio de salas en memoria (BD ficticia).
 *
 * PROGRAMACIÓN POR CONTRATO
 *  - Invariante: la lista de salas no es null y no hay dos salas con el mismo id.
 *  - guardar: Pre sala != null (IllegalArgumentException).
 *             Post (assert) la sala se puede recuperar por su id y sigue sin haber ids repetidos.
 *  - obtenerSalasDisponibles: Post (assert) todas las salas devueltas tienen asientos libres.
 */
public class EnMemoriaSalaRepository implements SalaRepository {
	private final List<Sala> salas = new ArrayList<>();

	@Override
	public Optional<Sala> buscarPorId(int id) {
		// Bucle simple: recorre las salas hasta encontrar el id
		for (Sala sala : salas) {
			if (sala.getId() == id) {
				return Optional.of(sala);
			}
		}
		return Optional.empty();
	}

	@Override
	public void guardar(Sala sala) {
		// Precondición
		if (sala == null) {
			throw new IllegalArgumentException("La sala no puede ser nula");
		}
		salas.removeIf(salaGuardada -> salaGuardada.getId() == sala.getId());
		salas.add(sala);

		// Postcondiciones
		assert buscarPorId(sala.getId()).orElse(null) == sala : "La sala guardada debe poder recuperarse";
		assert invariante() : "Invariante de EnMemoriaSalaRepository violado";
	}

	@Override
	public List<Sala> obtenerSalasDisponibles() {
		List<Sala> dispo = new ArrayList<>();
		// Bucle simple: filtra las salas con algún asiento libre
		for (Sala sala : salas) {
			if (sala.tieneAsientosDisponibles()) {
				dispo.add(sala);
			}
		}
		// Postcondición
		assert dispo.stream().allMatch(Sala::tieneAsientosDisponibles);
		return dispo;
	}

	@Override
	public List<Sala> obtenerTodas() {
		return new ArrayList<>(salas);
	}

	// Invariante: no hay ids repetidos (solo se evalúa dentro de los assert)
	private boolean invariante() {
		return salas.stream().map(Sala::getId).distinct().count() == salas.size();
	}
}
