package Exceptions;

/**
 * excepcion lanzada cada vez que se quiere acceder a un turno que no existe
 */
public class TurnoNoEncontradoException extends TallerException {
    public TurnoNoEncontradoException(int id) {
        super("Turno no encontrado: " + id);
    }
}
