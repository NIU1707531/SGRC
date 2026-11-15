package sgrc.test.mocks;

import sgrc.modelo.Sala;
import sgrc.repositorio.SalaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MOCK OBJECT MANUAL (sin Mockito) de SalaRepository.
 *
 * - Devuelve las respuestas que el test le configura (stubbing manual).
 * - Registra las llamadas recibidas para poder verificarlas después
 *   (equivalente manual a verify() de Mockito).
 */
public class SalaRepositoryMock implements SalaRepository {

    // Respuestas configuradas por el test
    private Sala salaADevolver;
    private List<Sala> salasADevolver = new ArrayList<>();

    // Registro de llamadas
    private final List<Integer> idsBuscados = new ArrayList<>();
    private final List<Sala> salasGuardadas = new ArrayList<>();

    // ---------- Configuración (stubbing) ----------

    // Sala que devolverá buscarPorId (null = la sala no existe)
    public void setSalaADevolver(Sala sala) {
        this.salaADevolver = sala;
    }

    // Lista que devolverán obtenerTodas y obtenerSalasDisponibles
    public void setSalasADevolver(List<Sala> salas) {
        this.salasADevolver = salas;
    }

    // ---------- Implementación simulada ----------

    @Override
    public Optional<Sala> buscarPorId(int id) {
        idsBuscados.add(id);
        if (salaADevolver != null && salaADevolver.getId() == id) {
            return Optional.of(salaADevolver);
        }
        return Optional.empty();
    }

    @Override
    public void guardar(Sala sala) {
        salasGuardadas.add(sala);
    }

    @Override
    public List<Sala> obtenerSalasDisponibles() {
        return salasADevolver;
    }

    @Override
    public List<Sala> obtenerTodas() {
        return salasADevolver;
    }

    // ---------- Verificación ----------

    public int getVecesGuardar() {
        return salasGuardadas.size();
    }

    public List<Sala> getSalasGuardadas() {
        return salasGuardadas;
    }

    public List<Integer> getIdsBuscados() {
        return idsBuscados;
    }
}
