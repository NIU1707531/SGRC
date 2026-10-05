package sgrc.modelo;

public class Pelicula {
    private String titulo;
    private int duracionMinutos;

    public Pelicula(String titulo, int duracionMinutos) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio");
        }
        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException("La duracion debe ser mayor a 0");
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