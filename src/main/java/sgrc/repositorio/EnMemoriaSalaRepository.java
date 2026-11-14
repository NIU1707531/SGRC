package sgrc.repositorio;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import sgrc.modelo.Sala;

public class EnMemoriaSalaRepository implements SalaRepository {
	private List<Sala> salas = new ArrayList<>();

	@Override
	public Optional<Sala> buscarPorId(int id) {
		for(Sala sala:salas ) {
		    if (sala.getId() == id) {
		       return Optional.of(sala);
		    }
	    }
		return Optional.empty();
		
	}

	@Override
	public void guardar(Sala sala) {
		if(sala==null) {
			throw new IllegalArgumentException("La sala no puede ser nula");
		}
		salas.removeIf(salaGuardada->salaGuardada.getId()==sala.getId());
		salas.add(sala);
	}
	
	@Override
	public List<Sala> obtenerSalasDisponibles() {
		List<Sala> dispo=new ArrayList<>();
		for (Sala sala : salas) {
		    // Comprobar si tiene asientos disponibles
			if(sala.tieneAsientosDisponibles()) {
				dispo.add(sala);
			}
		    
		}
		return dispo;
	}
	
	

}
