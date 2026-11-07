package Entidades;

import Enums.EstadoTurno;
import Exceptions.DatosInvalidosException;

import java.time.LocalDate;


public class Turno {
    private int id;
    private final String patenteVehiculo;
    private final String dniMecanico;
    private final LocalDate fecha;
    private EstadoTurno estado;

    /** Constructor para un turno nuevo: nace PENDIENTE y el id lo genera la base de datos. */
    public Turno(String patenteVehiculo, String dniMecanico, LocalDate fecha)
            throws DatosInvalidosException {
        this(0, patenteVehiculo, dniMecanico, fecha, EstadoTurno.PENDIENTE);
    }

    /** Constructor que setea el id: se usa al traer los datos desde la base de datos. */
    public Turno(int id, String patenteVehiculo, String dniMecanico, LocalDate fecha,
                 EstadoTurno estado) throws DatosInvalidosException {
        if (patenteVehiculo == null || patenteVehiculo.isBlank()) {
            throw new DatosInvalidosException("Patente vehiculo no valida");
        }
        if (dniMecanico == null || dniMecanico.isBlank()) {
            throw new DatosInvalidosException("DNI del mecanico no valido");
        }
        if (fecha == null) {
            throw new DatosInvalidosException("La fecha del turno no puede ser nula");
        }
        if (estado == null) {
            throw new DatosInvalidosException("El estado del turno no puede ser nulo");
        }
        this.id = id;
        this.patenteVehiculo = patenteVehiculo.trim().toUpperCase();
        this.dniMecanico = dniMecanico;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getPatenteVehiculo() {return patenteVehiculo;}

    public String getDniMecanico() {return dniMecanico;}

    public LocalDate getFecha() {return fecha;}

    public EstadoTurno getEstado() {return estado;}

    public void setEstado(EstadoTurno estado) {this.estado = estado;}

    @Override
    public String toString() {
        return "Turno #" + id + " - Patente: " + patenteVehiculo + " - Mecanico DNI: " + dniMecanico
                + " - Fecha: " + fecha + " - " + estado;
    }
}

