package sgrc.servicio;

import sgrc.modelo.Sala;

import sgrc.repositorio.SalaRepository;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class ReservaService {
    private final SalaRepository salaRepository;

    public ReservaService(SalaRepository salaRepository) {
        // Precondición
        if (salaRepository == null) {
            throw new IllegalArgumentException("El repositorio de salas no puede ser nulo");
        }
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

        Sala sala = salaRepository.buscarPorId(salaId)
                .orElseThrow(() -> new IllegalArgumentException("La sala no existe"));

        if (asientos == null || asientos.isEmpty()) {
            throw new IllegalArgumentException("Debes indicar al menos un asiento");
        }

        Set<String> posicionesVistas = new HashSet<>();

        for (int[] posicion : asientos) {

            if (posicion == null || posicion.length != 2) {
                throw new IllegalArgumentException("Coordenadas inválidas");
            }

            int fila = posicion[0];
            int columna = posicion[1];

            if (!posicionesVistas.add(fila + "," + columna)) {
                return false;
            }

            if (!sala.estaAsientoDisponible(fila, columna)) {
                return false;
            }
        }

        for (int[] posicion : asientos) {
            sala.reservarAsiento(posicion[0], posicion[1]);
        }

        salaRepository.guardar(sala);

        return true;
    }
    
}