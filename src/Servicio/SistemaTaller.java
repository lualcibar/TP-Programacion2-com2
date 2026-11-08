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








}