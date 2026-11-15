package sgrc.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sgrc.controlador.CineController;
import sgrc.modelo.Pelicula;
import sgrc.servicio.CarteleraService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CineControllerTest {

    @Mock
    private CarteleraService carteleraService; // Mockeamos el servicio

    @InjectMocks
    private CineController cineController; // Inyectamos el controlador

    @Test
    public void testListarCarteleraControlador() {
        // Arrange
        Pelicula p1 = new Pelicula("Inception", 148);
        when(carteleraService.obtenerCartelera()).thenReturn(Arrays.asList(p1));

        // Act
        List<Pelicula> resultado = cineController.mostrarCartelera();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Inception", resultado.get(0).getTitulo());
        verify(carteleraService, times(1)).obtenerCartelera();
    }
}