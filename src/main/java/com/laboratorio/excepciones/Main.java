package com.laboratorio.excepciones;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) { menu(); return; }
        if ("todos".equalsIgnoreCase(args[0])) {
            for (int i = 1; i <= 12; i++) ejecutar(i);
            return;
        }
        try { ejecutar(Integer.parseInt(args[0])); }
        catch (NumberFormatException e) { menu(); }
    }
    private static void menu() {
        System.out.println("Uso: mvn exec:java -Dexec.args=1 (casos 1..12 o todos)");
    }
    private static void ejecutar(int numero) {
        System.out.println("\n=== CASO " + numero + " ===");
        switch (numero) {
            case 1 -> basico(); case 2 -> multiples(); case 3 -> finalmente();
            case 4 -> lanzar(); case 5 -> declarada(); case 6 -> noDeclarada();
            case 7 -> propagacion(); case 8 -> recursos(); case 9 -> multicaptura();
            case 10 -> personalizada(); case 11 -> causas(); case 12 -> integrador();
            default -> menu();
        }
    }
    private static void basico() {
        try { System.out.println(100 / 0); }
        catch (ArithmeticException e) { System.out.println("Recuperado: " + e.getMessage()); }
        System.out.println("El programa continúa");
    }
    private static void multiples() {
        for (String valor : List.of("0", "abc", "5")) {
            try { System.out.println("100 / " + valor + " = " + 100 / Integer.parseInt(valor)); }
            catch (NumberFormatException e) { System.out.println("No es un número: " + valor); }
            catch (ArithmeticException e) { System.out.println("División por cero"); }
        }
    }
    private static void finalmente() {
        try { System.out.println("Abriendo operación"); Integer.parseInt("invalido"); }
        catch (NumberFormatException e) { System.out.println("Se detectó un dato inválido"); }
        finally { System.out.println("finally se ejecuta al salir del try/catch"); }
    }
    private static void lanzar() {
        try { validarMonto(new BigDecimal("-10")); }
        catch (IllegalArgumentException e) { System.out.println("Rechazado: " + e.getMessage()); }
    }
    private static void validarMonto(BigDecimal monto) {
        if (monto.signum() <= 0) throw new IllegalArgumentException("Monto no positivo");
    }
    private static void declarada() {
        try { System.out.println(leerArchivo(Path.of("archivo-que-no-existe.txt"))); }
        catch (IOException e) { System.out.println("Error de lectura controlado: " + e.getClass().getSimpleName()); }
    }
    private static String leerArchivo(Path ruta) throws IOException { return Files.readString(ruta); }
    private static void noDeclarada() {
        try { Integer.parseInt("NO_NUMERICO"); }
        catch (NumberFormatException e) { System.out.println("Unchecked: " + e.getClass().getSimpleName()); }
    }
    private static void propagacion() {
        try { controlador(); }
        catch (IllegalStateException e) { System.out.println("Capturada en main: " + e.getMessage()); }
    }
    private static void controlador() { servicio(); }
    private static void servicio() { repositorio(); }
    private static void repositorio() { throw new IllegalStateException("Repositorio temporalmente no disponible"); }
    private static void recursos() {
        try (Scanner scanner = new Scanner("10 20 30")) {
            int suma = 0;
            while (scanner.hasNextInt()) suma += scanner.nextInt();
            System.out.println("Suma: " + suma + "; Scanner cerrado automáticamente");
        }
    }
    private static void multicaptura() {
        for (String entrada : List.of("x", "0")) {
            try { System.out.println(50 / Integer.parseInt(entrada)); }
            catch (NumberFormatException | ArithmeticException e) { System.out.println("Entrada inválida: " + entrada); }
        }
    }
    private static void personalizada() {
        Banco banco = new Banco();
        try { banco.retirar("CLI-001", new BigDecimal("600")); }
        catch (SaldoInsuficienteException e) { System.out.println("Excepción propia: " + e.getMessage()); }
    }
    private static void causas() {
        try { importarDatos("valor-invalido"); }
        catch (IllegalStateException e) {
            System.out.println("Contexto: " + e.getMessage());
            System.out.println("Causa original: " + e.getCause().getClass().getSimpleName());
        }
    }
    private static void importarDatos(String valor) {
        try { Integer.parseInt(valor); }
        catch (NumberFormatException e) { throw new IllegalStateException("No se pudo importar el importe", e); }
    }
    private static void integrador() {
        Banco banco = new Banco();
        for (String[] operacion : new String[][] {{"CLI-001","100"},{"CLI-001","900"},{"CLI-999","20"},{"CLI-001","-5"},{"CLI-001","50"}}) {
            try {
                BigDecimal nuevoSaldo = banco.retirar(operacion[0], new BigDecimal(operacion[1]));
                System.out.println("APROBADO " + operacion[0] + " monto=" + operacion[1] + " saldo=" + nuevoSaldo);
            } catch (SaldoInsuficienteException | ClienteNoEncontradoException | IllegalArgumentException e) {
                System.out.println("RECHAZADO " + operacion[0] + " monto=" + operacion[1] + " causa=" + e.getMessage());
            }
        }
        System.out.println("Saldo final CLI-001: " + banco.consultar("CLI-001"));
    }
}
