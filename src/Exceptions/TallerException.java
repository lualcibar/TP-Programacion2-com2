package Exceptions;

/**
 * Excepcion base (checked) de todas las excepciones de negocio del sistema.
 * Todas las excepciones personalizadas "checked" heredan de esta clase,
 * de esta forma los menus pueden atrapar cualquier error del taller con un solo catch.
 * (DatosInvalidosException es la unica unchecked: se lanza desde los constructores)
 */
public class TallerException extends Exception {
    public TallerException(String mensaje) {
        super(mensaje);
    }
}
