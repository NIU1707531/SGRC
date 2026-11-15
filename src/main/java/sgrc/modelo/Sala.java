package sgrc.modelo;

public class Sala {
    private int id;
    private int filas;
    private int columnas;
    private boolean[][] asientos;
    

    public Sala(int id, int filas, int columnas) {
    	//precondicion
    	if (id <= 0) {
    		throw new IllegalArgumentException("El id de la sala debe ser positivo");
    	}
    	if (filas<= 0 ||  columnas <= 0 ) {
    		throw new IllegalArgumentException("Invalido, minimo crear un asiento");
    	}
    	
        this.id = id;
        this.filas = filas;
        this.columnas = columnas;
        this.asientos = new boolean[filas][columnas];
    }

    public int getId() {
        return id;
    }

    public int getCapacidadTotal() {
        return filas * columnas;
    }

    public boolean estaAsientoDisponible(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            throw new IllegalArgumentException("Asiento fuera de los límites de la sala");
        }
        return !asientos[fila][columna];
    }

    public boolean reservarAsiento(int fila, int columna) {
        if (!estaAsientoDisponible(fila, columna)) {
            return false;
        }
        asientos[fila][columna] = true;
        return true;
    }
    
    public boolean liberarAsiento(int fila, int columna)
    {
    	if(!estaAsientoDisponible(fila,columna))
    	{
    		asientos[fila][columna]=false;
    		return true;
    	}
    	return false;
    }
    
    public boolean tieneAsientosDisponibles() {
    	for (int fila = 0; fila < filas; fila++) {
    	    for (int columna = 0; columna < columnas; columna++) {
    	       if(!asientos[fila][columna]) {
    	    	   return true;
    	       }
    	    }
    	}
    	return false;
    }
}