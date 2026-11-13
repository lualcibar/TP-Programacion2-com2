package Exceptions;

/**
 * excepcion lanzada cuando falla el inicio de sesion (DNI o contrasenia incorrectos,
 * o usuario sin acceso al sistema)
 */
public class CredencialesInvalidasException extends TallerException {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
