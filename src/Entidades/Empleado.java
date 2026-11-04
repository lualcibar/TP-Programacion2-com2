package Entidades;
import Enums.Cargo;
import Exceptions.DatosInvalidosException;

/**
 * Representa un empleado del taller.
 * Extiende Persona y mantiene un cargo y salario.
 * No se una un 'activo' como en Cliente porque la baja es fisica.
 */

public class Empleado extends Persona {

    private Cargo cargo; // ADMIN, RECEP, MACAN
    private double salario;

    public Empleado(String nombre, String dni, String telefono,
                    String email, Cargo cargo, double salario) throws DatosInvalidosException {
        super(nombre, dni, telefono, email);
        if  (cargo == null) {// buscar como hacer la comprobacion de que usa datos del enum correcto
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
        return cargo + ": " + nombre + " - DNI: " + dni;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
}
