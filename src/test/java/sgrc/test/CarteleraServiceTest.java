package sgrc.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sgrc.modelo.Pelicula;
import sgrc.repositorio.PeliculaRepository;
import sgrc.servicio.CarteleraService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests de CarteleraService (Model) con un MOCK OBJECT de Mockito de PeliculaRepository.
 */
@ExtendWith(MockitoExtension.class)
public class CarteleraServiceTest {

    @Mock
    private PeliculaRepository peliculaRepository; // Simulamos el repositorio

    @InjectMocks
    private CarteleraService carteleraService; // Inyectamos el mock en el servicio

    @Test
    public void testObtenerTodasLasPeliculas() {
        // Arrange: Preparamos el comportamiento del mock
        Pelicula p1 = new Pelicula("Inception", 148);
        when(peliculaRepository.obtenerTodas()).thenReturn(Arrays.asList(p1));

        // Act: Llamamos al servicio
        List<Pelicula> resultado = carteleraService.obtenerCartelera();

        // Assert: Comprobamos el resultado y que se llamó al repositorio
        assertEquals(1, resultado.size());
        assertEquals("Inception", resultado.get(0).getTitulo());
        verify(peliculaRepository, times(1)).obtenerTodas();
    }

    // ===================== agregarPelicula =====================
    // Particiones: película null (inválida) | película nueva (válida)

    @Test
    public void testAgregarPeliculaValida() {
        Pelicula pelicula = new Pelicula("Matrix", 136);

        carteleraService.agregarPelicula(pelicula);

        // El servicio debe delegar el guardado en el repositorio
        verify(peliculaRepository, times(1)).guardar(pelicula);
    }

    @Test
    public void testAgregarPeliculaNula() {
        assertThrows(IllegalArgumentException.class, () -> carteleraService.agregarPelicula(null));
        verify(peliculaRepository, never()).guardar(any());
    }
}
