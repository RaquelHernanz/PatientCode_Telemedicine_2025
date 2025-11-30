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

    // Devuelve el email validado.
    public static String obtainEmail() {
        final String pattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        while (true) {
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            if (Pattern.matches(pattern, email)) return email;
            System.out.println("Invalid email. Try again.");
        }
    }

    // Pide hasta que sea válido y devuelve el String.
    public static String obtainDate(String label) {
        final String datePattern = "\\d{4}-\\d{2}-\\d{2}";
        while (true) {
            System.out.print(label + " (yyyy-MM-dd): ");
            String dob = scanner.nextLine().trim();
            if (Pattern.matches(datePattern, dob)) return dob;
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    //Validación del teléfono
    public static boolean validatePhone(String phone) {
        if (phone == null) return false;
        String cleaned = phone.replaceAll("[\\s-]", "");
        return cleaned.matches("^\\+?[0-9]{9,15}$");
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

    public static boolean validateDateTime(String date, String time) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return false;
        }
        try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            return false;
        }
        return true;

    }

}
