package Exceptions;

/**
 * excepcion lanzada cada vez que se quiere operar con un cliente que no esta activo
 * (dado de baja logica) o que no existe
 */
public class ClienteNoActivoException extends TallerException {
    public ClienteNoActivoException(String dni) {
        super("Cliente no activo o no encontrado: " + dni);
    }
}
