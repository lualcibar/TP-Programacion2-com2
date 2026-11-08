package Entidades;

import Enums.EstadoReparacion;
import Exceptions.DatosInvalidosException;
import Interfaces.Reparable;

import java.time.LocalDate;

/**
 * Representa la reparacion de un vehiculo en el taller.
 * Implementa la interfaz Reparable, que obliga a definir iniciarReparacion() y finalizarReparacion().
 * El estado evoluciona: PENDIENTE -> EN_CURSO -> FINALIZADA.
 * Las fechas se asignan automaticamente al iniciar y finalizar.
 * El id es autoincremental en la base de datos: hay dos constructores, uno para
 * crear una reparacion nueva (sin id) y otro para cuando se trae desde la base (con id).
 */
public class Reparacion implements Reparable {
    private int id;
    private final String patenteVehiculo;
    private final String dniMecanico;
    private final String descripcion;
    private final double costo;
    private EstadoReparacion estado; // PENDIENTE, EN_CURSO, FINALIZADA
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    /** Constructor para una reparacion nueva: el id lo genera la base de datos. */
    public Reparacion(String patenteVehiculo, String dniMecanico,
                      String descripcion, double costo) throws DatosInvalidosException {
        this(0, patenteVehiculo, dniMecanico, descripcion, costo);
    }

    /** Constructor que setea el id: se usa al traer los datos desde la base de datos. */
    public Reparacion(int id, String patenteVehiculo, String dniMecanico,
                      String descripcion, double costo) throws DatosInvalidosException {

        if (patenteVehiculo == null || patenteVehiculo.isBlank()) {
            throw new DatosInvalidosException("Patente vehiculo no valida");
        }
        if (dniMecanico == null || dniMecanico.isBlank()) {
            throw new DatosInvalidosException("DNI ingresado no valido");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new DatosInvalidosException("Descripcion no valida");
        }
        if (costo < 0) {
            throw new DatosInvalidosException("Costo negativo");
        }

        this.id = id;
        this.patenteVehiculo = patenteVehiculo;
        this.dniMecanico = dniMecanico;
        this.descripcion = descripcion;
        this.costo = costo;
        this.estado = EstadoReparacion.valueOf("PENDIENTE");
    }

    /**
     * Cambia el estado a EN_CURSO y registra la fecha de inicio.
     * Solo se puede iniciar una reparacion PENDIENTE.
     */
    @Override
    public void iniciarReparacion() {
        if (estado != EstadoReparacion.PENDIENTE) {
            throw new DatosInvalidosException("Solo se puede iniciar una reparacion PENDIENTE (estado actual: " + estado + ")");
        }
        this.estado = EstadoReparacion.EN_CURSO;
        this.fechaInicio = LocalDate.now();
    }

    /**
     * Cambia el estado a FINALIZADA y registra la fecha de fin.
     * Solo se puede finalizar una reparacion EN_CURSO.
     */
    @Override
    public void finalizarReparacion() {
        if (estado != EstadoReparacion.EN_CURSO) {
            throw new DatosInvalidosException("Solo se puede finalizar una reparacion EN_CURSO (estado actual: " + estado + ")");
        }
        this.estado = EstadoReparacion.FINALIZADA;
        this.fechaFin = LocalDate.now();
    }

    /**
     * @return true si la reparacion no esta en estado FINALIZADA.
     */
    public boolean estaActiva() { return estado != EstadoReparacion.FINALIZADA; }

    // getters y setters (el DAO usa los setters de estado y fechas al traer los datos)

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getPatenteVehiculo() {return patenteVehiculo;}

    public String getDniMecanico() {return dniMecanico;}

    public String getDescripcion() {return descripcion;}

    public double getCosto() {return costo;}

    public EstadoReparacion getEstado() {return estado;}

    public void setEstado(EstadoReparacion estado) {this.estado = estado;}

    public LocalDate getFechaInicio() {return fechaInicio;}

    public void setFechaInicio(LocalDate fechaInicio) {this.fechaInicio = fechaInicio;}

    public LocalDate getFechaFin() {return fechaFin;}

    public void setFechaFin(LocalDate fechaFin) {this.fechaFin = fechaFin;}

    @Override
    public String toString() {
        return "Reparacion #" + id + " - Patente: " + patenteVehiculo + " - Mecanico DNI: " + dniMecanico
                + " - " + descripcion + " - $" + costo + " - " + estado
                + (fechaInicio != null ? " - Inicio: " + fechaInicio : "")
                + (fechaFin != null ? " - Fin: " + fechaFin : "");
    }
}
