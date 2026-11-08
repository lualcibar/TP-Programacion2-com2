package Entidades;

import Exceptions.DatosInvalidosException;

import java.time.LocalDate;

/**
 * Clase Vehiculo
 * Mantiene atributos:
 * patente: identificador de cada vehiculo (clave primaria en la base de datos)
 * marca: atributo caracteristico
 * modelo: atributo caracteristico
 * anio: atributo caracteristico
 * dniDuenio: identificador del duenio del vehiculo (vincula con 'Entidades.Cliente')
 * activo: baja logica (se conserva el historial de reparaciones y turnos del vehiculo)
 */
public class Vehiculo {
    private final String patente;
    private final String marca;
    private final String modelo;
    private final int anio;
    private final String dniDuenio;
    private boolean activo;

    public Vehiculo(String patente, String marca, String modelo, int anio,
                    String dniDuenio) throws DatosInvalidosException {
        if (patente == null || patente.isBlank()) {
            throw new DatosInvalidosException("Patente no puede ser nula o vacia");
        }
        if (marca == null || marca.isBlank()) {
            throw new DatosInvalidosException("Marca no puede ser nula o vacia");
        }
        if (modelo == null || modelo.isBlank()) {
            throw new DatosInvalidosException("Modelo no puede ser nulo o vacio");
        }
        if (anio < 1980 || anio > LocalDate.now().getYear()) {
            throw new DatosInvalidosException("Anio ingresado no valido");
        }
        if (dniDuenio == null || dniDuenio.isBlank()) {
            throw new DatosInvalidosException("El dni del duenio no puede ser nulo o vacio");
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
     * Con estos metodos evitamos duplicados en el Set de vehiculos:
     * dos vehiculos son el mismo si tienen la misma patente.
     * @param obj es el objeto de referencia con el cual comparamos.
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
