package Enums;

/**
 * enum para asignar el estado de cada reparacion.
 * El estado evoluciona: PENDIENTE -> EN_CURSO -> FINALIZADA.
 * (se corrigio el error de tipeo PENDINTE: Reparacion usa PENDIENTE al crearse)
 */
public enum EstadoReparacion {
    PENDIENTE, EN_CURSO, FINALIZADA
}
