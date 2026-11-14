package sgrc.test;
import sgrc.modelo.Sala;
import sgrc.repositorio.EnMemoriaSalaRepository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

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
    
    @Test
    public void testGuardarSalaNula() {
        // Crear un repositorio vacío

   	 EnMemoriaSalaRepository repositorio = new EnMemoriaSalaRepository();
 
        //lanza IllegalArgumentException
   	assertThrows(IllegalArgumentException.class, () -> {
   	    repositorio.guardar(null);
   	});
    }
    
    @Test
    public void testGuardarVariasSalas(){
        EnMemoriaSalaRepository repositorio = new EnMemoriaSalaRepository();

        Sala sala1 = new Sala(1, 2, 2);
        Sala sala2 = new Sala(2, 5, 5);
        repositorio.guardar(sala1);
        repositorio.guardar(sala2);
        Optional<Sala> resultado1 = repositorio.buscarPorId(1);
        Optional<Sala> resultado2 = repositorio.buscarPorId(2);
        assertTrue(resultado1.isPresent());
        assertTrue(resultado2.isPresent());


        assertSame(sala1, resultado1.get());
        assertSame(sala2, resultado2.get());
    }
    
    @Test
    public void testObtenerSalasDisponibles() {
        EnMemoriaSalaRepository repositorio = new EnMemoriaSalaRepository();

        Sala sala1 = new Sala(1, 1, 1);
        Sala sala2 = new Sala(2, 2, 2);
        Sala sala3 = new Sala(3, 1, 2);
        repositorio.guardar(sala1);
        repositorio.guardar(sala2);
        repositorio.guardar(sala3);
        sala1.reservarAsiento(0, 0); // Sala 1: completamente ocupada
        sala3.reservarAsiento(0, 0); // Sala 3: queda un asiento libre
        List<Sala> resultado = repositorio.obtenerSalasDisponibles();
        assertFalse(resultado.contains(sala1));
        
        assertEquals(2, resultado.size());

        assertTrue(resultado.contains(sala2));

        assertTrue(resultado.contains(sala3));
        
    }
    
    

}
