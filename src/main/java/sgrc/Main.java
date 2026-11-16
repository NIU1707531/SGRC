package sgrc;
import sgrc.repositorio.PeliculaRepository;
import sgrc.repositorio.PeliculaRepositoryImpl;
import sgrc.servicio.CarteleraService;
import sgrc.repositorio.SalaRepository;
import sgrc.repositorio.EnMemoriaSalaRepository;
import sgrc.servicio.ReservaService;
import sgrc.vista.Vista;
import sgrc.vista.ConsolaVista;
import sgrc.controlador.CineController;
import sgrc.modelo.Sala;
public class Main {
	public static void main(String[] args) {
		PeliculaRepository repositorioPeliculas = new PeliculaRepositoryImpl();
		CarteleraService carteleraService = new CarteleraService(repositorioPeliculas);
		SalaRepository repositorioSalas = new EnMemoriaSalaRepository();
		ReservaService reservaService = new ReservaService(repositorioSalas);
		Vista vista = new ConsolaVista();
		CineController controller = new CineController(carteleraService, reservaService, vista);
		repositorioSalas.guardar(new Sala(1, 3, 4));
		controller.iniciar();
		
	}

}
