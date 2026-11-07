package Enums;

/**
 * enum para asignar el estado de cada turno.
 * CANCELADO es la baja logica del turno: el registro se conserva en la base de datos.
 */
public enum EstadoTurno {
    ATENDIDO, PENDIENTE, CANCELADO
}
