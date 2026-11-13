package Menu;

import Entidades.Cliente;
import Entidades.Empleado;
import Entidades.Turno;
import Exceptions.DatosInvalidosException;
import Exceptions.TallerException;
import Servicio.SistemaTaller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Menu del CLIENTE (segundo tipo de acceso al sistema).
 * El cliente ya esta logueado, por eso nunca se le pide el DNI: todas las operaciones
 * usan el DNI del cliente que inicio sesion, y asi solo puede ver y tocar lo suyo.
 * A traves de este menu puede ver sus datos y vehiculos, pedir y cancelar turnos y ver sus reparaciones.
 */
public final class MenuCliente {

    private MenuCliente() {
    }

    public static void mostrar(SistemaTaller taller, Cliente cliente) {
        String dni = cliente.getDni();
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n===== MENU CLIENTE (" + cliente.getNombre() + ") =====");
            System.out.println("1. Ver mis datos");
            System.out.println("2. Ver mis vehiculos");
            System.out.println("3. Pedir turno");
            System.out.println("4. Ver mis turnos");
            System.out.println("5. Cancelar un turno");
            System.out.println("6. Ver mis reparaciones");
            System.out.println("0. Cerrar sesion");
            opcion = Consola.entero("Opcion: ");

            try {
                switch (opcion) {
                    case 1:
                        System.out.println(taller.buscarCliente(dni));
                        break;
                    case 2:
                        mostrarLista(taller.listarVehiculosPorCliente(dni), "No tenes vehiculos registrados.");
                        break;
                    case 3:
                        pedirTurno(taller, dni);
                        break;
                    case 4:
                        mostrarLista(taller.listarTurnosPorCliente(dni), "No tenes turnos registrados.");
                        break;
                    case 5: {
                        int id = Consola.entero("ID del turno a cancelar: ");
                        Consola.resultado(taller.cancelarTurnoDeCliente(dni, id),
                                "Turno cancelado.", "No se pudo cancelar el turno.");
                        break;
                    }
                    case 6:
                        mostrarLista(taller.listarReparacionesPorCliente(dni), "No tenes reparaciones registradas.");
                        break;
                    case 0:
                        System.out.println("Cerrando sesion...");
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                }
            } catch (TallerException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (DatosInvalidosException e) {
                System.out.println("Datos invalidos: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        }
    }

    /**
     * Pide los datos del turno. Se muestran los mecanicos disponibles (solo nombre y DNI,
     * nunca el salario) para que el cliente pueda elegir uno.
     */
    private static void pedirTurno(SistemaTaller taller, String dniCliente) throws SQLException, TallerException {
        System.out.println("Mecanicos disponibles:");
        for (Empleado mecanico : taller.listarMecanicosActivos()) {
            System.out.println(" - " + mecanico.getNombre() + " - DNI: " + mecanico.getDni());
        }
        String patente = Consola.texto("Patente de tu vehiculo: ");
        String dniMecanico = Consola.texto("DNI del mecanico: ");
        LocalDate fecha = Consola.fecha("Fecha (AAAA-MM-DD): ");

        Turno nuevo = new Turno(patente, dniMecanico, fecha);
        Consola.resultado(taller.agregarTurnoDeCliente(dniCliente, nuevo), "Turno agregado: " + nuevo,
                "No se pudo agregar el turno.");
    }

    private static void mostrarLista(List<?> lista, String mensajeVacio) {
        if (lista.isEmpty()) {
            System.out.println(mensajeVacio);
            return;
        }
        for (Object o : lista) {
            System.out.println(" - " + o);
        }
    }
}
