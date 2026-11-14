package sgrc.servicio;

import sgrc.modelo.Sala;

import sgrc.repositorio.SalaRepository;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

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

        // Buscar
        Sala sala = salaRepository.buscarPorId(salaId)
                .orElseThrow(() -> new IllegalArgumentException("La sala no existe"));

        //Comprobar la lista
        if (asientos == null || asientos.isEmpty()) {
            throw new IllegalArgumentException("Debes indicar al menos un asiento");
        }

        // Comprobar que TODOS los asientos estén disponibles
        for (int[] posicion : asientos) {

            if (posicion == null || posicion.length != 2) {
                throw new IllegalArgumentException("Coordenadas inválidas");
            }

            int fila = posicion[0];
            int columna = posicion[1];

            if (!sala.estaAsientoDisponible(fila, columna)) {
                return false;
            }
        }

        // Reservar los asientos
        for (int[] posicion : asientos) {
            sala.reservarAsiento(posicion[0], posicion[1]);
        }

        // Guardar la sala actualizada
        salaRepository.guardar(sala);

        return true;
    }
    
}