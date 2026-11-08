package Persistencia;

import Conexion.ConexionBD;
import Entidades.Vehiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    public boolean insertar(Vehiculo v) throws SQLException {
        String sql = "INSERT INTO vehiculo (patente, marca, modelo, anio, dni_duenio, activo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getPatente());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getAnio());
            ps.setString(5, v.getDniDuenio());
            ps.setBoolean(6, v.isActivo());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Vehiculo> listar() throws SQLException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT patente, marca, modelo, anio, dni_duenio, activo FROM vehiculo ORDER BY patente";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                vehiculos.add(armarVehiculo(rs));
            }
        }
        return vehiculos;
    }

    public Vehiculo buscarPorPatente(String patente) throws SQLException {
        String sql = "SELECT patente, marca, modelo, anio, dni_duenio, activo FROM vehiculo WHERE patente = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, patente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarVehiculo(rs);
                }
            }
        }
        return null;
    }

    public boolean modificar(Vehiculo v) throws SQLException {
        String sql = "UPDATE vehiculo SET marca = ?, modelo = ?, anio = ?, activo = ? WHERE patente = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getMarca());
            ps.setString(2, v.getModelo());
            ps.setInt(3, v.getAnio());
            ps.setBoolean(4, v.isActivo());
            ps.setString(5, v.getPatente());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean darDeBaja(String patente) throws SQLException {
        String sql = "UPDATE vehiculo SET activo = FALSE WHERE patente = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, patente);

            return ps.executeUpdate() > 0;
        }
    }

    private Vehiculo armarVehiculo(ResultSet rs) throws SQLException {
        Vehiculo v = new Vehiculo(
                rs.getString("patente"),
                rs.getString("marca"),
                rs.getString("modelo"),
                rs.getInt("anio"),
                rs.getString("dni_duenio")
        );
        v.setActivo(rs.getBoolean("activo"));
        return v;
    }
}
