package Entidades;

import Exceptions.DatosInvalidosException;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un cliente del taller.
 * Extiende Persona y mantiene una lista de patentes de sus vehiculos ACTIVOS.
 * Soporta baja logica mediante el atributo activo (heredado de Persona).
 * La lista de patentes no se guarda en una columna de la base: se reconstruye
 * al cargar el sistema a partir de la tabla vehiculo (columna dni_duenio),
 * asi hay una sola fuente de verdad.
 */

public class Cliente extends Persona {
    private List<String> vehiculos = new ArrayList<>();

    public Cliente(String nombre, String dni, String telefono, String email, String password)
            throws DatosInvalidosException {
        super(nombre, dni, telefono, email, password);
    }

    @Override
    public String toString () {
        return "Cliente: " + nombre + " - DNI: " + dni + " - Telefono: "
                + telefono + " - Email: " + email + " - " + estadoTexto();
    }

    /**
     * Agrega un vehiculo a la lista de vehiculos del cliente (si todavia no estaba),
     * pasamos patente por @param
     */
    public void agregarVehiculo(String patente) {
        if (!vehiculos.contains(patente)) {
            vehiculos.add(patente);
        }
    }

    /**
     * Elimina el vehiculo de la lista de vehiculos del cliente,
     * pasamos patente por @param
     */
    public void eliminarVehiculo(String patente) {
        vehiculos.remove(patente);
    }

    /** Devuelve una copia para que la lista original no se pueda modificar desde afuera. */
    public List<String> getVehiculos() {
        return new ArrayList<>(vehiculos);
    }

}






