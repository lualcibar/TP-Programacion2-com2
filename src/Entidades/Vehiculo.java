package Entidades;

import Exceptions.DatosInvalidosException;

import java.time.LocalDate;

/**
 * Clase Entidades.Vehiculo
 * Mantiene atributos:
 * patente: identificador de cada vehiculo
 * marca: atributo caracteristico
 * modelo: atributo caracteritico
 * anio: tributo caracteristico
 * dniDueno: identificador del dueno del vehiculo
 */
public class Vehiculo {
    private final String patente;
    private final String marca;
    private final String modelo;
    private final int anio;
    private final String dniDuenio;// vincula con clase 'Entidades.Cliente'
    private boolean activo;


    public Vehiculo(String patente, String marca, String modelo, int anio,
                    String dniDuenio) throws DatosInvalidosException {
        if(patente == null || patente.isBlank()){
            throw new DatosInvalidosException("Patente no puede ser nula o vacia");
        }
        if(marca == null || marca.isBlank()){
            throw new DatosInvalidosException("Marca no puede ser nula o vacia");
        }
        if(modelo == null  || modelo.isBlank()){
            throw new DatosInvalidosException("Modelo no puede ser nula o vacia");
        }
        if(anio < 1980 || anio > LocalDate.now().getYear()){
            throw new DatosInvalidosException("Anio ingresado no valido");
        }
        if(dniDuenio == null || dniDuenio.isBlank()){
            throw new DatosInvalidosException("DNI ingresado no valido");
        }
        // la patente siempre se guarda en mayusculas para que AB123CD y ab123cd sean la misma
        this.patente = patente.trim().toUpperCase();
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.dniDuenio = dniDuenio;
        this.activo = true;
    }

    public String getPatente() {return patente;}
    public String getMarca() {return marca;}
    public String getModelo() {return modelo;}
    public int getAnio() {return anio;}
    public String getDniDuenio() {return dniDuenio;}
    public boolean isActivo() {return activo;}
    public void setActivo(boolean activo) {this.activo = activo;}

    @Override
    public String toString() {
        return patente + " - " + marca + " - " + modelo + " (" + anio + ") - Duenio DNI: "
                + dniDuenio + (activo ? "" : " - DADO DE BAJA");
    }


    /**
     * Con estos metodos evitamos duplicados en las listas de vehiculos
     * @param obj es el objeto de referencia con el cual comparamos.
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
