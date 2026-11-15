package sgrc.modelo;

public class Pelicula {
    public static final int DURACION_MAXIMA = 600;

    private String titulo;
    private int duracionMinutos;

    public Pelicula(String titulo, int duracionMinutos) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio");
        }
        if (duracionMinutos <= 0 || duracionMinutos > DURACION_MAXIMA) {
            throw new IllegalArgumentException("La duracion debe estar entre 1 y " + DURACION_MAXIMA + " minutos");
        }
        this.titulo = titulo;
        this.duracionMinutos = duracionMinutos;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }
}