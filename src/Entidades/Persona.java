package Entidades;

import Exceptions.DatosInvalidosException;

/**
 * Clase padre de la que extienden Cliente y Empleado.
 * Mantiene atributos: nombre, dni, telefono, email
 */
public abstract class Persona {
    protected String nombre;
    protected String dni;
    protected String telefono;
    protected String email;

    public Persona (String nombre, String dni, String telefono, String email) throws DatosInvalidosException {
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre ingresado es invalido o esta vacio");
        }
        if (dni == null || dni.isBlank() || dni.length() != 8) {
            throw new DatosInvalidosException("El dni ingresado es invalido o esta vacio");
        }
        if  (telefono == null || telefono.isBlank()) {
            throw new DatosInvalidosException("El telefono ingresado es invalido o esta vacio");
        }
        if (email == null || email.isBlank()) {
            throw new DatosInvalidosException("El email ingresado es invalido o esta vacio");
        }

        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
    }



    public abstract String toString();

    /**setter y getter*/
    //public void setNombre(String nombre) {this.nombre = nombre;}

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }
}
