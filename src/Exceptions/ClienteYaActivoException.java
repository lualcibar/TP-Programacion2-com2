package Exceptions;

/**
 * excepcion lanzada cada vez que se quiere dar de alta con un cliente que ya esta activo
 */
public class ClienteYaActivoException extends TallerException {
    public ClienteYaActivoException(String dni) {
        super("Cliente ya se encuentra activo: " + dni);
    }
}