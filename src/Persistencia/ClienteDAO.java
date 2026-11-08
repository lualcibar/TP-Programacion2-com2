package Persistencia;

import Entidades.Cliente;
import Conexion.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public boolean insertar(Cliente c) throws SQLException {
        String sql = "INSERT INTO cliente (dni, nombre, telefono, email, password, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getPassword());
            ps.setBoolean(6, c.isActivo());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Cliente> listar() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT dni, nombre, telefono, email, password, activo FROM cliente ORDER BY dni";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(armarCliente(rs));
            }
        }
        return clientes;
    }

    public Cliente buscarPorDni(String dni) throws SQLException {
        String sql = "SELECT dni, nombre, telefono, email, password, activo FROM cliente WHERE dni = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarCliente(rs);
                }
            }
        }
        return null;
    }

    public boolean modificar(Cliente c) throws SQLException {
        String sql = "UPDATE cliente SET nombre = ?, telefono = ?, email = ?, password = ?, activo = ? WHERE dni = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getTelefono());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getPassword());
            ps.setBoolean(5, c.isActivo());
            ps.setString(6, c.getDni());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean darDeBaja(String dni) throws SQLException {
        String sql = "UPDATE cliente SET activo = FALSE WHERE dni = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            return ps.executeUpdate() > 0;
        }
    }

    private Cliente armarCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente(
                rs.getString("nombre"),
                rs.getString("dni"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getString("password")
        );
        c.setActivo(rs.getBoolean("activo"));
        return c;
    }
}
