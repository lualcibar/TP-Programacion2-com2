package Entidades;


import java.util.ArrayList;
import java.util.List;

/**
 * Representa un cliente del taller.
 * Extiende Persona y mantiene una lista de patentes de vehiculos asociados.
 * Debera soportar baja logica (en proceso) mediante atributo activo.
 */

public class Cliente extends Persona {
    private List<String> vehiculos = new ArrayList<>();
    private boolean activo;

    public Cliente(String nombre, String dni, String telefono, String email) {
        super(nombre, dni, telefono, email);
        this.activo = true;
    }

    @Override
    public String toString () {
        return "Cliente: " + nombre + " - DNI: " + dni + " - Telefono: "
                + telefono + " - Email: " + email;
    }

    /**
     * Agrega un vehiculo a la lista de vehiculos del cliente,
     * pasamos patente por @param
     */
    public void agregarVehiculo (String patente) {
        vehiculos.add(patente);
    }
    /**
     * Elimina el vehiculo de la lista de vehiculos del cliente,
     * pasamos patente por @param
     */
    public void eliminarVehiculo (String patente) {vehiculos.remove(patente);}
    public List<String> getVehiculos() {return vehiculos;}
    //public void setVehiculos(List<String> vehiculos) {this.vehiculos = vehiculos;}
    public boolean isActivo() {return activo;}
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
