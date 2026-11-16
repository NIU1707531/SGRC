package sgrc.vista;

import java.util.List;
import java.util.Scanner;

import sgrc.modelo.Pelicula;
import sgrc.modelo.Sala;

public class ConsolaVista implements Vista {
	private final Scanner scanner = new Scanner(System.in);

	@Override
	public void mostrarMenu() {
		System.out.println("1. Ver cartelera");
		System.out.println("2. Añadir película");
		System.out.println("3. Eliminar película");
		System.out.println("4. Ver salas disponibles");
		System.out.println("5. Ver asientos de una sala");
		System.out.println("6. Reservar asiento");
		System.out.println("7. Reservar varios asientos");
		System.out.println("8. Cancelar reserva");
		System.out.println("0. Salir");
	}

	@Override
	public int leerOpcion() {
		return Integer.parseInt(scanner.nextLine().trim());
	}

	@Override
	public String leerTexto(String mensaje) {
	    System.out.print(mensaje);
	    return scanner.nextLine();
	}

	@Override
	public int leerEntero(String mensaje) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void mostrarMensaje(String mensaje) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mostrarError(String mensaje) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mostrarCartelera(List<Pelicula> peliculas) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mostrarSalas(List<Sala> salas) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mostrarSala(Sala sala) {
		// TODO Auto-generated method stub

	}

}
