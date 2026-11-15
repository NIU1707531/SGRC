package sgrc.vista;

import sgrc.modelo.Pelicula;
import sgrc.modelo.Sala;

import java.util.List;

/**
 * Interfaz de la Vista (MVC). El Controlador solo conoce esta interfaz, de modo que:
 *  - ConsolaVista (desarrollador/a B) la implementa con E/S por consola.
 *  - En los tests se sustituye por un mock (VistaMock manual o Mockito).
 *
 * Las filas y columnas que el usuario introduce se numeran desde 1;
 * el Controlador las convierte a índices desde 0 para el Model.
 */
public interface Vista {

    // Muestra el menú de opciones
    void mostrarMenu();

    // Lee la opción elegida por el usuario
    int leerOpcion();

    // Muestra el mensaje y lee un texto
    String leerTexto(String mensaje);

    // Muestra el mensaje y lee un número entero
    int leerEntero(String mensaje);

    // Muestra un mensaje informativo
    void mostrarMensaje(String mensaje);

    // Muestra un mensaje de error
    void mostrarError(String mensaje);

    // Muestra la lista de películas de la cartelera
    void mostrarCartelera(List<Pelicula> peliculas);

    // Muestra una lista de salas (id, capacidad, asientos libres)
    void mostrarSalas(List<Sala> salas);

    // Muestra el mapa de asientos de una sala
    void mostrarSala(Sala sala);
}
