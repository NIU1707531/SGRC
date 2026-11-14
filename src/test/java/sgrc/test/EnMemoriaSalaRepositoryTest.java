package sgrc.test;
import sgrc.modelo.Sala;
import sgrc.repositorio.EnMemoriaSalaRepository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class EnMemoriaSalaRepositoryTest {
	
    @Test
    public void testGuardarYBuscarPorId() {

        // ARRANGE: preparar el repositorio y una sala
        EnMemoriaSalaRepository repositorio = new EnMemoriaSalaRepository();
        Sala sala = new Sala(1, 2, 2);

        // ACT: guardar la sala y buscarla por su ID
        repositorio.guardar(sala);
        Optional<Sala> resultado = repositorio.buscarPorId(1);

        // ASSERT: comprobar que encontramos la sala correcta
        assertTrue(resultado.isPresent());
        assertSame(sala, resultado.get());
    }
    
    @Test
    public void testBuscarSalaInexistente() {
    	 EnMemoriaSalaRepository repositorio = new EnMemoriaSalaRepository();
         Sala sala = new Sala(1, 2, 2);

         repositorio.guardar(sala);
    	 Optional<Sala> resultado = repositorio.buscarPorId(99);
    	 
    	 assertFalse(resultado.isPresent());
    	 
    }
    
    @Test
    public void testGuardarSalaConIdDuplicado() {
    	 EnMemoriaSalaRepository repositorio = new EnMemoriaSalaRepository();
         Sala sala1 = new Sala(1, 2, 2);
         Sala sala2 = new Sala(1, 5, 5);
         repositorio.guardar(sala1);
         repositorio.guardar(sala2);
         Optional<Sala> resultado = repositorio.buscarPorId(1);
         assertSame(sala2, resultado.get());
    }
    
    
    

}
