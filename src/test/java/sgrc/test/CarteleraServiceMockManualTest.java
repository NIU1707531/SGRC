package sgrc.test;

import sgrc.modelo.Pelicula;
import sgrc.servicio.CarteleraService;
import sgrc.test.mocks.PeliculaRepositoryMock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de CarteleraService usando el MOCK MANUAL PeliculaRepositoryMock
 * (test del Model sin Mockito).
 */
public class CarteleraServiceMockManualTest {

    private PeliculaRepositoryMock repositorioMock;
    private CarteleraService carteleraService;

    // Antes de cada test: mock nuevo (sin películas ni llamadas registradas) y servicio que lo usa
    @BeforeEach
    public void setUp() {
        repositorioMock = new PeliculaRepositoryMock();
        carteleraService = new CarteleraService(repositorioMock);
    }

    @Test
    public void testObtenerCarteleraConsultaElRepositorio() {
        Pelicula pelicula = new Pelicula("Inception", 148);
        repositorioMock.precargar(pelicula);

        assertEquals(List.of(pelicula), carteleraService.obtenerCartelera());
        // El mock cuenta las llamadas: el servicio debe consultar el repositorio exactamente una vez
        assertEquals(1, repositorioMock.getVecesObtenerTodas());
    }

    @Test
    public void testAgregarPeliculaNuevaSeGuarda() {
        Pelicula pelicula = new Pelicula("Matrix", 136);

        carteleraService.agregarPelicula(pelicula);

        assertEquals(List.of(pelicula), repositorioMock.getGuardadas());
    }

    @Test
    public void testAgregarPeliculaDuplicadaNoSeGuarda() {
        repositorioMock.precargar(new Pelicula("Matrix", 136));

        // Mismo título con otras mayúsculas: se rechaza y no se llega a llamar a guardar()
        assertThrows(IllegalStateException.class,
                () -> carteleraService.agregarPelicula(new Pelicula("matrix", 120)));
        assertTrue(repositorioMock.getGuardadas().isEmpty());
    }

    @Test
    public void testEliminarPeliculaExistente() {
        repositorioMock.precargar(new Pelicula("Matrix", 136));

        assertTrue(carteleraService.eliminarPelicula("Matrix"));
        assertEquals(List.of("Matrix"), repositorioMock.getEliminadas());
    }

    @Test
    public void testEliminarPeliculaInexistenteNoLlamaAEliminar() {
        // Repositorio vacío: el servicio devuelve false sin pedir al repositorio que elimine
        assertFalse(carteleraService.eliminarPelicula("Matrix"));
        assertTrue(repositorioMock.getEliminadas().isEmpty());
    }
}
