package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que se encarga unicamente de abrir conexiones con la base de datos MySQL.
 * Es la misma logica del ejemplo de clase: los DAO piden una conexion cada vez
 * que la necesitan y la cierran solos (try-with-resources).
 * IMPORTANTE: si tu MySQL tiene otro usuario o contrasenia, cambialos aca.
 * La base de datos se crea con el script database/taller_mecanico.sql
 */
public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/taller_mecanico";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
