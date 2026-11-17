package sgrc.modelo;

/**
 * Sala de cine con una matriz de asientos (true = ocupado, false = libre).
 *
 * PROGRAMACIÓN POR CONTRATO
 *  - Precondiciones: se comprueban siempre y lanzan IllegalArgumentException
 *    (forman parte de la API pública y se testean en SalaTest).
 *  - Postcondiciones e invariante: se comprueban con assert (activos con -ea,
 *    que Maven/Surefire activa por defecto en los tests).
 *  - Invariante de clase: id > 0, filas > 0, columnas > 0, la matriz tiene
 *    dimensiones filas x columnas y 0 <= asientos libres <= capacidad.
 */
public class Sala {
    private final int id;
    private final int filas;
    private final int columnas;
    // asientos[fila][columna]: true si está ocupado. Índices desde 0.
    private final boolean[][] asientos;

    /**
     * Pre: id > 0, filas > 0, columnas > 0.
     * Post: sala con capacidad filas x columnas y todos los asientos libres.
     */
    public Sala(int id, int filas, int columnas) {
        // Precondiciones
        if (id <= 0) {
            throw new IllegalArgumentException("El id de la sala debe ser positivo");
        }
        if (filas <= 0 || columnas <= 0) {
            throw new IllegalArgumentException("Invalido, minimo crear un asiento");
        }

        this.id = id;
        this.filas = filas;
        this.columnas = columnas;
        // Java inicializa los boolean a false, así que todos los asientos empiezan libres
        this.asientos = new boolean[filas][columnas];

        // Postcondición: todos los asientos libres
        assert contarAsientosLibres() == getCapacidadTotal() : "Una sala nueva debe estar vacía";
        assert invariante() : "Invariante de Sala violado";
    }

    public int getId() {
        return id;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    // Número total de asientos de la sala (libres + ocupados)
    public int getCapacidadTotal() {
        return filas * columnas;
    }

    /**
     * Pre: 0 <= fila < filas y 0 <= columna < columnas.
     * Post: devuelve true si el asiento está libre (no modifica el estado).
     */
    public boolean estaAsientoDisponible(int fila, int columna) {
        // Precondición: el asiento existe dentro de la sala
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            throw new IllegalArgumentException("Asiento fuera de los límites de la sala");
        }
        return !asientos[fila][columna];
    }

    /**
     * Pre: asiento dentro de los límites (la comprueba estaAsientoDisponible).
     * Post: el asiento queda ocupado. Devuelve true si estaba libre (hay un
     * libre menos) y false si ya estaba ocupado (el estado no cambia).
     */
    public boolean reservarAsiento(int fila, int columna) {
        // estaAsientoDisponible también valida los límites (lanza excepción si no existe)
        if (!estaAsientoDisponible(fila, columna)) {
            return false;
        }
        // Se guarda el número de libres para comprobar la postcondición después
        int libresAntes = contarAsientosLibres();
        asientos[fila][columna] = true;

        // Postcondiciones
        assert !estaAsientoDisponible(fila, columna) : "El asiento debe quedar ocupado";
        assert contarAsientosLibres() == libresAntes - 1 : "Debe haber un asiento libre menos";
        assert invariante() : "Invariante de Sala violado";
        return true;
    }

    /**
     * Pre: asiento dentro de los límites.
     * Post: el asiento queda libre. Devuelve true si estaba ocupado y false
     * si ya estaba libre (el estado no cambia).
     */
    public boolean liberarAsiento(int fila, int columna) {
        if (estaAsientoDisponible(fila, columna)) {
            return false;
        }
        asientos[fila][columna] = false;

        // Postcondiciones
        assert estaAsientoDisponible(fila, columna) : "El asiento debe quedar libre";
        assert invariante() : "Invariante de Sala violado";
        return true;
    }

    // Devuelve true en cuanto encuentra un asiento libre (bucle anidado con salida anticipada)
    public boolean tieneAsientosDisponibles() {
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                if (!asientos[fila][columna]) {
                    return true;
                }
            }
        }
        return false;
    }

    // Cuenta los asientos libres recorriendo la matriz (bucle anidado filas x columnas)
    public int contarAsientosLibres() {
        int libres = 0;
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                if (!asientos[fila][columna]) {
                    libres++;
                }
            }
        }
        // Postcondición: el número de libres está entre 0 y la capacidad
        assert libres >= 0 && libres <= getCapacidadTotal();
        return libres;
    }

    // Invariante de clase (solo se evalúa dentro de los assert)
    private boolean invariante() {
        return id > 0 && filas > 0 && columnas > 0
                && asientos.length == filas
                && asientos[0].length == columnas;
    }
}
