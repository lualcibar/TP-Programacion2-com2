package Persistencia;

import Conexion.ConexionBD;
import Entidades.Reparacion;
import Enums.EstadoReparacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Reparacion (tabla 'reparacion'). El id es AUTO_INCREMENT en MySQL,
 * por eso insertar() funciona igual que en el ejemplo de clase: devuelve el id generado
 * y lo setea en el objeto.
 * Las reparaciones son un registro historico: no se borran ni se dan de baja.
 */
public class ReparacionDAO {

    // CREATE: retorna el id autoincremental generado en la base de datos
    public int insertar(Reparacion r) throws SQLException {

        String sqlInsert = "INSERT INTO reparacion (patente_vehiculo, dni_mecanico, descripcion, costo, estado, "
                + "fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlGetId = "SELECT LAST_INSERT_ID()";
        // son 2 consultas porque una agrega en la BD y la otra obtiene el id que genero la BD

        try (Connection con = ConexionBD.conectar(); // misma conexion para ambas consultas
             PreparedStatement psInsert = con.prepareStatement(sqlInsert);
             PreparedStatement psID = con.prepareStatement(sqlGetId)) {

            psInsert.setString(1, r.getPatenteVehiculo());
            psInsert.setString(2, r.getDniMecanico());
            psInsert.setString(3, r.getDescripcion());
            psInsert.setDouble(4, r.getCosto());
            psInsert.setString(5, r.getEstado().name());
            asignarFecha(psInsert, 6, r.getFechaInicio());
            asignarFecha(psInsert, 7, r.getFechaFin());

            psInsert.executeUpdate();

            try (ResultSet rs = psID.executeQuery()) {
                if (rs.next()) {
                    int ultimoId = rs.getInt(1);
                    r.setId(ultimoId); // el objeto queda con su id real
                    return ultimoId;
                }
            }
        }
        return -1; // si no pudo hacer la consulta
    }

    // READ: listar todas las reparaciones
    public List<Reparacion> listar() throws SQLException {
        List<Reparacion> reparaciones = new ArrayList<>();
        String sql = "SELECT id, patente_vehiculo, dni_mecanico, descripcion, costo, estado, fecha_inicio, fecha_fin "
                + "FROM reparacion ORDER BY id";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reparaciones.add(armarReparacion(rs));
            }
        }
        return reparaciones;
    }

    // READ: buscar por id en la base de datos
    public Reparacion buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, patente_vehiculo, dni_mecanico, descripcion, costo, estado, fecha_inicio, fecha_fin "
                + "FROM reparacion WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarReparacion(rs);
                }
            }
        }
        return null;
    }

    // UPDATE: cambia descripcion, costo, estado y fechas (la patente y el mecanico no cambian)
    public boolean modificar(Reparacion r) throws SQLException {
        String sql = "UPDATE reparacion SET descripcion = ?, costo = ?, estado = ?, fecha_inicio = ?, fecha_fin = ? "
                + "WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getDescripcion());
            ps.setDouble(2, r.getCosto());
            ps.setString(3, r.getEstado().name());
            asignarFecha(ps, 4, r.getFechaInicio());
            asignarFecha(ps, 5, r.getFechaFin());
            ps.setInt(6, r.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // LocalDate -> java.sql.Date, o NULL si la fecha todavia no existe
    private void asignarFecha(PreparedStatement ps, int posicion, java.time.LocalDate fecha) throws SQLException {
        if (fecha != null) {
            ps.setDate(posicion, Date.valueOf(fecha));
        } else {
            ps.setNull(posicion, Types.DATE);
        }
    }

    private Reparacion armarReparacion(ResultSet rs) throws SQLException {
        Reparacion r = new Reparacion(
                rs.getInt("id"),
                rs.getString("patente_vehiculo"),
                rs.getString("dni_mecanico"),
                rs.getString("descripcion"),
                rs.getDouble("costo")
        );
        r.setEstado(EstadoReparacion.valueOf(rs.getString("estado")));

        Date inicio = rs.getDate("fecha_inicio");
        if (inicio != null) {
            r.setFechaInicio(inicio.toLocalDate());
        }
        Date fin = rs.getDate("fecha_fin");
        if (fin != null) {
            r.setFechaFin(fin.toLocalDate());
        }
        return r;
    }
}
