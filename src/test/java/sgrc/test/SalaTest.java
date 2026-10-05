package sgrc.test;

import sgrc.modelo.Sala;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class SalaTest {

    @Test
    @DisplayName("Debe calcular correctamente la capacidad total de la sala")
    public void testCapacidadTotal() {
        // Arrange
        Sala sala = new Sala(1, 10, 12);

        // Act
        int capacidad = sala.getCapacidadTotal();

        // Assert
        assertEquals(120, capacidad, "La capacidad debe ser filas x columnas");
    }

    @Test
    @DisplayName("Debe permitir reservar un asiento disponible")
    public void testReservarAsientoExitoso() {
        // Arrange
        Sala sala = new Sala(1, 5, 5);

        // Act & Assert
        assertTrue(sala.estaAsientoDisponible(2, 2));
        boolean reservado = sala.reservarAsiento(2, 2);

        assertTrue(reservado, "La reserva debe ser exitosa");
        assertFalse(sala.estaAsientoDisponible(2, 2), "El asiento ya no debe estar disponible");
    }

    @Test
    @DisplayName("Debe lanzar excepci�n al intentar acceder a un asiento fuera de limites")
    public void testAsientoFueraDeLimites() {
        // Arrange
        Sala sala = new Sala(1, 5, 5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sala.estaAsientoDisponible(10, 10);
        });
    }
}