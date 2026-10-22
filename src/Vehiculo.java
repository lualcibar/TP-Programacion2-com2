
/**
 * Clase Vehiculo
 * Mantiene atributos:
 * patente: identificador de cada vehiculo
 * marca: atributo caracteristico
 * modelo: atributo caracteritico
 * anio: tributo caracteristico
 * dniDueno: identificador del dueno del vehiculo
 */
public abstract class Vehiculo {
    private String patente; //hay que hacer que no pueda haber 2 iguales
    private String marca;
    private String modelo;
    private int anio;
    private String dniDuenio;//debe vincular con futura clase 'Entidades.Cliente'

    public Vehiculo(String patente, String marca, String modelo, int anio, String dniDuenio) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.dniDuenio = dniDuenio;
    }

    public String getPatente() {return patente;}
    public String getMarca() {return marca;}
    public String getModelo() {return modelo;}
    public int getAnio() {return anio;}
    public String getDniDuenio() {return dniDuenio;}

    //public void setPatente(String patente) {this.patente = patente;}
    public void setMarca(String marca) {this.marca = marca;}
    public void setModelo(String modelo) {this.modelo = modelo;}

    @Override
    public String toString() {
        return patente + " - " + marca + " - " + modelo + " (" + anio + ")";
    }


    /**
     * con estos metodos evitamos duplicados en las listas de vehiculos
     * @param obj   the reference object with which to compare.
     * //@return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vehiculo v)) return false;
        return patente.equalsIgnoreCase(v.patente);
    }

    @Override
    public int hashCode() {
        return patente.toUpperCase().hashCode();
    }



}
