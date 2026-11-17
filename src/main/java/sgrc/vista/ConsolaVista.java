package sgrc.vista;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import sgrc.modelo.Pelicula;
import sgrc.modelo.Sala;

/**
 * Vista por consola del SGRC (parte "View" del MVC).
 *
 * Solo se encarga de pintar datos y leer lo que escribe el usuario: no contiene
 * lógica de negocio. El Controlador la usa a través de la interfaz {@link Vista}.
 *
 * Los marcos usan caracteres Unicode de dibujo de cajas, que se ven bien en la consola
 * de Eclipse con la codificación UTF-8 del proyecto. Los colores ANSI son opcionales
 * (consolas antiguas los muestran como texto basura): se activan arrancando con
 * {@code -Dsgrc.colores=true}.
 */
public class ConsolaVista implements Vista {

    // Ancho interior de los marcos del cartel y del menú
    private static final int ANCHO = 52;
    // Ancho máximo de la columna "Título" en la tabla de la cartelera
    private static final int ANCHO_TITULO = 34;
    // Número de bloques de la barra de ocupación de una sala
    private static final int ANCHO_BARRA = 10;

    // Líneas en blanco para "limpiar" la consola de Eclipse, que no admite borrado de pantalla
    private static final String LINEAS_LIMPIEZA = "\n".repeat(50);

    private static final String ASIENTO_LIBRE = "○";
    private static final String ASIENTO_OCUPADO = "●";

    // Códigos de color ANSI (solo se usan si los colores están activados)
    private static final String RESET = "\u001B[0m";
    private static final String NEGRITA = "\u001B[1m";
    private static final String ROJO = "\u001B[31m";
    private static final String VERDE = "\u001B[32m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String CIAN = "\u001B[36m";

    // Un único Scanner para toda la aplicación (crear varios sobre System.in da problemas)
    private final Scanner scanner = new Scanner(System.in);
    // true solo si se arranca con -Dsgrc.colores=true
    private final boolean colores = Boolean.getBoolean("sgrc.colores");
    // El cartel de bienvenida se muestra solo la primera vez que aparece el menú
    private boolean bienvenidaMostrada = false;

    // ------------------------------------------------------------------ Menú

    @Override
    public void mostrarMenu() {
        PrintStream out = System.out;
        if (!bienvenidaMostrada) {
            mostrarBienvenida();
            bienvenidaMostrada = true;
        } else {
            // Se deja leer el resultado de la opción anterior y se limpia antes de volver al menú
            esperarEnter();
            limpiarPantalla();
        }
        out.println();
        out.println("┌" + repetir("─", ANCHO) + "┐");
        out.println("│" + color(NEGRITA, centrar("MENÚ PRINCIPAL", ANCHO)) + "│");
        out.println("├" + repetir("─", ANCHO) + "┤");
        lineaMenu(color(CIAN, "■ Cartelera"), "■ Cartelera".length());
        opcionMenu("1. Ver cartelera");
        opcionMenu("2. Añadir película");
        opcionMenu("3. Eliminar película");
        lineaMenu(color(CIAN, "■ Salas y reservas"), "■ Salas y reservas".length());
        opcionMenu("4. Ver salas disponibles");
        opcionMenu("5. Ver asientos de una sala");
        opcionMenu("6. Reservar asiento");
        opcionMenu("7. Reservar varios asientos");
        opcionMenu("8. Cancelar reserva");
        out.println("├" + repetir("─", ANCHO) + "┤");
        opcionMenu("0. Salir");
        out.println("└" + repetir("─", ANCHO) + "┘");
    }

    // Opción del menú, sangrada bajo el título de su grupo
    private void opcionMenu(String texto) {
        lineaMenu("   " + texto, texto.length() + 3);
    }

    // Línea del menú con marco. 'visible' es la longitud sin códigos de color.
    private void lineaMenu(String texto, int visible) {
        System.out.println("│  " + texto + repetir(" ", ANCHO - 2 - visible) + "│");
    }

    // Cartel con doble marco que se muestra al arrancar la aplicación
    private void mostrarBienvenida() {
        String doble = repetir("═", ANCHO);
        System.out.println("╔" + doble + "╗");
        System.out.println("║" + repetir(" ", ANCHO) + "║");
        System.out.println("║" + color(NEGRITA, centrar("S G R C", ANCHO)) + "║");
        System.out.println("║" + centrar("Gestión de Reservas de Cine", ANCHO) + "║");
        System.out.println("║" + repetir(" ", ANCHO) + "║");
        System.out.println("╚" + doble + "╝");
    }

    /**
     * Mensaje de cierre. No forma parte de la interfaz Vista porque el Controlador
     * no lo necesita: lo llama Main cuando termina el bucle principal.
     */
    public void mostrarDespedida() {
        System.out.println();
        System.out.println("╔" + repetir("═", ANCHO) + "╗");
        System.out.println("║" + centrar("¡Hasta pronto! Gracias por usar SGRC", ANCHO) + "║");
        System.out.println("╚" + repetir("═", ANCHO) + "╝");
    }

    // --------------------------------------------------------- Entrada de datos

    // Si se acaba la entrada (Ctrl+Z / Ctrl+D) se devuelve "Salir" para terminar sin errores
    // Tras elegir una opción se limpia la pantalla para que su resultado aparezca solo
    @Override
    public int leerOpcion() {
        Integer opcion = leerNumero("Elige una opción: ");
        limpiarPantalla();
        return opcion == null ? 0 : opcion;
    }

    // Pausa hasta que el usuario pulsa Enter (si no queda entrada, no espera)
    private void esperarEnter() {
        System.out.println();
        System.out.print("  " + color(AMARILLO, "Pulsa Enter para volver al menú..."));
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }

    /**
     * Limpia la pantalla.
     * - Terminal real (System.console() != null): "cls" en Windows o secuencia ANSI en Linux/macOS.
     * - Consola de Eclipse o tests (sin terminal): no se puede borrar desde el programa,
     *   así que se empuja el contenido anterior fuera de la vista con líneas en blanco.
     */
    private void limpiarPantalla() {
        System.out.flush();
        if (System.console() == null) {
            System.out.print(LINEAS_LIMPIEZA);
            return;
        }
        if (System.getProperty("os.name", "").startsWith("Windows")) {
            try {
                // inheritIO() hace que "cls" actúe sobre la misma consola que el programa
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (IOException e) {
                System.out.print(LINEAS_LIMPIEZA);
            } catch (InterruptedException e) {
                // Se restaura el estado de interrupción del hilo, como recomienda Java
                Thread.currentThread().interrupt();
            }
        } else {
            // Secuencias ANSI: ESC[H mueve el cursor arriba a la izquierda y ESC[2J borra la pantalla
            System.out.print("\u001B[H\u001B[2J");
            System.out.flush();
        }
    }

    // Lee una línea completa (los títulos pueden tener espacios).
    // Si no queda entrada se lanza IllegalStateException, que el Controlador muestra como error.
    @Override
    public String leerTexto(String mensaje) {
        System.out.print(prompt(mensaje));
        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("No hay más datos de entrada");
        }
        return scanner.nextLine();
    }

    // Igual que leerNumero, pero si se acaba la entrada lanza excepción en vez de devolver null
    @Override
    public int leerEntero(String mensaje) {
        Integer numero = leerNumero(mensaje);
        if (numero == null) {
            throw new IllegalStateException("No hay más datos de entrada");
        }
        return numero;
    }

    /**
     * Pide un número entero hasta que el usuario escribe uno válido.
     * Devuelve null si se acaba la entrada.
     */
    private Integer leerNumero(String mensaje) {
        while (true) {
            System.out.print(prompt(mensaje));
            if (!scanner.hasNextLine()) {
                return null;
            }
            // Se lee la línea entera (y no nextInt) para no dejar restos en el buffer
            String linea = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                // No es un número: se avisa y el bucle vuelve a pedirlo
                mostrarError("Introduce un número entero (has escrito \"" + linea + "\")");
            }
        }
    }

    // Formato común de todas las preguntas al usuario: "  » mensaje"
    private String prompt(String mensaje) {
        return "  " + color(AMARILLO, "»") + " " + mensaje;
    }

    // ----------------------------------------------------------------- Mensajes

    // Mensajes de confirmación (p. ej. "Reserva realizada")
    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println();
        System.out.println("  " + color(VERDE, "► " + mensaje));
    }

    // Los errores van a System.err: Eclipse los pinta en rojo
    @Override
    public void mostrarError(String mensaje) {
        // Se vacía System.out antes para que el error no salga desordenado respecto al resto
        System.out.flush();
        System.err.println("  " + color(ROJO, "[!] " + mensaje));
        System.err.flush();
    }

    // ---------------------------------------------------------------- Cartelera

    @Override
    public void mostrarCartelera(List<Pelicula> peliculas) {
        System.out.println();
        if (peliculas.isEmpty()) {
            System.out.println("  " + color(AMARILLO, "La cartelera está vacía."
                    + " Añade una película con la opción 2."));
            return;
        }
        String unidad = peliculas.size() == 1 ? " película" : " películas";
        System.out.println("  " + color(NEGRITA, "CARTELERA") + " · " + peliculas.size() + unidad);

        // Ancho de cada columna de la tabla: "#", "Título" y "Duración"
        int[] anchos = {5, ANCHO_TITULO + 2, 24};
        System.out.println(bordeTabla("┌", "┬", "┐", anchos));
        System.out.println(filaTabla(anchos, "#", "Título", "Duración"));
        System.out.println(bordeTabla("├", "┼", "┤", anchos));
        // Bucle simple: una fila por película
        for (int i = 0; i < peliculas.size(); i++) {
            Pelicula pelicula = peliculas.get(i);
            System.out.println(filaTabla(anchos, String.valueOf(i + 1),
                    recortar(pelicula.getTitulo(), ANCHO_TITULO), formatearDuracion(pelicula.getDuracionMinutos())));
        }
        System.out.println(bordeTabla("└", "┴", "┘", anchos));
    }

    // 195 -> "195 minutos (3h 15min)"; 45 -> "45 minutos"
    private static String formatearDuracion(int minutos) {
        String texto = minutos + " minutos";
        int horas = minutos / 60;
        int resto = minutos % 60;
        if (horas > 0) {
            texto += " (" + horas + "h" + (resto > 0 ? " " + resto + "min" : "") + ")";
        }
        return texto;
    }

    // -------------------------------------------------------------------- Salas

    @Override
    public void mostrarSalas(List<Sala> salas) {
        System.out.println();
        if (salas.isEmpty()) {
            System.out.println("  " + color(AMARILLO, "No hay salas con asientos disponibles."));
            return;
        }
        System.out.println("  " + color(NEGRITA, "SALAS CON ASIENTOS LIBRES") + " · " + salas.size()
                + (salas.size() == 1 ? " sala" : " salas"));

        // Ancho de cada columna: "Sala", "Tamaño", "Libres" y "Ocupación" (barra + porcentaje)
        int[] anchos = {10, 9, 11, ANCHO_BARRA + 8};
        System.out.println(bordeTabla("┌", "┬", "┐", anchos));
        System.out.println(filaTabla(anchos, "Sala", "Tamaño", "Libres", "Ocupación"));
        System.out.println(bordeTabla("├", "┼", "┤", anchos));
        // El repositorio no garantiza el orden: se muestran ordenadas por número de sala
        List<Sala> ordenadas = new ArrayList<>(salas);
        ordenadas.sort(Comparator.comparingInt(Sala::getId));
        for (Sala sala : ordenadas) {
            int capacidad = sala.getCapacidadTotal();
            int libres = sala.contarAsientosLibres();
            System.out.println(filaTabla(anchos, "Sala " + sala.getId(),
                    sala.getFilas() + " x " + sala.getColumnas(),
                    libres + " / " + capacidad,
                    barraOcupacion(capacidad - libres, capacidad)));
        }
        System.out.println(bordeTabla("└", "┴", "┘", anchos));
    }

    // Barra de 10 bloques con el porcentaje de asientos ocupados, p. ej. "███░░░░░░░  30%"
    private static String barraOcupacion(int ocupados, int capacidad) {
        int porcentaje = ocupados * 100 / capacidad;
        // Bloques llenos redondeados al más cercano (sumar capacidad / 2 antes de dividir)
        int llenos = (ocupados * ANCHO_BARRA + capacidad / 2) / capacidad;
        return repetir("█", llenos) + repetir("░", ANCHO_BARRA - llenos) + String.format(" %3d%%", porcentaje);
    }

    @Override
    public void mostrarSala(Sala sala) {
        int columnas = sala.getColumnas();
        // Margen del ancho de "  Fila nn │" para alinear la pantalla y los números con los asientos
        String margen = repetir(" ", 11);
        System.out.println();
        System.out.println("  " + color(NEGRITA, "Sala " + sala.getId()) + " · " + sala.getFilas()
                + " filas x " + columnas + " asientos");
        System.out.println();
        // Cada asiento ocupa 3 caracteres; la pantalla tiene un ancho mínimo para que se lea el texto
        System.out.println(margen + pantalla(Math.max(columnas * 3, 12)));

        // Números de asiento (columna) encima de cada asiento, numerados desde 1
        StringBuilder cabecera = new StringBuilder(margen);
        for (int columna = 1; columna <= columnas; columna++) {
            cabecera.append(String.format("%3d", columna));
        }
        System.out.println(cabecera);

        // Bucles anidados: recorre filas y, dentro de cada fila, sus asientos
        for (int fila = 0; fila < sala.getFilas(); fila++) {
            StringBuilder linea = new StringBuilder(String.format("  Fila %2d │", fila + 1));
            for (int columna = 0; columna < columnas; columna++) {
                boolean libre = sala.estaAsientoDisponible(fila, columna);
                linea.append("  ").append(libre ? color(VERDE, ASIENTO_LIBRE) : color(ROJO, ASIENTO_OCUPADO));
            }
            System.out.println(linea.append(" │"));
        }

        System.out.println();
        System.out.println("  " + color(VERDE, ASIENTO_LIBRE) + " libre   " + color(ROJO, ASIENTO_OCUPADO)
                + " ocupado   ·   " + sala.contarAsientosLibres() + " de " + sala.getCapacidadTotal()
                + " asientos libres");
    }

    // "══════ PANTALLA ══════" con el ancho indicado
    private static String pantalla(int ancho) {
        String texto = " PANTALLA ";
        int izquierda = Math.max(0, (ancho - texto.length()) / 2);
        int derecha = Math.max(0, ancho - texto.length() - izquierda);
        return repetir("═", izquierda) + texto + repetir("═", derecha);
    }

    // ---------------------------------------------------------- Utilidades

    // Borde horizontal de una tabla, p. ej. "┌─────┬─────┐"
    private static String bordeTabla(String izquierda, String cruce, String derecha, int[] anchos) {
        StringBuilder linea = new StringBuilder("  ").append(izquierda);
        for (int i = 0; i < anchos.length; i++) {
            linea.append(repetir("─", anchos[i])).append(i < anchos.length - 1 ? cruce : derecha);
        }
        return linea.toString();
    }

    // Fila de una tabla con cada celda rellenada hasta el ancho de su columna
    private static String filaTabla(int[] anchos, String... celdas) {
        StringBuilder linea = new StringBuilder("  │");
        for (int i = 0; i < anchos.length; i++) {
            String celda = " " + celdas[i];
            linea.append(celda).append(repetir(" ", anchos[i] - celda.length())).append("│");
        }
        return linea.toString();
    }

    // Corta el texto con "…" si no cabe en el ancho indicado
    private static String recortar(String texto, int ancho) {
        return texto.length() <= ancho ? texto : texto.substring(0, ancho - 1) + "…";
    }

    // Rellena con espacios a ambos lados para centrar el texto en el ancho indicado
    private static String centrar(String texto, int ancho) {
        int izquierda = (ancho - texto.length()) / 2;
        return repetir(" ", izquierda) + texto + repetir(" ", ancho - texto.length() - izquierda);
    }

    // Como String.repeat, pero devuelve "" si 'veces' es negativo (evita una excepción)
    private static String repetir(String texto, int veces) {
        return veces <= 0 ? "" : texto.repeat(veces);
    }

    // Aplica el color ANSI al texto solo si los colores están activados
    private String color(String codigo, String texto) {
        return colores ? codigo + texto + RESET : texto;
    }
}
