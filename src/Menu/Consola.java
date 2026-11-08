package Menu;

import Enums.Cargo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public final class Consola {

    private static final Scanner SC = new Scanner(System.in);

    private Consola() {
    }

    public static String texto(String mensaje) {
        System.out.print(mensaje);
        return SC.nextLine().trim();
    }

    public static int entero(String mensaje) {
        while (true) {
            String linea = texto(mensaje);
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    public static double decimal(String mensaje) {
        while (true) {
            String linea = texto(mensaje).replace(',', '.');
            try {
                return Double.parseDouble(linea);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido (ejemplo: 85000.50).");
            }
        }
    }

    public static LocalDate fecha(String mensaje) {
        while (true) {
            String linea = texto(mensaje);
            try {
                return LocalDate.parse(linea);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha invalida. Use el formato AAAA-MM-DD (ejemplo: 2026-12-31).");
            }
        }
    }

    public static Cargo cargo(String mensaje) {
        while (true) {
            String linea = texto(mensaje).toUpperCase();
            try {
                return Cargo.valueOf(linea);
            } catch (IllegalArgumentException e) {
                System.out.println("Cargo invalido. Opciones: ADMINISTRADOR, MECANICO, RECEPCIONISTA.");
            }
        }
    }

    public static void resultado(boolean ok, String mensajeOk, String mensajeError) {
        System.out.println(ok ? mensajeOk : mensajeError);
    }
}
