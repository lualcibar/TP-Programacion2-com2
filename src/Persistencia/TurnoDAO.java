package Persistencia;

import Conexion.ConexionBD;
import Entidades.Turno;
import Enums.EstadoTurno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Turno (tabla 'turno'). El id es AUTO_INCREMENT (igual que Reparacion).
 * La baja logica del turno es el estado CANCELADO.
 */
public class TurnoDAO {

    // CREATE: retorna el id autoincremental generado en la base de datos
    public int insertar(Turno t) throws SQLException {

        String sqlInsert = "INSERT INTO turno (patente_vehiculo, dni_mecanico, fecha, estado) VALUES (?, ?, ?, ?)";
        String sqlGetId = "SELECT LAST_INSERT_ID()";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement psInsert = con.prepareStatement(sqlInsert);
             PreparedStatement psID = con.prepareStatement(sqlGetId)) {

            psInsert.setString(1, t.getPatenteVehiculo());
            psInsert.setString(2, t.getDniMecanico());
            psInsert.setDate(3, Date.valueOf(t.getFecha()));
            psInsert.setString(4, t.getEstado().name());

            psInsert.executeUpdate();

            try (ResultSet rs = psID.executeQuery()) {
                if (rs.next()) {
                    int ultimoId = rs.getInt(1);
                    t.setId(ultimoId);
                    return ultimoId;
                }
            }
        }
        return -1;
    }

    // READ: listar todos los turnos
    public List<Turno> listar() throws SQLException {
        List<Turno> turnos = new ArrayList<>();
        String sql = "SELECT id, patente_vehiculo, dni_mecanico, fecha, estado FROM turno ORDER BY id";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                turnos.add(armarTurno(rs));
            }
        }
        return turnos;
    }

    // READ: buscar por id en la base de datos
    public Turno buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, patente_vehiculo, dni_mecanico, fecha, estado FROM turno WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarTurno(rs);
                }
            }
        }
        return null;
    }

    // UPDATE: cambia mecanico, fecha y estado (la patente no cambia)
    public boolean modificar(Turno t) throws SQLException {
        String sql = "UPDATE turno SET dni_mecanico = ?, fecha = ?, estado = ? WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getDniMecanico());
            ps.setDate(2, Date.valueOf(t.getFecha()));
            ps.setString(3, t.getEstado().name());
            ps.setInt(4, t.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // BAJA LOGICA: cancelar un turno no lo borra, lo pasa a estado CANCELADO
    public boolean cancelar(int id) throws SQLException {
        String sql = "UPDATE turno SET estado = 'CANCELADO' WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    private Turno armarTurno(ResultSet rs) throws SQLException {
        return new Turno(
                rs.getInt("id"),
                rs.getString("patente_vehiculo"),
                rs.getString("dni_mecanico"),
                rs.getDate("fecha").toLocalDate(),
                EstadoTurno.valueOf(rs.getString("estado"))
        );
    }
}
