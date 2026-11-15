package sgrc.vista;

import java.util.List;

import sgrc.modelo.Pelicula;
import sgrc.modelo.Sala;

public class ConsolaVista implements Vista {

	@Override
	public void mostrarMenu() {
		System.out.println("1. Ver cartelera");
		System.out.println("2. Añadir película");
		System.out.println("3. Eliminar película");

	}

	@Override
	public int leerOpcion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String leerTexto(String mensaje) {
		// TODO Auto-generated method stub
		return null;
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
