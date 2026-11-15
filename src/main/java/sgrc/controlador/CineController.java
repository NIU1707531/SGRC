package sgrc.controlador;

import sgrc.modelo.Pelicula;
import sgrc.servicio.CarteleraService;
import sgrc.servicio.ReservaService;
import sgrc.vista.Vista;
import java.util.ArrayList;
import java.util.List;

public class CineController {
    public static final int OPCION_SALIR = 0;
    public static final int OPCION_VER_CARTELERA = 1;
    public static final int OPCION_ANADIR_PELICULA = 2;
    public static final int OPCION_ELIMINAR_PELICULA = 3;
    public static final int OPCION_VER_SALAS = 4;
    public static final int OPCION_VER_SALA = 5;
    public static final int OPCION_RESERVAR = 6;
    public static final int OPCION_RESERVAR_VARIOS = 7;
    public static final int OPCION_CANCELAR = 8;

    private final CarteleraService carteleraService;
    private final ReservaService reservaService;
    private final Vista vista;

    public CineController(CarteleraService carteleraService, ReservaService reservaService, Vista vista) {
        if (carteleraService == null || reservaService == null || vista == null) {
            throw new IllegalArgumentException("El controlador necesita los servicios y la vista");
        }
        this.carteleraService = carteleraService;
        this.reservaService = reservaService;
        this.vista = vista;
    }

    // Bucle principal: muestra el menú y procesa opciones hasta que el usuario sale
    public void iniciar() {
        boolean continuar = true;
        while (continuar) {
            vista.mostrarMenu();
            continuar = procesarOpcion(vista.leerOpcion());
        }
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
                case OPCION_VER_SALAS:
                    vista.mostrarSalas(reservaService.obtenerSalasDisponibles());
                    break;
                case OPCION_VER_SALA:
                    vista.mostrarSala(reservaService.obtenerSala(vista.leerEntero("Número de sala: ")));
                    break;
                case OPCION_RESERVAR:
                    reservarAsiento();
                    break;
                case OPCION_RESERVAR_VARIOS:
                    reservarVariosAsientos();
                    break;
                case OPCION_CANCELAR:
                    cancelarReserva();
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

    private void reservarAsiento() {
        int salaId = vista.leerEntero("Número de sala: ");
        int fila = vista.leerEntero("Fila: ") - 1;
        int columna = vista.leerEntero("Asiento: ") - 1;
        if (reservaService.realizarReserva(salaId, fila, columna)) {
            vista.mostrarMensaje("Reserva realizada");
        } else {
            vista.mostrarError("El asiento ya está ocupado");
        }
    }

    private void cancelarReserva() {
        int salaId = vista.leerEntero("Número de sala: ");
        int fila = vista.leerEntero("Fila: ") - 1;
        int columna = vista.leerEntero("Asiento: ") - 1;
        if (reservaService.cancelarReserva(salaId, fila, columna)) {
            vista.mostrarMensaje("Reserva cancelada");
        } else {
            vista.mostrarError("El asiento no estaba reservado");
        }
    }

    private void reservarVariosAsientos() {
        int salaId = vista.leerEntero("Número de sala: ");
        int cantidad = vista.leerEntero("¿Cuántos asientos quieres reservar? ");
        List<int[]> asientos = new ArrayList<>();
        // Bucle simple: se leen 'cantidad' asientos
        for (int i = 0; i < cantidad; i++) {
            int fila = vista.leerEntero("Fila del asiento " + (i + 1) + ": ") - 1;
            int columna = vista.leerEntero("Asiento " + (i + 1) + ": ") - 1;
            asientos.add(new int[]{fila, columna});
        }
        if (reservaService.realizarReservaMultiple(salaId, asientos)) {
            vista.mostrarMensaje("Reserva de " + cantidad + " asientos realizada");
        } else {
            vista.mostrarError("Algún asiento está ocupado o repetido: no se ha reservado ninguno");
        }
    }
}
