package Menu;

import Entidades.*;
import Enums.Cargo;
import Exceptions.DatosInvalidosException;
import Exceptions.TallerException;
import Servicio.SistemaTaller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public final class MenuAdmin {

    private MenuAdmin() {
    }

    public static void mostrar(SistemaTaller taller, Empleado admin) {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n===== MENU ADMINISTRADOR (" + admin.getNombre() + ") =====");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Gestionar Empleados");
            System.out.println("3. Gestionar Vehiculos");
            System.out.println("4. Gestionar Turnos");
            System.out.println("5. Gestionar Reparaciones");
            System.out.println("0. Cerrar sesion");
            opcion = Consola.entero("Opcion: ");

            switch (opcion) {
                case 1:
                    menuClientes(taller);
                    break;
                case 2:
                    menuEmpleados(taller);
                    break;
                case 3:
                    menuVehiculos(taller);
                    break;
                case 4:
                    menuTurnos(taller);
                    break;
                case 5:
                    menuReparaciones(taller);
                    break;
                case 0:
                    System.out.println("Cerrando sesion...");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
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

    ///CLIENTES

    private static void menuClientes(SistemaTaller taller) {
        int op = -1;
        while (op != 0) {
            System.out.println("\n-- Clientes --");
            System.out.println("1. Listar  2. Buscar  3. Agregar  4. Modificar  5. Dar de baja  0. Volver");
            op = Consola.entero("Opcion: ");

            try {
                switch (op) {
                    case 1:
                        mostrarLista(taller.listarClientes(), "No hay clientes cargados.");
                        break;
                    case 2: {
                        String dni = Consola.texto("DNI a buscar: ");
                        System.out.println(taller.buscarCliente(dni));
                        break;
                    }
                    case 3: {
                        String nombre = Consola.texto("Nombre: ");
                        String dni = Consola.texto("DNI (8 digitos): ");
                        String tel = Consola.texto("Telefono: ");
                        String email = Consola.texto("Email: ");
                        String pass = Consola.texto("Contrasenia: ");
                        Cliente nuevo = new Cliente(nombre, dni, tel, email, pass);
                        Consola.resultado(taller.agregarCliente(nuevo), "Cliente agregado: " + nuevo,
                                "No se pudo agregar el cliente.");
                        break;
                    }
                    case 4: {
                        String dni = Consola.texto("DNI del cliente a modificar: ");
                        String nombre = Consola.texto("Nuevo nombre: ");
                        String tel = Consola.texto("Nuevo telefono: ");
                        String email = Consola.texto("Nuevo email: ");
                        Consola.resultado(taller.modificarCliente(dni, nombre, tel, email),
                                "Cliente modificado.", "No se pudo modificar el cliente.");
                        break;
                    }
                    case 5: {
                        String dni = Consola.texto("DNI del cliente a dar de baja: ");
                        Consola.resultado(taller.darDeBajaCliente(dni),
                                "Cliente dado de baja.", "No se pudo dar de baja al cliente.");
                        break;
                    }
                    case 0:
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

    /// EMPLEADOS
    private static void menuEmpleados(SistemaTaller taller) {
        int op = -1;
        while (op != 0) {
            System.out.println("\n-- Empleados --");
            System.out.println("1. Listar  2. Buscar  3. Agregar  4. Modificar  5. Dar de baja  0. Volver");
            op = Consola.entero("Opcion: ");

            try {
                switch (op) {
                    case 1:
                        mostrarLista(taller.listarEmpleados(), "No hay empleados cargados.");
                        break;
                    case 2: {
                        String dni = Consola.texto("DNI a buscar: ");
                        System.out.println(taller.buscarEmpleado(dni));
                        break;
                    }
                    case 3: {
                        String nombre = Consola.texto("Nombre: ");
                        String dni = Consola.texto("DNI (8 digitos): ");
                        String tel = Consola.texto("Telefono: ");
                        String email = Consola.texto("Email: ");
                        String pass = Consola.texto("Contrasenia: ");
                        Cargo cargo = Consola.cargo("Cargo (ADMINISTRADOR/MECANICO/RECEPCIONISTA): ");
                        double salario = Consola.decimal("Salario: ");
                        Empleado nuevo = new Empleado(nombre, dni, tel, email, pass, cargo, salario);
                        Consola.resultado(taller.agregarEmpleado(nuevo), "Empleado agregado: " + nuevo,
                                "No se pudo agregar el empleado.");
                        break;
                    }
                    case 4: {
                        String dni = Consola.texto("DNI del empleado a modificar: ");
                        String nombre = Consola.texto("Nuevo nombre: ");
                        String tel = Consola.texto("Nuevo telefono: ");
                        String email = Consola.texto("Nuevo email: ");
                        Cargo cargo = Consola.cargo("Nuevo cargo (ADMINISTRADOR/MECANICO/RECEPCIONISTA): ");
                        double salario = Consola.decimal("Nuevo salario: ");
                        Consola.resultado(taller.modificarEmpleado(dni, nombre, tel, email, cargo, salario),
                                "Empleado modificado.", "No se pudo modificar el empleado.");
                        break;
                    }
                    case 5: {
                        String dni = Consola.texto("DNI del empleado a dar de baja: ");
                        Consola.resultado(taller.darDeBajaEmpleado(dni),
                                "Empleado dado de baja.", "No se pudo dar de baja al empleado.");
                        break;
                    }
                    case 0:
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

    ///VEHICULOS

    private static void menuVehiculos(SistemaTaller taller) {
        int op = -1;
        while (op != 0) {
            System.out.println("\n-- Vehiculos --");
            System.out.println("1. Listar  2. Buscar  3. Agregar  4. Modificar  5. Dar de baja  0. Volver");
            op = Consola.entero("Opcion: ");

            try {
                switch (op) {
                    case 1:
                        mostrarLista(taller.listarVehiculos(), "No hay vehiculos cargados.");
                        break;
                    case 2: {
                        String patente = Consola.texto("Patente a buscar: ");
                        System.out.println(taller.buscarVehiculo(patente));
                        break;
                    }
                    case 3: {
                        String patente = Consola.texto("Patente: ");
                        String marca = Consola.texto("Marca: ");
                        String modelo = Consola.texto("Modelo: ");
                        int anio = Consola.entero("Anio: ");
                        String dni = Consola.texto("DNI del duenio: ");
                        Vehiculo nuevo = new Vehiculo(patente, marca, modelo, anio, dni);
                        Consola.resultado(taller.agregarVehiculo(nuevo), "Vehiculo agregado: " + nuevo,
                                "No se pudo agregar el vehiculo.");
                        break;
                    }
                    case 4: {
                        String patente = Consola.texto("Patente del vehiculo a modificar: ");
                        String marca = Consola.texto("Nueva marca: ");
                        String modelo = Consola.texto("Nuevo modelo: ");
                        int anio = Consola.entero("Nuevo anio: ");
                        Consola.resultado(taller.modificarVehiculo(patente, marca, modelo, anio),
                                "Vehiculo modificado.", "No se pudo modificar el vehiculo.");
                        break;
                    }
                    case 5: {
                        String patente = Consola.texto("Patente del vehiculo a dar de baja: ");
                        Consola.resultado(taller.darDeBajaVehiculo(patente),
                                "Vehiculo dado de baja.", "No se pudo dar de baja el vehiculo.");
                        break;
                    }
                    case 0:
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

    ///TURNOS
    private static void menuTurnos(SistemaTaller taller) {
        int op = -1;
        while (op != 0) {
            System.out.println("\n-- Turnos --");
            System.out.println("1. Listar  2. Buscar  3. Agregar  4. Modificar  5. Cancelar  6. Marcar atendido  0. Volver");
            op = Consola.entero("Opcion: ");

            try {
                switch (op) {
                    case 1:
                        mostrarLista(taller.listarTurnos(), "No hay turnos cargados.");
                        break;
                    case 2: {
                        int id = Consola.entero("ID del turno a buscar: ");
                        System.out.println(taller.buscarTurno(id));
                        break;
                    }
                    case 3: {
                        String patente = Consola.texto("Patente del vehiculo: ");
                        String dniMecanico = Consola.texto("DNI del mecanico: ");
                        LocalDate fecha = Consola.fecha("Fecha (AAAA-MM-DD): ");
                        Turno nuevo = new Turno(patente, dniMecanico, fecha);
                        Consola.resultado(taller.agregarTurno(nuevo), "Turno agregado: " + nuevo,
                                "No se pudo agregar el turno.");
                        break;
                    }
                    case 4: {
                        int id = Consola.entero("ID del turno a modificar: ");
                        String dniMecanico = Consola.texto("Nuevo DNI del mecanico: ");
                        LocalDate fecha = Consola.fecha("Nueva fecha (AAAA-MM-DD): ");
                        Consola.resultado(taller.modificarTurno(id, dniMecanico, fecha),
                                "Turno modificado.", "No se pudo modificar el turno.");
                        break;
                    }
                    case 5: {
                        int id = Consola.entero("ID del turno a cancelar: ");
                        Consola.resultado(taller.cancelarTurno(id),
                                "Turno cancelado.", "No se pudo cancelar el turno.");
                        break;
                    }
                    case 6: {
                        int id = Consola.entero("ID del turno atendido: ");
                        Consola.resultado(taller.marcarTurnoAtendido(id),
                                "Turno marcado como atendido.", "No se pudo actualizar el turno.");
                        break;
                    }
                    case 0:
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

    ///REPARACIONES
    private static void menuReparaciones(SistemaTaller taller) {
        int op = -1;
        while (op != 0) {
            System.out.println("\n-- Reparaciones --");
            System.out.println("1. Listar  2. Listar activas  3. Buscar  4. Agregar  5. Modificar  6. Iniciar  7. Finalizar  0. Volver");
            op = Consola.entero("Opcion: ");

            try {
                switch (op) {
                    case 1:
                        mostrarLista(taller.listarReparaciones(), "No hay reparaciones cargadas.");
                        break;
                    case 2:
                        mostrarLista(taller.listarReparacionesActivas(), "No hay reparaciones activas.");
                        break;
                    case 3: {
                        int id = Consola.entero("ID de la reparacion a buscar: ");
                        System.out.println(taller.buscarReparacion(id));
                        break;
                    }
                    case 4: {
                        String patente = Consola.texto("Patente del vehiculo: ");
                        String dniMecanico = Consola.texto("DNI del mecanico: ");
                        String descripcion = Consola.texto("Descripcion: ");
                        double costo = Consola.decimal("Costo: ");
                        Reparacion nueva = new Reparacion(patente, dniMecanico, descripcion, costo);
                        Consola.resultado(taller.agregarReparacion(nueva), "Reparacion agregada: " + nueva,
                                "No se pudo agregar la reparacion.");
                        break;
                    }
                    case 5: {
                        int id = Consola.entero("ID de la reparacion a modificar: ");
                        String descripcion = Consola.texto("Nueva descripcion: ");
                        double costo = Consola.decimal("Nuevo costo: ");
                        Consola.resultado(taller.modificarReparacion(id, descripcion, costo),
                                "Reparacion modificada.", "No se pudo modificar la reparacion.");
                        break;
                    }
                    case 6: {
                        int id = Consola.entero("ID de la reparacion a iniciar: ");
                        Consola.resultado(taller.iniciarReparacion(id),
                                "Reparacion iniciada.", "No se pudo iniciar la reparacion.");
                        break;
                    }
                    case 7: {
                        int id = Consola.entero("ID de la reparacion a finalizar: ");
                        Consola.resultado(taller.finalizarReparacion(id),
                                "Reparacion finalizada.", "No se pudo finalizar la reparacion.");
                        break;
                    }
                    case 0:
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
}
