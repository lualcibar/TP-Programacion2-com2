package Exceptions;

/**
 * excepcion lanzada cada vez que se quiere acceder a un cliente que no esta activo o no existe
 */
public class ClienteNoActivoException extends Exception {
    public ClienteNoActivoException(String dni) { super("Cliente no activo o no encontrado: " + dni); }
}
