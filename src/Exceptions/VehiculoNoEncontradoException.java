package Exceptions;

/**
 * excepcion lanzada cada vez que se quiere acceder a un vehiculo que no existe
 */
public class VehiculoNoEncontradoException extends Exception {
    public VehiculoNoEncontradoException(String patente) { super("Vehículo no encontrado: " + patente); }
}
