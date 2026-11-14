package sgrc.servicio;

import sgrc.modelo.Sala;
import java.util.List;
import sgrc.repositorio.SalaRepository;

public class ReservaService {
    private final SalaRepository salaRepository;

    public ReservaService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    public boolean realizarReserva(int salaId, int fila, int columna) {
        Sala sala = salaRepository.buscarPorId(salaId)
                .orElseThrow(() -> new IllegalArgumentException("La sala no existe"));

        boolean exito = sala.reservarAsiento(fila, columna);
        if (exito) {
            salaRepository.guardar(sala);
        }
        return exito;
    }
    public boolean realizarReservaMultiple(int salaId, List<int[]> asientos) {
        return true;
    }
    
}