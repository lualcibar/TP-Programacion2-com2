package Exceptions;

/**
 * excepcion lanzada cada vez que se quiere acceder a una reparacion que no existe
 */
public class ReparacionNoEncontradaException extends TallerException {
    public ReparacionNoEncontradaException(int id) {
        super("Reparación no encontrada: " + id);
    }
}
