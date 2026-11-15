package sgrc.controlador;

import sgrc.modelo.Pelicula;
import sgrc.servicio.CarteleraService;
import sgrc.servicio.ReservaService;
import sgrc.vista.Vista;
import java.util.List;

public class CineController {
    public static final int OPCION_SALIR = 0;
    public static final int OPCION_VER_CARTELERA = 1;

    private final CarteleraService carteleraService;
    private final ReservaService reservaService;
    private final Vista vista;

    public CineController(CarteleraService carteleraService, ReservaService reservaService, Vista vista) {
        this.carteleraService = carteleraService;
        this.reservaService = reservaService;
        this.vista = vista;
    }

    public List<Pelicula> mostrarCartelera() {
        return carteleraService.obtenerCartelera();
    }

    public void registrarPelicula(String titulo, int duracion) {
        Pelicula pelicula = new Pelicula(titulo, duracion);
        carteleraService.agregarPelicula(pelicula);
    }

    // Ejecuta la opción elegida. Devuelve false si el usuario quiere salir.
    public boolean procesarOpcion(int opcion) {
        switch (opcion) {
            case OPCION_SALIR:
                return false;
            case OPCION_VER_CARTELERA:
                vista.mostrarCartelera(mostrarCartelera());
                break;
            default:
                vista.mostrarError("Opción no válida");
        }
        return true;
    }
}
