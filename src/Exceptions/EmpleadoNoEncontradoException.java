package Exceptions;

/**
 * excepcion lanzada cada vez que se quiere acceder a un empleado que no existe
 */
public class EmpleadoNoEncontradoException extends TallerException {
    public EmpleadoNoEncontradoException(String dni) {
        super("Empleado no encontrado: " + dni);
    }
}
