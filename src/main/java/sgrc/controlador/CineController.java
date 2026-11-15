package sgrc.controlador;

import sgrc.modelo.Pelicula;
import sgrc.servicio.CarteleraService;
import sgrc.servicio.ReservaService;
import sgrc.vista.Vista;
import java.util.List;

public class CineController {
    public static final int OPCION_SALIR = 0;
    public static final int OPCION_VER_CARTELERA = 1;
    public static final int OPCION_ANADIR_PELICULA = 2;
    public static final int OPCION_ELIMINAR_PELICULA = 3;

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
    // Los errores del Model se muestran en la Vista sin detener la aplicación.
    public boolean procesarOpcion(int opcion) {
        try {
            switch (opcion) {
                case OPCION_SALIR:
                    return false;
                case OPCION_VER_CARTELERA:
                    vista.mostrarCartelera(mostrarCartelera());
                    break;
                case OPCION_ANADIR_PELICULA:
                    anadirPelicula();
                    break;
                case OPCION_ELIMINAR_PELICULA:
                    eliminarPelicula();
                    break;
                default:
                    vista.mostrarError("Opción no válida");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            vista.mostrarError(e.getMessage());
        }
        return true;
    }

    private void anadirPelicula() {
        String titulo = vista.leerTexto("Título de la película: ");
        int duracion = vista.leerEntero("Duración en minutos: ");
        registrarPelicula(titulo, duracion);
        vista.mostrarMensaje("Película añadida a la cartelera");
    }

    private void eliminarPelicula() {
        String titulo = vista.leerTexto("Título de la película a eliminar: ");
        if (carteleraService.eliminarPelicula(titulo)) {
            vista.mostrarMensaje("Película eliminada");
        } else {
            vista.mostrarError("No existe ninguna película con ese título");
        }
    }
}
