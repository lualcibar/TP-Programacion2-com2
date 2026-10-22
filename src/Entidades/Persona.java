package Entidades;

/**SERA ABSTRACT YA QUE DE ESTA HEREDARAN ATRIBUTOS CLIENTE Y EMPLEADO
 * NO SE CREA OBJETO*/
public abstract class Persona {
    protected String nombre;
    protected String dni;
    protected String telefono;
    protected String email;

    public Persona (String nombre, String dni, String telefono, String email) {
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
