package Exceptions;

/**
 * excepcion lanzada cada vez que se quiere acceder a un cliente que no existe
 */
public class ClienteNoEncontradoException extends TallerException {
    public ClienteNoEncontradoException(String dni) {
        super("Cliente no encontrado: " + dni);
    }
}
