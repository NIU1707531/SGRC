package sgrc;

import sgrc.controlador.CineController;
import sgrc.modelo.Pelicula;
import sgrc.modelo.Sala;
import sgrc.repositorio.EnMemoriaSalaRepository;
import sgrc.repositorio.PeliculaRepository;
import sgrc.repositorio.PeliculaRepositoryImpl;
import sgrc.repositorio.SalaRepository;
import sgrc.servicio.CarteleraService;
import sgrc.servicio.ReservaService;
import sgrc.vista.ConsolaVista;

/**
 * Punto de entrada del SGRC. Solo hace el "cableado" del MVC:
 * crea los repositorios y servicios (Model), la Vista por consola y el Controlador,
 * carga unos datos de ejemplo y arranca el bucle principal.
 */
public class Main {

    public static void main(String[] args) {
        // Model
        PeliculaRepository repositorioPeliculas = new PeliculaRepositoryImpl();
        SalaRepository repositorioSalas = new EnMemoriaSalaRepository();
        CarteleraService carteleraService = new CarteleraService(repositorioPeliculas);
        ReservaService reservaService = new ReservaService(repositorioSalas);

        cargarDatosDeEjemplo(carteleraService, repositorioSalas);

        // Vista y Controlador
        ConsolaVista vista = new ConsolaVista();
        CineController controller = new CineController(carteleraService, reservaService, vista);
        controller.iniciar();
        vista.mostrarDespedida();
    }

    // Datos iniciales para que la aplicación no arranque vacía
    private static void cargarDatosDeEjemplo(CarteleraService cartelera, SalaRepository salas) {
        cartelera.agregarPelicula(new Pelicula("Dune: Parte Dos", 166));
        cartelera.agregarPelicula(new Pelicula("Del revés 2", 96));
        cartelera.agregarPelicula(new Pelicula("La sociedad de la nieve", 144));

        salas.guardar(new Sala(1, 3, 4));
        salas.guardar(new Sala(2, 5, 8));
        salas.guardar(new Sala(3, 8, 12));
    }
}
