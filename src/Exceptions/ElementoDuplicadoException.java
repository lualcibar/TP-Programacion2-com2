package Exceptions;

/**
 * excepcion lanzada cuando se intenta dar de alta algo que ya existe
 * (mismo DNI de cliente/empleado o misma patente de vehiculo)
 */
public class ElementoDuplicadoException extends TallerException {
    public ElementoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
