package sgrc.test.mocks;

import sgrc.modelo.Pelicula;
import sgrc.modelo.Sala;
import sgrc.vista.Vista;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * MOCK OBJECT MANUAL (sin Mockito) de la Vista: simula la interacción con el usuario.
 *
 * - Entradas: el test programa las opciones, enteros y textos que "teclearía" el usuario.
 * - Salidas: se registran los mensajes, errores y datos mostrados para verificarlos.
 */
public class VistaMock implements Vista {

    // Entradas programadas (se consumen en orden)
    private final Deque<Integer> opciones = new ArrayDeque<>();
    private final Deque<Integer> enteros = new ArrayDeque<>();
    private final Deque<String> textos = new ArrayDeque<>();

    // Registro de salidas
    private int vecesMenu = 0;
    private final List<String> mensajes = new ArrayList<>();
    private final List<String> errores = new ArrayList<>();
    private List<Pelicula> ultimaCartelera;
    private List<Sala> ultimasSalas;
    private Sala ultimaSala;

    // ---------- Programación de entradas ----------

    public void programarOpciones(Integer... valores) {
        opciones.addAll(List.of(valores));
    }

    public void programarEnteros(Integer... valores) {
        enteros.addAll(List.of(valores));
    }

    public void programarTextos(String... valores) {
        textos.addAll(List.of(valores));
    }

    // ---------- Implementación simulada ----------

    @Override
    public void mostrarMenu() {
        vecesMenu++;
    }

    @Override
    public int leerOpcion() {
        return opciones.removeFirst();
    }

    @Override
    public String leerTexto(String mensaje) {
        return textos.removeFirst();
    }

    @Override
    public int leerEntero(String mensaje) {
        return enteros.removeFirst();
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        mensajes.add(mensaje);
    }

    @Override
    public void mostrarError(String mensaje) {
        errores.add(mensaje);
    }

    @Override
    public void mostrarCartelera(List<Pelicula> peliculas) {
        ultimaCartelera = peliculas;
    }

    @Override
    public void mostrarSalas(List<Sala> salas) {
        ultimasSalas = salas;
    }

    @Override
    public void mostrarSala(Sala sala) {
        ultimaSala = sala;
    }

    // ---------- Verificación ----------

    public int getVecesMenu() {
        return vecesMenu;
    }

    public List<String> getMensajes() {
        return mensajes;
    }

    public List<String> getErrores() {
        return errores;
    }

    public List<Pelicula> getUltimaCartelera() {
        return ultimaCartelera;
    }

    public List<Sala> getUltimasSalas() {
        return ultimasSalas;
    }

    public Sala getUltimaSala() {
        return ultimaSala;
    }

    // true si el test ha consumido todas las entradas programadas
    public boolean entradasConsumidas() {
        return opciones.isEmpty() && enteros.isEmpty() && textos.isEmpty();
    }
}
