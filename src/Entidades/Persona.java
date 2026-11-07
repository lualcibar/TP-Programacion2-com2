package Entidades;

import Exceptions.DatosInvalidosException;

/**
 * Clase padre abstracta de la que extienden Cliente y Empleado.
 * Mantiene atributos: nombre, dni, telefono, email, password y activo
 * El atributo 'activo' implementa la BAJA LOGICA: cuando una persona se "da de baja"
 *  no se borra de la base de datos, solo se marca activo = false (asi se conserva su historial).
 *  Encapsulamiento: todos los datos se validan en el constructor y no hay setters
 *  que permitan saltear esas validaciones. Para "modificar" una persona, el SistemaTaller
 *  crea un objeto nuevo (el constructor valida ttodo) y lo reemplaza.
 */
public abstract class Persona {
    protected String nombre;
    protected String dni;
    protected String telefono;
    protected String email;
    protected String password;
    protected boolean activo;

    public Persona (String nombre, String dni, String telefono, String email, String password)
            throws DatosInvalidosException {
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre ingresado es invalido o esta vacio");
        }
        // el dni es la clave primaria en la base de datos: exactamente 8 digitos
        if (dni == null || dni.isBlank() || !dni.matches("[0-9]{8}")) {
            throw new DatosInvalidosException("El dni ingresado es invalido o esta vacio (deben ser 8 digitos)");
        }
        if  (telefono == null || telefono.isBlank()) {
            throw new DatosInvalidosException("El telefono ingresado es invalido o esta vacio");
        }
        if (email == null || email.isBlank()) {
            throw new DatosInvalidosException("El email ingresado es invalido o esta vacio");
        }
        if (password == null || password.isBlank()) {
            throw new DatosInvalidosException("La contrasenia ingresada es invalida o esta vacia");
        }

        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.activo = true; // toda persona nueva nace activa
    }

    /** Cada hijo (Cliente, Empleado) lo implementa a su manera: polimorfismo. */
    @Override
    public abstract String toString();

    /**setter y getter*/
    public String getNombre() {return nombre;}

    public String getDni() {return dni;}

    public String getTelefono() {return telefono;}

    public String getEmail() {return email;}

    public String getPassword() {return password;}

    public boolean isActivo() {return activo;}

    /** Lo usa el DAO al cargar desde la base y el sistema al dar de baja. */
    public void setActivo(boolean activo) {this.activo = activo;}

    /** Texto auxiliar para mostrar el estado en los listados. */
    protected String estadoTexto() {
        return activo ? "ACTIVO" : "DADO DE BAJA";
    }
}














