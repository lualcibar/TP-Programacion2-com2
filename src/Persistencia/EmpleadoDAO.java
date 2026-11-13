package Persistencia;

import Conexion.ConexionBD;
import Entidades.Empleado;
import Enums.Cargo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Empleado (tabla 'empleado'). Misma logica que ClienteDAO.
 * El cargo se guarda como texto (enum de MySQL) y se convierte con Cargo.valueOf().
 */
public class EmpleadoDAO {


    public boolean insertar(Empleado e) throws SQLException {
        String sql = "INSERT INTO empleado (dni, nombre, telefono, email, password, cargo, salario, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getDni());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getTelefono());
            ps.setString(4, e.getEmail());
            ps.setString(5, e.getPassword());
            ps.setString(6, e.getCargo().name());
            ps.setDouble(7, e.getSalario());
            ps.setBoolean(8, e.isActivo());

            return ps.executeUpdate() > 0;
        }
    }

    // listar todos los empleados
    public List<Empleado> listar() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT dni, nombre, telefono, email, password, cargo, salario, activo FROM empleado ORDER BY dni";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                empleados.add(armarEmpleado(rs));
            }
        }
        return empleados;
    }

    // buscar por DNI en la base de datos
    public Empleado buscarPorDni(String dni) throws SQLException {
        String sql = "SELECT dni, nombre, telefono, email, password, cargo, salario, activo FROM empleado WHERE dni = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarEmpleado(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public boolean modificar(Empleado e) throws SQLException {
        String sql = "UPDATE empleado SET nombre = ?, telefono = ?, email = ?, password = ?, "
                + "cargo = ?, salario = ?, activo = ? WHERE dni = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getTelefono());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getPassword());
            ps.setString(5, e.getCargo().name());
            ps.setDouble(6, e.getSalario());
            ps.setBoolean(7, e.isActivo());
            ps.setString(8, e.getDni());

            return ps.executeUpdate() > 0;
        }
    }

    // BAJA LOGICA: las reparaciones y turnos referencian al empleado, por eso no se borra la fila
    public boolean darDeBaja(String dni) throws SQLException {
        String sql = "UPDATE empleado SET activo = FALSE WHERE dni = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            return ps.executeUpdate() > 0;
        }
    }

    private Empleado armarEmpleado(ResultSet rs) throws SQLException {
        Empleado e = new Empleado(
                rs.getString("nombre"),
                rs.getString("dni"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getString("password"),
                Cargo.valueOf(rs.getString("cargo")),
                rs.getDouble("salario")
        );
        e.setActivo(rs.getBoolean("activo"));
        return e;
    }
}
