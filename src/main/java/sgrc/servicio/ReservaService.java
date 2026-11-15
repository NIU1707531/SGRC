package sgrc.servicio;

import sgrc.modelo.Sala;

import sgrc.repositorio.SalaRepository;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Servicio del Model que gestiona las reservas de asientos de las salas.
 *
 * PROGRAMACIÓN POR CONTRATO
 *  - Invariante: salaRepository != null (garantizado por el constructor y final).
 *  - Precondiciones (IllegalArgumentException): la sala existe, la lista de
 *    asientos no es null ni vacía y cada posición tiene 2 coordenadas válidas.
 *  - Postcondiciones (assert): tras una reserva con éxito los asientos quedan
 *    ocupados; tras una cancelación con éxito el asiento queda libre.
 */
public class ReservaService {
    private final SalaRepository salaRepository;

    /** Pre: salaRepository != null. */
    public ReservaService(SalaRepository salaRepository) {
        // Precondición
        if (salaRepository == null) {
            throw new IllegalArgumentException("El repositorio de salas no puede ser nulo");
        }
        this.salaRepository = salaRepository;
    }

    /**
     * Reserva un asiento. Devuelve true si se ha reservado y false si ya estaba ocupado.
     * Pre: la sala existe y el asiento está dentro de sus límites.
     * Post: si devuelve true, el asiento queda ocupado y la sala se guarda.
     */
    public boolean realizarReserva(int salaId, int fila, int columna) {
        Sala sala = obtenerSala(salaId);

        boolean exito = sala.reservarAsiento(fila, columna);
        if (exito) {
            salaRepository.guardar(sala);
        }
        // Postcondición
        assert !exito || !sala.estaAsientoDisponible(fila, columna);
        return exito;
    }

    /**
     * Reserva varios asientos de forma atómica: o se reservan todos o ninguno.
     * Devuelve false (sin reservar nada) si hay coordenadas repetidas o algún asiento ocupado.
     * Pre: la sala existe, asientos no es null ni vacía y cada posición es {fila, columna}.
     * Post: si devuelve true, todos los asientos indicados quedan ocupados.
     */
    public boolean realizarReservaMultiple(int salaId, List<int[]> asientos) {
        Sala sala = obtenerSala(salaId);

        // Precondición
        if (asientos == null || asientos.isEmpty()) {
            throw new IllegalArgumentException("Debes indicar al menos un asiento");
        }

        // 1er bucle: validar todas las posiciones antes de reservar ninguna
        Set<String> posicionesVistas = new HashSet<>();
        for (int[] posicion : asientos) {
            if (posicion == null || posicion.length != 2) {
                throw new IllegalArgumentException("Coordenadas inválidas");
            }

            int fila = posicion[0];
            int columna = posicion[1];

            if (!posicionesVistas.add(fila + "," + columna)) {
                return false; // coordenada repetida
            }
            if (!sala.estaAsientoDisponible(fila, columna)) {
                return false; // asiento ocupado
            }
        }

        // 2o bucle: todas son válidas, se reservan
        for (int[] posicion : asientos) {
            sala.reservarAsiento(posicion[0], posicion[1]);
        }
        salaRepository.guardar(sala);

        // Postcondición
        assert asientos.stream().noneMatch(p -> sala.estaAsientoDisponible(p[0], p[1]));
        return true;
    }

    /**
     * Cancela la reserva de un asiento. Devuelve false si el asiento ya estaba libre.
     * Pre: la sala existe y el asiento está dentro de sus límites.
     * Post: el asiento queda libre.
     */
    public boolean cancelarReserva(int salaId, int fila, int columna) {
        Sala sala = obtenerSala(salaId);

        boolean exito = sala.liberarAsiento(fila, columna);
        if (exito) {
            salaRepository.guardar(sala);
        }
        // Postcondición
        assert sala.estaAsientoDisponible(fila, columna);
        return exito;
    }

    /** Pre: la sala existe (si no, IllegalArgumentException). */
    public Sala obtenerSala(int salaId) {
        return salaRepository.buscarPorId(salaId)
                .orElseThrow(() -> new IllegalArgumentException("La sala no existe"));
    }

    public List<Sala> obtenerSalas() {
        return salaRepository.obtenerTodas();
    }

    public List<Sala> obtenerSalasDisponibles() {
        return salaRepository.obtenerSalasDisponibles();
    }
}
