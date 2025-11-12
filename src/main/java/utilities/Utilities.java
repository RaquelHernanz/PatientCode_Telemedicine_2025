package utilities;

import pojos.Sex;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class Utilities {

    // ========= Entrada por consola =========
    private static final Scanner scanner = new Scanner(System.in);

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número entero.");
            }
        }
    }

    public static float readFloat(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Float.parseFloat(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número decimal válido.");
            }
        }
    }

    public static int readId(String prompt) {
        while (true) {
            int id = readInt(prompt);
            if (id > 0) return id;
            System.out.println("Error: El ID debe ser un número positivo.");
        }
    }

    // ========= Email =========
    // Mantiene la firma original: bloquea hasta que el email es válido y devuelve true.
    public static boolean readEmail(String prompt) {
        final String emailPattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        while (true) {
            System.out.print(prompt);
            String email = scanner.nextLine().trim();
            if (Pattern.matches(emailPattern, email)) return true;
            System.out.println("Error: Ingresa un email válido (ej: usuario@dominio.com).");
        }
    }

    // Variante que devuelve el email validado.
    public static String obtainEmail() {
        final String pattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        while (true) {
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            if (Pattern.matches(pattern, email)) return email;
            System.out.println("Invalid email. Try again.");
        }
    }

    // Validador “puro” (sin I/O) por si lo necesitas en lógica no interactiva.
    public static boolean validateEmail(String email) {
        if (email == null) return false;
        return email.trim().matches("^[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    // ========= Teléfono =========
    public static boolean validatePhone(String phone) {
        if (phone == null) return false;
        String cleaned = phone.replaceAll("[\\s-]", "");
        return cleaned.matches("^\\+?[0-9]{9,15}$");
    }

    // ========= Fecha de nacimiento (yyyy-MM-dd) =========
    // Versión “pura”: devuelve true/false (sin bucles infinitos).
    public static boolean validateDateOfBirth(String dobYYYYMMDD) {
        return dobYYYYMMDD != null && dobYYYYMMDD.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    // Variante interactiva: pide hasta que sea válido y devuelve el String.
    public static String obtainDate(String label) {
        final String datePattern = "\\d{4}-\\d{2}-\\d{2}";
        while (true) {
            System.out.print(label + " (yyyy-MM-dd): ");
            String dob = scanner.nextLine().trim();
            if (Pattern.matches(datePattern, dob)) return dob;
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    // ========= Sí/No y rango =========
    public static boolean obtainYesNo(String label) {
        while (true) {
            System.out.print(label + " (y/n): ");
            String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (s.equals("y")) return true;
            if (s.equals("n")) return false;
            System.out.println("Please enter y or n.");
        }
    }

    public static int obtainIntInRange(String label, int min, int max) {
        while (true) {
            System.out.print(label + " (" + min + "-" + max + "): ");
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException ignored) {}
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }

    // ========= Sex =========
    public static Sex readSex(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
        switch (s) {
            case "M": case "MALE":   return Sex.MALE;
            case "F": case "FEMALE": return Sex.FEMALE;
            default:                 return Sex.OTHER;
        }
    }

    public static String returnSexString(Sex sex) {
        if (sex == null) return "OTHER";
        switch (sex) {
            case MALE:   return "MALE";
            case FEMALE: return "FEMALE";
            default:     return "OTHER";
        }
    }

    // ========= Fecha/Hora ISO =========
    private static final DateTimeFormatter ISO_DT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter ISO_D  = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter ISO_T  = DateTimeFormatter.ISO_LOCAL_TIME;

    public static String formatDateTime(LocalDateTime dt) { return dt != null ? ISO_DT.format(dt) : null; }
    public static String formatDate(LocalDate d)         { return d  != null ? ISO_D.format(d)  : null; }
    public static String formatTime(LocalTime t)         { return t  != null ? ISO_T.format(t)  : null; }

    public static LocalDateTime parseDateTime(String s) {
        if (s == null || s.isEmpty()) return null;
        try { return LocalDateTime.parse(s, ISO_DT); }
        catch (DateTimeParseException e) { return null; }
    }
}
