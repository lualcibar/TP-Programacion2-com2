package Entidades;
import Enums.Cargo;

/**
 * representa un empleado del taller.
 * extiende de persona y mantiene un cargo y salario
 * no se una un 'activo' como en Cliete porque la baja es fisica
 */

public class Empleado extends Persona {

    private Cargo cargo;//ADMIN, RECEP, MACAN
    private double salario;

    public Empleado(String nombre, String dni, String telefono,
                    String email, Cargo cargo, double salario) {
        super(nombre, dni, telefono, email);
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
