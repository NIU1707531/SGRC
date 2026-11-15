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

@ExtendWith(MockitoExtension.class)
public class CarteleraServiceTest {

    @Mock
    private PeliculaRepository peliculaRepository; // Simulamos el repositorio

    @InjectMocks
    private CarteleraService carteleraService; // Inyectamos el mock en el servicio

    @Test
    public void testObtenerTodasLasPelculas() {
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
}