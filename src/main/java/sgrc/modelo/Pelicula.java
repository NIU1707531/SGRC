package sgrc.modelo;

/**
 * Película de la cartelera.
 *
 * PROGRAMACIÓN POR CONTRATO
 *  - Precondiciones (IllegalArgumentException): título no nulo ni en blanco
 *    y duración en [1, DURACION_MAXIMA].
 *  - Postcondición/invariante (assert): el título guardado no tiene espacios
 *    exteriores ni está vacío y la duración está dentro del rango.
 *  - La clase es inmutable, así que el invariante se mantiene siempre.
 */
public class Pelicula {
    // Duración máxima permitida en minutos (10 horas)
    public static final int DURACION_MAXIMA = 600;

    private final String titulo;
    private final int duracionMinutos;

    /**
     * Pre: titulo != null y no en blanco; 1 <= duracionMinutos <= DURACION_MAXIMA.
     * Post: getTitulo() es el título sin espacios exteriores.
     */
    public Pelicula(String titulo, int duracionMinutos) {
        // Precondiciones
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio");
        }
        if (duracionMinutos <= 0 || duracionMinutos > DURACION_MAXIMA) {
            throw new IllegalArgumentException("La duracion debe estar entre 1 y " + DURACION_MAXIMA + " minutos");
        }
        // Se guarda sin espacios al principio ni al final ("  Dune " -> "Dune")
        this.titulo = titulo.trim();
        this.duracionMinutos = duracionMinutos;

        // Postcondición / invariante
        assert invariante() : "Invariante de Pelicula violado";
    }

    public String getTitulo() {
        return titulo;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    // Invariante de clase (solo se evalúa dentro de los assert)
    private boolean invariante() {
        return !titulo.isEmpty() && titulo.equals(titulo.trim())
                && duracionMinutos >= 1 && duracionMinutos <= DURACION_MAXIMA;
    }
}
