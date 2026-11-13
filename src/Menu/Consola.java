package Menu;

import Enums.Cargo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Clase auxiliar para leer datos por teclado de forma segura.
 * Hay UN solo Scanner para ttodo el programa (varios Scanner sobre System.in se pisan entre si).
 * Si el usuario escribe algo invalido (letras donde va un numero, una fecha mal escrita, etc.)
 * se le vuelve a pedir el dato en lugar de que el programa se caiga.
 */
public final class Consola {

    private static final Scanner SC = new Scanner(System.in);

    private Consola() {
    }

    /** Lee una linea de texto (sin espacios al principio ni al final). */
    public static String texto(String mensaje) {
        System.out.print(mensaje);
        return SC.nextLine().trim();
    }

    /** Lee un numero entero; si no es valido, vuelve a pedirlo. */
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

    /** Lee un numero decimal (acepta coma o punto); si no es valido, vuelve a pedirlo. */
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

    /** Lee una fecha con formato AAAA-MM-DD; si no es valida, vuelve a pedirla. */
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

    /** Lee un cargo (ADMINISTRADOR / MECANICO / RECEPCIONISTA); si no es valido, vuelve a pedirlo. */
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

    /** Muestra un mensaje u otro segun el resultado de una operacion contra la base de datos. */
    public static void resultado(boolean ok, String mensajeOk, String mensajeError) {
        System.out.println(ok ? mensajeOk : mensajeError);
    }
}
