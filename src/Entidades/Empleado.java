package Entidades;

import Enums.Cargo;
import Exceptions.DatosInvalidosException;

/**
 * Representa un empleado del taller.
 * Extiende Persona y mantiene un cargo y salario.
 * Tiene baja logica (activo, heredado de Persona) porque las reparaciones y
 * los turnos lo referencian por DNI: si se borrara fisicamente, esos registros
 * quedarian huerfanos.
 */

public class Empleado extends Persona {

    private Cargo cargo; // ADMIN, RECEP, MACAN
    private double salario;

    public Empleado(String nombre, String dni, String telefono, String email,
                    String password, Cargo cargo, double salario) throws DatosInvalidosException {
        super(nombre, dni, telefono, email,  password);
        if  (cargo == null) {
            throw new DatosInvalidosException("El cargo ingresado es invalido");
        }
        if(salario < 0) {
            throw new DatosInvalidosException("El salario ingresado es invalido");
        }
        this.cargo = cargo;
        this.salario = salario;
    }

    @Override
    public String toString() {
        return cargo + ": " + nombre + " - DNI: " + dni + " - Salario: $" + salario
                + " - " + estadoTexto();
    }

    public Cargo getCargo() {
        return cargo;
    }

    public double getSalario() {
        return salario;
    }
}
