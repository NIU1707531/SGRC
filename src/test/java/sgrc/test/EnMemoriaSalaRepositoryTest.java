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
    
    

    // ============ CAJA BLANCA: LOOP TESTING del bucle simple de buscarPorId ============
    // El bucle recorre la lista de salas hasta encontrar el id.
    // Casos: 0 iteraciones (lista vacía), 1 iteración, 2 iteraciones,
    // n iteraciones (último elemento) y n iteraciones sin encontrarlo.

    @Test
    public void testBuscarPorIdBucleCeroIteraciones() {
        // Lista vacía: el bucle no se ejecuta nunca
        EnMemoriaSalaRepository repositorio = new EnMemoriaSalaRepository();
        assertFalse(repositorio.buscarPorId(1).isPresent());
    }

    @Test
    public void testBuscarPorIdBucleUnaIteracion() {
        // Se encuentra en el primer elemento
        EnMemoriaSalaRepository repositorio = repositorioConSalas(5);
        assertEquals(1, repositorio.buscarPorId(1).get().getId());
    }

    @Test
    public void testBuscarPorIdBucleDosIteraciones() {
        EnMemoriaSalaRepository repositorio = repositorioConSalas(5);
        assertEquals(2, repositorio.buscarPorId(2).get().getId());
    }

    @Test
    public void testBuscarPorIdBucleNIteraciones() {
        // Se encuentra en el último elemento (n iteraciones)
        EnMemoriaSalaRepository repositorio = repositorioConSalas(5);
        assertEquals(5, repositorio.buscarPorId(5).get().getId());
    }

    @Test
    public void testBuscarPorIdBucleCompletoSinEncontrar() {
        // Recorre las n salas y no la encuentra
        EnMemoriaSalaRepository repositorio = repositorioConSalas(5);
        assertFalse(repositorio.buscarPorId(6).isPresent());
    }

    // ============ LOOP TESTING del bucle simple de obtenerSalasDisponibles ============

    @Test
    public void testObtenerSalasDisponiblesBucleCeroIteraciones() {
        // Repositorio vacío: 0 iteraciones, lista vacía
        EnMemoriaSalaRepository repositorio = new EnMemoriaSalaRepository();
        assertTrue(repositorio.obtenerSalasDisponibles().isEmpty());
    }

    @Test
    public void testObtenerSalasDisponiblesTodasLlenas() {
        // n iteraciones y ninguna sala cumple la condición
        EnMemoriaSalaRepository repositorio = repositorioConSalas(3);
        for (int id = 1; id <= 3; id++) {
            repositorio.buscarPorId(id).get().reservarAsiento(0, 0);
        }
        assertTrue(repositorio.obtenerSalasDisponibles().isEmpty());
    }

    @Test
    public void testObtenerSalasDisponiblesUnaSala() {
        // 1 iteración y la sala cumple la condición
        EnMemoriaSalaRepository repositorio = repositorioConSalas(1);
        assertEquals(1, repositorio.obtenerSalasDisponibles().size());
    }

    // ===================== obtenerTodas (caja negra) =====================
    // Valores límite del número de salas: 0 y varias (incluidas salas llenas)

    @Test
    public void testObtenerTodasVacio() {
        assertTrue(new EnMemoriaSalaRepository().obtenerTodas().isEmpty());
    }

    @Test
    public void testObtenerTodasIncluyeSalasLlenas() {
        EnMemoriaSalaRepository repositorio = repositorioConSalas(2);
        repositorio.buscarPorId(1).get().reservarAsiento(0, 0); // sala 1 llena
        assertEquals(2, repositorio.obtenerTodas().size());
    }

    // Crea un repositorio con n salas 1x1 de ids 1..n
    private EnMemoriaSalaRepository repositorioConSalas(int n) {
        EnMemoriaSalaRepository repositorio = new EnMemoriaSalaRepository();
        for (int id = 1; id <= n; id++) {
            repositorio.guardar(new Sala(id, 1, 1));
        }
        return repositorio;
    }
}
