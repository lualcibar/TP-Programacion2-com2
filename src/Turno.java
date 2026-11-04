
import Enums.EstadoTurno;

import java.time.LocalDate;


public class Turno {
    private int id;
    private final String patenteVehiculo;
    private final String dniMecanico;
    private final LocalDate fecha;
    private EstadoTurno estado;

    // constructor, getters y setters

    public Turno(int id, String patenteVehiculo, String dniMecanico, LocalDate fecha, EstadoTurno estado) {
        this.id = id;
        this.patenteVehiculo = patenteVehiculo;
        this.dniMecanico = dniMecanico;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getPatenteVehiculo() {return patenteVehiculo;}

    //public void setPatenteVehiculo(String patenteVehiculo) {this.patenteVehiculo = patenteVehiculo;}

    public String getDniMecanico() {return dniMecanico;}

    //public void setDniMecanico(String dniMecanico) {this.dniMecanico = dniMecanico;}

    public LocalDate getFecha() {return fecha;}

    //public void setFecha(LocalDate fecha) {this.fecha = fecha;}

    public EstadoTurno getEstado() {return estado;}

    public void setEstado(EstadoTurno estado) {this.estado = estado;}

}

/**
 * Clase turno
 * mantiene atributos:
 * id: id del turno
 * patenteVehiculo: patente del vehiculo que le pertenece el turno
 * dniMecanico: sabemos que mecanico esta encargado de la reparacion
 * fecha: fecha del turno
 * esatdo: PENDIENTE, CANCELADO, ATENDIDO
 */