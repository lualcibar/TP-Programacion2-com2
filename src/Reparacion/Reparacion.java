package Reparacion;

import java.time.LocalDate;
import Entidades.Empleado;
import Enums.Cargo;
import Enums.EstadoReparacion;
import Exceptions.DatosInvalidosException;

/**
 * Representa la reparación de un vehículo en el taller.
 * Implementa la interfaz Reparable, que obliga a definir iniciarReparacion() y finalizarReparacion().
 * El estado evoluciona: PENDIENTE → EN_CURSO → FINALIZADA.
 * Las fechas se asignan automáticamente al iniciar y finalizar.
 */
public class Reparacion implements Reparable {
    private int id; // estaria beuno hacer que el id sea autoincremental en la bdd
    //analizar si conviene pasar el dni y patente por param o
    //pasar un objeto directamente
    private final String patenteVehiculo;
    private final String dniMecanico;
    private final String descripcion;
    private final double costo;
    private EstadoReparacion estado; // PENDIENTE, EN_CURSO, FINALIZADA
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Reparacion(int id, String patenteVehiculo, String dniMecanico,
                      String descripcion, double costo) throws DatosInvalidosException {

        if (patenteVehiculo == null || patenteVehiculo.isBlank()) {
            throw new DatosInvalidosException("Patente vehiculo no valida");
        }
        if (dniMecanico == null || dniMecanico.isBlank()) {
            throw new DatosInvalidosException("DNI ingresado no valido");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new DatosInvalidosException("Descripcion no valido");
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
     */
    public void iniciarReparacion() {
        this.estado = EstadoReparacion.valueOf("EN_CURSO");
        this.fechaInicio = LocalDate.now();
    }

    /**
     * Cambia el estado a FINALIZADA y registra la fecha de fin.
     */
    public void finalizarReparacion() {
        this.estado = EstadoReparacion.valueOf("FINALIZADA");
        this.fechaFin = LocalDate.now();
    }

    /**
     * @return true si la reparación no está en estado FINALIZADA.
     */
    public boolean estaActiva() { return estado != EstadoReparacion.FINALIZADA; }

    // getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatenteVehiculo() {
        return patenteVehiculo;
    }

    //public void setPatenteVehiculo(String patenteVehiculo) {this.patenteVehiculo = patenteVehiculo;}

    public String getDniMecanico() {
        return dniMecanico;
    }

    //public void setDniMecanico(String dniMecanico) {this.dniMecanico = dniMecanico;}

    public String getDescripcion() {
        return descripcion;
    }

    //public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public double getCosto() {
        return costo;
    }

    //public void setCosto(double costo) {this.costo = costo;}

    public EstadoReparacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoReparacion estado) {
        this.estado = estado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}

