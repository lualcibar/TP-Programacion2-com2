import java.util.ArrayList;
import java.util.List;

/**
 * representa un cliente del taller.
 * extiende de persona y mantiene una lista de patentes de vehiculos asociados
 * debera soportar baja logica(en proceso)*/

public class Cliente extends Persona{
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
     * agrega un vehiculo a la lista de vehiculos del cliente, pasamos patente por parametro
     */
    public void agregarVehiculo (String patente) {
        vehiculos.add(patente);
    }
    /**
     * elimina el vehiculo de la lista de vehiculos del cliente,
     * pasamos patente por param
     */
    public void eliminarVehiculo (String patente) {vehiculos.remove(patente);}
    public List<String> getVehiculos() {return vehiculos;}
    //public void setVehiculos(List<String> vehiculos) {this.vehiculos = vehiculos;}
    public boolean isActivo() {return activo;}





    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
