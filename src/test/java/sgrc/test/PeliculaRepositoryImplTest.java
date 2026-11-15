package sgrc.test;

import sgrc.modelo.Pelicula;
import sgrc.repositorio.PeliculaRepositoryImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de caja negra de PeliculaRepositoryImpl (clase de bajo nivel).
 */
public class PeliculaRepositoryImplTest {

    private PeliculaRepositoryImpl repositorio;

    @BeforeEach
    public void setUp() {
        repositorio = new PeliculaRepositoryImpl();
    }

    // ===================== guardar =====================
    // Particiones: película null (inválida) | película nueva | película con título ya existente

    @Test
    @DisplayName("guardar: una película nueva aparece en el listado")
    public void testGuardarPeliculaNueva() {
        Pelicula pelicula = new Pelicula("Inception", 148);
        repositorio.guardar(pelicula);
        assertEquals(List.of(pelicula), repositorio.obtenerTodas());
    }

    @Test
    @DisplayName("guardar: null (partición inválida) lanza excepción")
    public void testGuardarNula() {
        assertThrows(IllegalArgumentException.class, () -> repositorio.guardar(null));
    }

    @Test
    @DisplayName("guardar: mismo título con otras mayúsculas reemplaza la película")
    public void testGuardarReemplazaMismoTitulo() {
        repositorio.guardar(new Pelicula("Inception", 148));
        Pelicula nueva = new Pelicula("INCEPTION", 150);
        repositorio.guardar(nueva);

        assertEquals(1, repositorio.obtenerTodas().size());
        assertSame(nueva, repositorio.obtenerTodas().get(0));
    }

    // ===================== buscarPorTitulo =====================
    // Particiones: título null | título en blanco | título existente | título inexistente

    @Test
    @DisplayName("buscarPorTitulo: título existente (sin distinguir mayúsculas)")
    public void testBuscarPorTituloExistente() {
        Pelicula pelicula = new Pelicula("Inception", 148);
        repositorio.guardar(pelicula);
        assertSame(pelicula, repositorio.buscarPorTitulo("inception").get());
    }

    @Test
    @DisplayName("buscarPorTitulo: título inexistente devuelve Optional vacío")
    public void testBuscarPorTituloInexistente() {
        repositorio.guardar(new Pelicula("Inception", 148));
        assertFalse(repositorio.buscarPorTitulo("Matrix").isPresent());
    }

    @Test
    @DisplayName("buscarPorTitulo: null y en blanco (particiones inválidas) devuelven vacío")
    public void testBuscarPorTituloNuloOEnBlanco() {
        repositorio.guardar(new Pelicula("Inception", 148));
        assertEquals(Optional.empty(), repositorio.buscarPorTitulo(null));
        assertEquals(Optional.empty(), repositorio.buscarPorTitulo(""));
        assertEquals(Optional.empty(), repositorio.buscarPorTitulo("   "));
    }

    // ===================== obtenerTodas =====================
    // Valores límite del tamaño de la lista: 0, 1 y varios elementos

    @Test
    @DisplayName("obtenerTodas: repositorio vacío (límite 0) devuelve lista vacía")
    public void testObtenerTodasVacio() {
        assertTrue(repositorio.obtenerTodas().isEmpty());
    }

    @Test
    @DisplayName("obtenerTodas: con varias películas las devuelve todas en orden de inserción")
    public void testObtenerTodasVarias() {
        Pelicula p1 = new Pelicula("Inception", 148);
        Pelicula p2 = new Pelicula("Matrix", 136);
        repositorio.guardar(p1);
        repositorio.guardar(p2);
        assertEquals(List.of(p1, p2), repositorio.obtenerTodas());
    }

    @Test
    @DisplayName("obtenerTodas: devuelve una copia (modificarla no afecta al repositorio)")
    public void testObtenerTodasCopiaDefensiva() {
        repositorio.guardar(new Pelicula("Inception", 148));
        repositorio.obtenerTodas().clear();
        assertEquals(1, repositorio.obtenerTodas().size());
    }

    // ===================== eliminar =====================
    // Particiones: título existente | inexistente | null

    @Test
    @DisplayName("eliminar: título existente borra la película")
    public void testEliminarExistente() {
        repositorio.guardar(new Pelicula("Inception", 148));
        repositorio.eliminar("INCEPTION");
        assertTrue(repositorio.obtenerTodas().isEmpty());
    }

    @Test
    @DisplayName("eliminar: título inexistente no modifica el repositorio")
    public void testEliminarInexistente() {
        repositorio.guardar(new Pelicula("Inception", 148));
        repositorio.eliminar("Matrix");
        assertEquals(1, repositorio.obtenerTodas().size());
    }

    @Test
    @DisplayName("eliminar: null (partición inválida) no lanza excepción ni borra nada")
    public void testEliminarNulo() {
        repositorio.guardar(new Pelicula("Inception", 148));
        repositorio.eliminar(null);
        assertEquals(1, repositorio.obtenerTodas().size());
    }
}
