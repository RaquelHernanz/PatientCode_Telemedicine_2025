package utilities;

import pojos.Sex;

import java.util.Scanner;
import java.util.regex.Pattern;


public class Utilities {

    private static final Scanner scanner = new Scanner(System.in);

    public static String readString(String string) {
        System.out.print(string);
        return scanner.nextLine();
    }

    public static Sex readSex (String string) {
        System.out.print(string);
        String Sex_string = scanner.nextLine();
        Sex_string = Sex_string.toUpperCase();
        if (Sex_string.equals("MALE")){
            return Sex.MALE;
        }else if (Sex_string.equals("FEMALE")){
            return Sex.FEMALE;
        }else{
            return Sex.OTHER;
        }
    }

    public static String returnSexString (Sex sex){
        if (sex.equals(Sex.MALE)) {
            return "MALE";
        }else if (sex.equals(Sex.FEMALE)) {
            return "FEMALE";
        }else {
            return "OTHER";
        }
    }

    public static int readInt(String integer) {
        int value;
        while (true) {
            System.out.print(integer);
            String input = scanner.nextLine();
            try {
                value = Integer.parseInt(input);
                break; // entrada válida
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número entero.");
            }
        }
        return value;
    }

    public static float readFloat(String floatIntroduced) {
        float value;
        while (true) {
            System.out.print(floatIntroduced);
            String input = scanner.nextLine().trim();
            try {
                value = Float.parseFloat(input);
                break; // entrada válida
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número decimal válido.");
            }
        }
        return value;
    }

    public static boolean readEmail(String emailIntroduced) {
        String emailPattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        while (true) {
            System.out.print(emailIntroduced);
            String email = scanner.nextLine().trim();
            if (Pattern.matches(emailPattern, email)) {
                return true;
            } else {
                System.out.println("Error: Ingresa un email válido (ej: usuario@dominio.com).");
            }
        }
    }

    public static int readId(String idIntroduced) {
        int id;
        while (true) {
            id = readInt(idIntroduced);
            if (id > 0) {
                return id;
            } else {
                System.out.println("Error: El ID debe ser un número positivo.");
            }
        }
    }

    public static boolean validateDateOfBirth(String dateOfBirthIntroduced) {
        // Regex para validar formato yyyy-MM-dd
        String datePattern = "\\d{4}-\\d{2}-\\d{2}";

        while (true) {
            if (Pattern.matches(datePattern, dateOfBirthIntroduced)) {
                return true; // formato válido
            } else {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }
    }

    public static String obtainEmail() {
        String pattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

        while (true) {
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();// .trim() elimina espacios en blanco al inicio y al final del texto introducido.

            if (Pattern.matches(pattern, email)) {
                return email;
            } else {
                System.out.println("Invalid email. Try again.");
            }
        }
    }

    public static String obtainDate(String label) {
        String datePattern = "\\d{4}-\\d{2}-\\d{2}";

        while (true) {
            System.out.print(label + " (yyyy-MM-dd): "); // imprime: Date of birth + formato
            // .trim() evita que espacios extra hagan fallar la validación
            String dob = scanner.nextLine().trim();

            if (Pattern.matches(datePattern, dob)) {
                return dob;
            } else {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }

    public static boolean obtainYesNo(String label) {
        while (true) {
            System.out.print(label + " (y/n): ");
            String s = scanner.nextLine().trim().toLowerCase();

            if (s.equals("y")) return true;
            if (s.equals("n")) return false;

            System.out.println("Please enter y or n.");
        }
    }//Solicita al usuario una respuesta de tipo Sí/No y devuelve un booleano.
    //Garantiza que solo se acepten valores válidos ('y' o 'n'), evitando entradas incorrectas.

    public static int obtainIntInRange(String label, int min, int max) {
        while (true) {
            System.out.print(label + " (" + min + "-" + max + "): ");
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {}

            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }//Obtiene un número introducido por el usuario y valida que esté dentro de un rango específico.
    //Se utiliza para valores de síntomas (por ejemplo, de 0 a 3) evitando errores en la entrada.


}
