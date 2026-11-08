package Servicio;

import Entidades.*;
import Enums.Cargo;
import Enums.EstadoReparacion;
import Enums.EstadoTurno;
import Exceptions.*;
import Persistencia.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class SistemaTaller {

    private Map<String, Cliente> clientes = new LinkedHashMap<>(); // clave = DNI
    private Map<String, Empleado> empleados = new LinkedHashMap<>(); // clave = DNI
    private Set<Vehiculo> vehiculos = new LinkedHashSet<>();
    private List<Reparacion> reparaciones = new ArrayList<>();
    private List<Turno> turnos = new ArrayList<>();

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private final ReparacionDAO reparacionDAO = new ReparacionDAO();
    private final TurnoDAO turnoDAO = new TurnoDAO();

    public SistemaTaller() throws SQLException {
        cargarTodo();
    }

    public void cargarTodo() throws SQLException {
        List<Cliente> listaClientes = clienteDAO.listar();
        List<Empleado> listaEmpleados = empleadoDAO.listar();
        List<Vehiculo> listaVehiculos = vehiculoDAO.listar();
        List<Reparacion> listaReparaciones = reparacionDAO.listar();
        List<Turno> listaTurnos = turnoDAO.listar();

        clientes.clear();
        for (Cliente c : listaClientes) {
            clientes.put(c.getDni(), c);
        }
        empleados.clear();
        for (Empleado e : listaEmpleados) {
            empleados.put(e.getDni(), e);
        }
        vehiculos.clear();
        vehiculos.addAll(listaVehiculos);
        reparaciones.clear();
        reparaciones.addAll(listaReparaciones);
        turnos.clear();
        turnos.addAll(listaTurnos);

        for (Vehiculo v : vehiculos) {
            Cliente dueno = clientes.get(v.getDniDuenio());
            if (dueno != null && v.isActivo()) {
                dueno.agregarVehiculo(v.getPatente());
            }
        }
    }

    /// inicio de sesion
    public Persona autenticar(String dni, String password)
            throws CredencialesInvalidasException, ClienteNoActivoException {

        Empleado empleado = empleados.get(dni);
        if (empleado != null) {
            if (!empleado.getPassword().equals(password)) {
                throw new CredencialesInvalidasException("DNI o contrasenia incorrectos");
            }
            if (!empleado.isActivo()) {
                throw new CredencialesInvalidasException("El empleado esta dado de baja");
            }
            if (empleado.getCargo() != Cargo.ADMINISTRADOR) {
                throw new CredencialesInvalidasException("Este tipo de empleado no tiene acceso al sistema");
            }
            return empleado;
        }

        Cliente cliente = clientes.get(dni);
        if (cliente == null || !cliente.getPassword().equals(password)) {
            // mismo mensaje para "no existe" y "contrasenia mal": no damos pistas
            throw new CredencialesInvalidasException("DNI o contrasenia incorrectos");
        }
        if (!cliente.isActivo()) {
            throw new ClienteNoActivoException(dni);
        }
        return cliente;
    }

    /// clientes

    public boolean agregarCliente(Cliente c) throws SQLException, ElementoDuplicadoException {
        if (clientes.containsKey(c.getDni()) || empleados.containsKey(c.getDni())) {
            throw new ElementoDuplicadoException("Ya existe una persona con DNI: " + c.getDni());
        }
        if (clienteDAO.insertar(c)) {
            clientes.put(c.getDni(), c);
            return true;
        }
        return false;
    }

    public Cliente buscarCliente(String dni) throws ClienteNoEncontradoException {
        Cliente c = clientes.get(dni);
        if (c == null) {
            throw new ClienteNoEncontradoException(dni);
        }
        return c;
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes.values());
    }

    public boolean modificarCliente(String dni, String nuevoNombre, String nuevoTelefono, String nuevoEmail)
            throws SQLException, ClienteNoEncontradoException {
        Cliente anterior = buscarCliente(dni);

        Cliente actualizado = new Cliente(nuevoNombre, dni, nuevoTelefono, nuevoEmail, anterior.getPassword());
        actualizado.setActivo(anterior.isActivo());
        for (String patente : anterior.getVehiculos()) {
            actualizado.agregarVehiculo(patente);
        }

        if (clienteDAO.modificar(actualizado)) {
            clientes.put(dni, actualizado);
            return true;
        }
        return false;
    }

    public boolean darDeBajaCliente(String dni)
            throws SQLException, ClienteNoEncontradoException, ClienteNoActivoException {
        Cliente c = buscarCliente(dni);
        if (!c.isActivo()) {
            throw new ClienteNoActivoException(dni);
        }
        if (clienteDAO.darDeBaja(dni)) {
            c.setActivo(false);
            return true;
        }
        return false;
    }

    /// empleados

    public boolean agregarEmpleado(Empleado e) throws SQLException, ElementoDuplicadoException {
        if (empleados.containsKey(e.getDni()) || clientes.containsKey(e.getDni())) {
            throw new ElementoDuplicadoException("Ya existe una persona con DNI: " + e.getDni());
        }
        if (empleadoDAO.insertar(e)) {
            empleados.put(e.getDni(), e);
            return true;
        }
        return false;
    }

    public Empleado buscarEmpleado(String dni) throws EmpleadoNoEncontradoException {
        Empleado e = empleados.get(dni);
        if (e == null) {
            throw new EmpleadoNoEncontradoException(dni);
        }
        return e;
    }

    public List<Empleado> listarEmpleados() {
        return new ArrayList<>(empleados.values());
    }

    public List<Empleado> listarMecanicosActivos() {
        List<Empleado> resultado = new ArrayList<>();
        for (Empleado e : empleados.values()) {
            if (e.getCargo() == Cargo.MECANICO && e.isActivo()) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    public boolean modificarEmpleado(String dni, String nuevoNombre, String nuevoTelefono, String nuevoEmail,
                                     Cargo nuevoCargo, double nuevoSalario)
            throws SQLException, EmpleadoNoEncontradoException {
        Empleado anterior = buscarEmpleado(dni);

        if (anterior.getCargo() == Cargo.ADMINISTRADOR && anterior.isActivo()
                && nuevoCargo != Cargo.ADMINISTRADOR && contarAdministradoresActivos() == 1) {
            throw new DatosInvalidosException("No se puede cambiar el cargo del unico administrador activo");
        }

        Empleado actualizado = new Empleado(nuevoNombre, dni, nuevoTelefono, nuevoEmail,
                anterior.getPassword(), nuevoCargo, nuevoSalario);
        actualizado.setActivo(anterior.isActivo());

        if (empleadoDAO.modificar(actualizado)) {
            empleados.put(dni, actualizado);
            return true;
        }
        return false;
    }

    public boolean darDeBajaEmpleado(String dni) throws SQLException, EmpleadoNoEncontradoException {
        Empleado e = buscarEmpleado(dni);
        if (!e.isActivo()) {
            throw new DatosInvalidosException("El empleado " + dni + " ya esta dado de baja");
        }
        if (e.getCargo() == Cargo.ADMINISTRADOR && contarAdministradoresActivos() == 1) {
            throw new DatosInvalidosException("No se puede dar de baja al unico administrador activo");
        }
        if (empleadoDAO.darDeBaja(dni)) {
            e.setActivo(false);
            return true;
        }
        return false;
    }

    private int contarAdministradoresActivos() {
        int cantidad = 0;
        for (Empleado e : empleados.values()) {
            if (e.getCargo() == Cargo.ADMINISTRADOR && e.isActivo()) {
                cantidad++;
            }
        }
        return cantidad;
    }

    /// vehiculos

    public boolean agregarVehiculo(Vehiculo v)
            throws SQLException, ClienteNoActivoException, ElementoDuplicadoException {
        Cliente dueno = obtenerClienteActivo(v.getDniDuenio());
        if (vehiculos.contains(v)) {
            throw new ElementoDuplicadoException("Ya existe un vehiculo con la patente: " + v.getPatente());
        }
        if (vehiculoDAO.insertar(v)) {
            vehiculos.add(v);
            dueno.agregarVehiculo(v.getPatente());
            return true;
        }
        return false;
    }

    public Vehiculo buscarVehiculo(String patente) throws VehiculoNoEncontradoException {
        Vehiculo v = buscarVehiculoONull(patente);
        if (v == null) {
            throw new VehiculoNoEncontradoException(patente);
        }
        return v;
    }

    public List<Vehiculo> listarVehiculos() {
        return new ArrayList<>(vehiculos);
    }

    public List<Vehiculo> listarVehiculosPorCliente(String dniCliente) throws ClienteNoEncontradoException {
        Cliente c = buscarCliente(dniCliente);
        List<Vehiculo> resultado = new ArrayList<>();
        for (String patente : c.getVehiculos()) {
            Vehiculo v = buscarVehiculoONull(patente);
            if (v != null) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public boolean modificarVehiculo(String patente, String nuevaMarca, String nuevoModelo, int nuevoAnio)
            throws SQLException, VehiculoNoEncontradoException {
        Vehiculo anterior = buscarVehiculo(patente);

        Vehiculo actualizado = new Vehiculo(anterior.getPatente(), nuevaMarca, nuevoModelo, nuevoAnio,
                anterior.getDniDuenio());
        actualizado.setActivo(anterior.isActivo());

        if (vehiculoDAO.modificar(actualizado)) {
            vehiculos.remove(anterior);
            vehiculos.add(actualizado);
            return true;
        }
        return false;
    }

    public boolean darDeBajaVehiculo(String patente) throws SQLException, VehiculoNoEncontradoException {
        Vehiculo v = buscarVehiculo(patente);
        if (!v.isActivo()) {
            throw new DatosInvalidosException("El vehiculo " + v.getPatente() + " ya esta dado de baja");
        }
        for (Reparacion r : reparaciones) {
            if (r.getPatenteVehiculo().equalsIgnoreCase(v.getPatente()) && r.estaActiva()) {
                throw new DatosInvalidosException("No se puede dar de baja: el vehiculo tiene la reparacion #"
                        + r.getId() + " sin finalizar");
            }
        }
        if (vehiculoDAO.darDeBaja(v.getPatente())) {
            v.setActivo(false);
            Cliente dueno = clientes.get(v.getDniDuenio());
            if (dueno != null) {
                dueno.eliminarVehiculo(v.getPatente());
            }
            return true;
        }
        return false;
    }

    /// reparaciones

    public boolean agregarReparacion(Reparacion r)
            throws SQLException, VehiculoNoEncontradoException, ClienteNoActivoException,
            EmpleadoNoEncontradoException {
        Vehiculo v = obtenerVehiculoActivo(r.getPatenteVehiculo());
        obtenerClienteActivo(v.getDniDuenio());
        obtenerMecanicoActivo(r.getDniMecanico());

        int idGenerado = reparacionDAO.insertar(r);
        if (idGenerado > 0) {
            reparaciones.add(r);
            return true;
        }
        return false;
    }

    public Reparacion buscarReparacion(int id) throws ReparacionNoEncontradaException {
        for (Reparacion r : reparaciones) {
            if (r.getId() == id) {
                return r;
            }
        }
        throw new ReparacionNoEncontradaException(id);
    }

    public List<Reparacion> listarReparaciones() {
        return new ArrayList<>(reparaciones);
    }

    public List<Reparacion> listarReparacionesActivas() {
        List<Reparacion> resultado = new ArrayList<>();
        for (Reparacion r : reparaciones) {
            if (r.estaActiva()) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public List<Reparacion> listarReparacionesPorCliente(String dniCliente) throws ClienteNoEncontradoException {
        buscarCliente(dniCliente); // lanza excepcion si el cliente no existe
        List<Reparacion> resultado = new ArrayList<>();
        for (Reparacion r : reparaciones) {
            Vehiculo v = buscarVehiculoONull(r.getPatenteVehiculo());
            if (v != null && v.getDniDuenio().equals(dniCliente)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public boolean modificarReparacion(int id, String nuevaDescripcion, double nuevoCosto)
            throws SQLException, ReparacionNoEncontradaException {
        Reparacion anterior = buscarReparacion(id);
        if (!anterior.estaActiva()) {
            throw new DatosInvalidosException("No se puede modificar una reparacion FINALIZADA");
        }

        Reparacion actualizada = new Reparacion(id, anterior.getPatenteVehiculo(), anterior.getDniMecanico(),
                nuevaDescripcion, nuevoCosto);
        actualizada.setEstado(anterior.getEstado());
        actualizada.setFechaInicio(anterior.getFechaInicio());
        actualizada.setFechaFin(anterior.getFechaFin());

        if (reparacionDAO.modificar(actualizada)) {
            for (int i = 0; i < reparaciones.size(); i++) {
                if (reparaciones.get(i).getId() == id) {
                    reparaciones.set(i, actualizada);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean iniciarReparacion(int id) throws SQLException, ReparacionNoEncontradaException {
        Reparacion r = buscarReparacion(id);
        EstadoReparacion estadoAnterior = r.getEstado();
        LocalDate inicioAnterior = r.getFechaInicio();
        LocalDate finAnterior = r.getFechaFin();

        r.iniciarReparacion(); // lanza DatosInvalidosException (y no cambia nada) si no estaba PENDIENTE
        return guardarCambioDeEstado(r, estadoAnterior, inicioAnterior, finAnterior);
    }

    public boolean finalizarReparacion(int id) throws SQLException, ReparacionNoEncontradaException {
        Reparacion r = buscarReparacion(id);
        EstadoReparacion estadoAnterior = r.getEstado();
        LocalDate inicioAnterior = r.getFechaInicio();
        LocalDate finAnterior = r.getFechaFin();

        r.finalizarReparacion();
        return guardarCambioDeEstado(r, estadoAnterior, inicioAnterior, finAnterior);
    }

    private boolean guardarCambioDeEstado(Reparacion r, EstadoReparacion estadoAnterior,
                                          LocalDate inicioAnterior, LocalDate finAnterior) throws SQLException {
        boolean guardado;
        try {
            guardado = reparacionDAO.modificar(r);
        } catch (SQLException e) {
            restaurar(r, estadoAnterior, inicioAnterior, finAnterior);
            throw e;
        }
        if (!guardado) {
            restaurar(r, estadoAnterior, inicioAnterior, finAnterior);
        }
        return guardado;
    }

    private void restaurar(Reparacion r, EstadoReparacion estado, LocalDate inicio, LocalDate fin) {
        r.setEstado(estado);
        r.setFechaInicio(inicio);
        r.setFechaFin(fin);
    }

    /// turnos

    public boolean agregarTurno(Turno t)
            throws SQLException, VehiculoNoEncontradoException, ClienteNoActivoException,
            EmpleadoNoEncontradoException {
        Vehiculo v = obtenerVehiculoActivo(t.getPatenteVehiculo());
        obtenerClienteActivo(v.getDniDuenio());
        obtenerMecanicoActivo(t.getDniMecanico());
        validarFechaNoPasada(t.getFecha());

        int idGenerado = turnoDAO.insertar(t);
        if (idGenerado > 0) {
            turnos.add(t);
            return true;
        }
        return false;
    }

    public boolean agregarTurnoDeCliente(String dniCliente, Turno t)
            throws SQLException, VehiculoNoEncontradoException, ClienteNoActivoException,
            EmpleadoNoEncontradoException {
        obtenerClienteActivo(dniCliente);
        Vehiculo v = obtenerVehiculoActivo(t.getPatenteVehiculo());
        if (!v.getDniDuenio().equals(dniCliente)) {
            throw new DatosInvalidosException("El vehiculo " + v.getPatente() + " no te pertenece");
        }
        return agregarTurno(t);
    }

    public Turno buscarTurno(int id) throws TurnoNoEncontradoException {
        for (Turno t : turnos) {
            if (t.getId() == id) {
                return t;
            }
        }
        throw new TurnoNoEncontradoException(id);
    }

    public List<Turno> listarTurnos() {
        return new ArrayList<>(turnos);
    }

    public List<Turno> listarTurnosPorCliente(String dniCliente) throws ClienteNoEncontradoException {
        buscarCliente(dniCliente);
        List<Turno> resultado = new ArrayList<>();
        for (Turno t : turnos) {
            if (turnoPerteneceACliente(t, dniCliente)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public boolean modificarTurno(int id, String nuevoDniMecanico, LocalDate nuevaFecha)
            throws SQLException, TurnoNoEncontradoException, EmpleadoNoEncontradoException {
        Turno anterior = buscarTurno(id);
        if (anterior.getEstado() != EstadoTurno.PENDIENTE) {
            throw new DatosInvalidosException("Solo se puede modificar un turno PENDIENTE (estado actual: "
                    + anterior.getEstado() + ")");
        }
        obtenerMecanicoActivo(nuevoDniMecanico);
        validarFechaNoPasada(nuevaFecha);

        Turno actualizado = new Turno(id, anterior.getPatenteVehiculo(), nuevoDniMecanico, nuevaFecha,
                EstadoTurno.PENDIENTE);
        if (turnoDAO.modificar(actualizado)) {
            reemplazarTurno(actualizado);
            return true;
        }
        return false;
    }

    public boolean cancelarTurno(int id) throws SQLException, TurnoNoEncontradoException {
        Turno t = buscarTurno(id);
        if (t.getEstado() != EstadoTurno.PENDIENTE) {
            throw new DatosInvalidosException("Solo se puede cancelar un turno PENDIENTE (estado actual: "
                    + t.getEstado() + ")");
        }
        if (turnoDAO.cancelar(id)) {
            t.setEstado(EstadoTurno.CANCELADO);
            return true;
        }
        return false;
    }

    public boolean cancelarTurnoDeCliente(String dniCliente, int id) throws SQLException, TurnoNoEncontradoException {
        Turno t = buscarTurno(id);
        if (!turnoPerteneceACliente(t, dniCliente)) {
            throw new DatosInvalidosException("El turno " + id + " no te pertenece");
        }
        return cancelarTurno(id);
    }

    public boolean marcarTurnoAtendido(int id) throws SQLException, TurnoNoEncontradoException {
        Turno anterior = buscarTurno(id);
        if (anterior.getEstado() != EstadoTurno.PENDIENTE) {
            throw new DatosInvalidosException("Solo se puede atender un turno PENDIENTE (estado actual: "
                    + anterior.getEstado() + ")");
        }
        Turno actualizado = new Turno(id, anterior.getPatenteVehiculo(), anterior.getDniMecanico(),
                anterior.getFecha(), EstadoTurno.ATENDIDO);
        if (turnoDAO.modificar(actualizado)) {
            reemplazarTurno(actualizado);
            return true;
        }
        return false;
    }

    private void reemplazarTurno(Turno nuevo) {
        for (int i = 0; i < turnos.size(); i++) {
            if (turnos.get(i).getId() == nuevo.getId()) {
                turnos.set(i, nuevo);
                break;
            }
        }
    }

    private boolean turnoPerteneceACliente(Turno t, String dniCliente) {
        Vehiculo v = buscarVehiculoONull(t.getPatenteVehiculo());
        return v != null && v.getDniDuenio().equals(dniCliente);
    }

    /// validaciones auxiliares

    private Vehiculo buscarVehiculoONull(String patente) {
        Iterator<Vehiculo> it = vehiculos.iterator();
        while (it.hasNext()) {
            Vehiculo v = it.next();
            if (v.getPatente().equalsIgnoreCase(patente)) {
                return v;
            }
        }
        return null;
    }

    private Cliente obtenerClienteActivo(String dni) throws ClienteNoActivoException {
        Cliente c = clientes.get(dni);
        if (c == null || !c.isActivo()) {
            throw new ClienteNoActivoException(dni);
        }
        return c;
    }

    private Vehiculo obtenerVehiculoActivo(String patente) throws VehiculoNoEncontradoException {
        Vehiculo v = buscarVehiculo(patente);
        if (!v.isActivo()) {
            throw new DatosInvalidosException("El vehiculo " + v.getPatente() + " esta dado de baja");
        }
        return v;
    }

    private Empleado obtenerMecanicoActivo(String dni) throws EmpleadoNoEncontradoException {
        Empleado e = buscarEmpleado(dni);
        if (!e.isActivo()) {
            throw new DatosInvalidosException("El empleado " + dni + " esta dado de baja");
        }
        if (e.getCargo() != Cargo.MECANICO) {
            throw new DatosInvalidosException("El empleado " + dni + " no es mecanico (cargo: " + e.getCargo() + ")");
        }
        return e;
    }

    private void validarFechaNoPasada(LocalDate fecha) {
        if (fecha.isBefore(LocalDate.now())) {
            throw new DatosInvalidosException("La fecha del turno no puede ser anterior a hoy");
        }
    }








}