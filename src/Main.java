import Entidades.Cliente;
import Entidades.Empleado;
import Entidades.Persona;
import Exceptions.TallerException;
import Menu.Consola;
import Menu.MenuAdmin;
import Menu.MenuCliente;
import Servicio.SistemaTaller;

import java.sql.SQLException;

/**
 * Punto de entrada del sistema del taller mecanico.
 * 1) Crea el SistemaTaller (que carga ttodo desde MySQL).
 * 2) Pide DNI y contrasenia (3 intentos).
 * 3) Segun el tipo de usuario muestra el menu de ADMINISTRADOR o el de CLIENTE.
 * Antes de ejecutar: correr database/taller_mecanico.sql en MySQL y agregar
 * Lib/mysql-connector-j-26.10.0.jar como libreria del proyecto.
 */
public class Main {
    public static void main(String[] args) {

        SistemaTaller taller;
        try {
            taller = new SistemaTaller();
        } catch (SQLException e) {
            // si la base no responde no tiene sentido seguir: el sistema no se crea
            System.err.println("No se pudo conectar con la base de datos: " + e.getMessage());
            return;
        }

        System.out.println("===== SISTEMA TALLER MECANICO =====");

        Persona usuario = null;
        int intentos = 0;
        while (usuario == null && intentos < 3) {
            String dni = Consola.texto("DNI: ");
            String password = Consola.texto("Contrasenia: ");
            try {
                usuario = taller.autenticar(dni, password);
            } catch (TallerException e) {
                intentos++;
                System.out.println("Error: " + e.getMessage());
            }
        }

        if (usuario == null) {
            System.out.println("Demasiados intentos fallidos. Hasta luego.");
            return;
        }

        // polimorfismo: autenticar devuelve una Persona; segun su tipo real se muestra el menu que corresponde
        if (usuario instanceof Empleado) {
            MenuAdmin.mostrar(taller, (Empleado) usuario);
        } else {
            MenuCliente.mostrar(taller, (Cliente) usuario);
        }
    }
}
