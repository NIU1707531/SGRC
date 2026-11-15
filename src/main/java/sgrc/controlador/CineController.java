package sgrc.controlador;

import sgrc.modelo.Pelicula;
import sgrc.servicio.CarteleraService;
import java.util.List;

public class CineController {
    private final CarteleraService carteleraService;

    public CineController(CarteleraService carteleraService) {
        this.carteleraService = carteleraService;
    }

    public List<Pelicula> mostrarCartelera() {
        return carteleraService.obtenerCartelera();
    }

    public void registrarPelicula(String titulo, int duracion) {
        Pelicula pelicula = new Pelicula(titulo, duracion);
        carteleraService.agregarPelicula(pelicula);
    }
}