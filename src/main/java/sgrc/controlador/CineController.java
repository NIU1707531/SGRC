package sgrc.controlador;

import sgrc.modelo.Pelicula;
import sgrc.servicio.CarteleraService;
import sgrc.servicio.ReservaService;
import sgrc.vista.Vista;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador del SGRC (parte "Controller" del MVC).
 *
 * Hace de puente entre la Vista y el Model: pide los datos al usuario a través de
 * la interfaz {@link Vista}, llama a los servicios (CarteleraService y ReservaService)
 * y le dice a la Vista qué tiene que mostrar. No pinta nada por sí mismo ni contiene
 * reglas de negocio (esas están en el Model).
 *
 * Recibe sus dependencias por el constructor (inyección de dependencias), lo que
 * permite sustituirlas por mocks en los tests.
 */
public class CineController {
    // Números de las opciones del menú (deben coincidir con los que muestra la Vista)
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

    /** Pre: ninguna dependencia puede ser null (IllegalArgumentException). */
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

    // Devuelve las películas de la cartelera (delegando en el servicio del Model)
    public List<Pelicula> mostrarCartelera() {
        return carteleraService.obtenerCartelera();
    }

    // Crea la película y la añade a la cartelera. Si los datos no son válidos,
    // el constructor de Pelicula o el servicio lanzan la excepción correspondiente.
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
        // Cualquier opción distinta de "Salir" mantiene el bucle principal en marcha
        return true;
    }

    // Opción 2: pide título y duración y registra la película
    private void anadirPelicula() {
        String titulo = vista.leerTexto("Título de la película: ");
        int duracion = vista.leerEntero("Duración en minutos: ");
        registrarPelicula(titulo, duracion);
        vista.mostrarMensaje("Película añadida a la cartelera");
    }

    // Opción 3: elimina una película por su título; si no existe se avisa con un error
    private void eliminarPelicula() {
        String titulo = vista.leerTexto("Título de la película a eliminar: ");
        if (carteleraService.eliminarPelicula(titulo)) {
            vista.mostrarMensaje("Película eliminada");
        } else {
            vista.mostrarError("No existe ninguna película con ese título");
        }
    }

    // Opción 6: reserva un asiento concreto de una sala
    private void reservarAsiento() {
        int salaId = vista.leerEntero("Número de sala: ");
        // El usuario numera filas y asientos desde 1; el Model trabaja con índices desde 0
        int fila = vista.leerEntero("Fila: ") - 1;
        int columna = vista.leerEntero("Asiento: ") - 1;
        if (reservaService.realizarReserva(salaId, fila, columna)) {
            vista.mostrarMensaje("Reserva realizada");
        } else {
            vista.mostrarError("El asiento ya está ocupado");
        }
    }

    // Opción 8: libera un asiento reservado (misma conversión de 1..n a 0..n-1)
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

    // Opción 7: reserva varios asientos a la vez. El servicio la hace de forma atómica:
    // o se reservan todos o, si alguno está ocupado o repetido, ninguno.
    private void reservarVariosAsientos() {
        int salaId = vista.leerEntero("Número de sala: ");
        int cantidad = vista.leerEntero("¿Cuántos asientos quieres reservar? ");
        // Cada asiento se guarda como un par {fila, columna} ya convertido a índices desde 0
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
