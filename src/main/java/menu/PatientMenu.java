package menu;

import pojos.*;
import connection.PatientService;
import connection.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.util.List;

import utilities.Utilities;

public class PatientMenu {

    private static Scanner scanner = new Scanner(System.in);
    private static Patient currentPatient = null; // Quien ha iniciado sesión
    private static Doctor selectedDoctor = null;

    public static void main(String[] args) {
        menu();
    }

    private static void menu() {
        System.out.println("\nPatient App");
        while (true) {

            System.out.println("0. Exit");
            System.out.println("1. Register"); //si no tienes cuenta, te registras
            System.out.println("2. Log in"); //si tienes cuenta, entras con tu cuenta
            System.out.print("\nChoose an option: ");

            String input = scanner.nextLine();   // leemos como texto
            int option;

            try {
                option = Integer.parseInt(input); // convertimos a número
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue; // vuelve al menú sin romper nada
            }

            switch (option) {
                case 1:
                    registerPatient();
                    break;
                case 2:
                    login();
                    break;
                case 0:
                    System.out.println("Exit");
                    return;
                default:
                    System.out.println("Error. Invalid option, please choose another option.");
            }
        }
    }

    // EN menu/PatientMenu.java (Reemplazar el método registerPatient existente)

    private static void registerPatient() {
        System.out.println();
        System.out.println("--- Patient Registration ---");

        // 1. Recopilar datos (la lógica de validación se mueve a PatientService/Server)
        String email = utilities.Utilities.obtainEmail();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        String name = utilities.Utilities.readString("Name: ");
        String surname = utilities.Utilities.readString("Surname: ");
        String phone = utilities.Utilities.readString("Phone Number: ");
        pojos.Sex sex = utilities.Utilities.readSex("Sex (M/F/O): ");
        String dob = utilities.Utilities.obtainDate("Date of Birth");

        // 2. Seleccionar Doctor (Requisito: seleccionar al registrar)
        Doctor doctor = selectDoctor();
        if (doctor == null) {
            System.out.println("Registration aborted. Cannot proceed without a doctor.");
            return;
        }

        // 3. Crear el POJO Patient
        Patient newPatient = new Patient(name, surname, email, phone, sex, dob,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), doctor, new ArrayList<>());

        // 4. Enviar el registro al servidor (USANDO PatientService)
        try {
            Patient p = PatientService.registerPatient(newPatient, password);
            currentPatient = p; // Marcar como logueado
            selectedDoctor = doctor; // Asignar el doctor seleccionado
            System.out.println("Registration successful. ID: " + p.getId());
            patientMenu(); // Ir al menú interno

        } catch (IOException e) {
            System.out.println("ERROR: Could not register with server. " + e.getMessage());
            Connection.releaseResources();
        }
    }
    // EN menu/PatientMenu.java (Reemplazar el método login existente)

    private static void login() {
        System.out.println();
        System.out.println("--- Log in ---");

        String email = utilities.Utilities.obtainEmail();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        // Lógica para establecer una conexión inicial
        try {
            Patient p = PatientService.login(email, password);
            currentPatient = p; // Marcar como logueado

            // Asumiendo que el PatientService no devuelve el Doctor asignado:
            // El paciente necesita seleccionar un doctor después del login si no tiene uno asignado.
            if (currentPatient != null && currentPatient.getDoctor() == null) {
                System.out.println("\nYou must select a supervising doctor now.");
                selectedDoctor = selectDoctor();
            } else {
                // Si el login fue exitoso, el paciente ya debería tener un doctor asociado.
                // Aquí deberías cargar la información completa del doctor del paciente desde el servidor,
                // pero por simplicidad, asumiremos que si p!=null, el login funciona.
                selectedDoctor = currentPatient.getDoctor();
            }

            System.out.println("Login successful.");
            patientMenu();

        } catch (IOException e) {
            System.out.println("ERROR: Login failed. " + e.getMessage());
            Connection.releaseResources();
        }
    }

    private static void patientMenu() {
        while (true) {
            if (currentPatient == null) return;

            System.out.println();
            System.out.println("=== Patient Menu ===");
            // Mostrar ID y Email
            System.out.println("Logged as: " + currentPatient.getEmail() + " (ID: " + currentPatient.getId() + ")");

            // Mostrar doctor asignado
            if (selectedDoctor != null) {
                System.out.println("Assigned Doctor: Dr. " + selectedDoctor.getSurname());
            }

            System.out.println("\n0. Log out");
            System.out.println("1. Send symptoms report");
            System.out.println("2. Send message to assigned doctor");
            System.out.println("3. Request appointment");
            System.out.println("4. Record ECG/EDA (Bitalino) [TO DO]");

            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "0" -> {
                    System.out.println("Logging out.");
                    currentPatient = null;
                    selectedDoctor = null;
                    Connection.releaseResources();
                    return;
                }
                case "1" -> sendSymptoms(); // <-- CAMBIO DE NOMBRE: writeSymptoms -> sendSymptoms
                case "2" -> sendMessage();
                case "3" -> requestAppointment(); // <-- LLAMADA AL NUEVO MÉTODO
                case "4" -> System.out.println("LÓGICA BITÁLINO PENDIENTE."); // recordECGorEDA();
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void sendSymptoms() {
        if (currentPatient == null) {
            System.out.println("You must log in first.");
            return;
        }

        System.out.println();
        System.out.println("--- Send Symptoms Report ---");

        String description;
        do {
            System.out.print("Describe your current symptoms (e.g., chest pain, dizziness): ");
            description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println("Please enter some text.");
            }
        } while (description.isEmpty());

        // Llamada al servicio
        try {
            boolean ok = PatientService.sendSymptoms(description);
            if (ok) {
                System.out.println("Symptoms successfully sent and stored by the server.");
            }
        } catch (IOException e) {
            System.out.println("ERROR sending symptoms to server: " + e.getMessage());
            Connection.releaseResources();
        }
    }

    /**
     * Muestra la lista de doctores disponibles y permite al paciente seleccionar uno.
     * Se usa tanto en el registro como desde el menú interno.
     * @return El Doctor seleccionado, o null si la lista está vacía o hay error.
     */
    private static Doctor selectDoctor() {
        System.out.println();
        System.out.println("--- Select Doctor ---");

        try {
            List<Doctor> doctors = PatientService.listAllDoctors();

            if (doctors.isEmpty()) {
                System.out.println("No doctors available for assignment. Try again later.");
                return null;
            }

            // 1. Mostrar lista numerada
            System.out.println("Available Doctors:");
            for (int i = 0; i < doctors.size(); i++) {
                Doctor d = doctors.get(i);
                System.out.printf(" %d. Dr. %s %s (%s) \n", (i + 1), d.getName(), d.getSurname(), d.getEmail());
            }

            // 2. Elegir opción válida
            int option;
            while (true) {
                System.out.print("Choose a doctor (1-" + doctors.size() + "): ");
                String input = scanner.nextLine().trim();
                try {
                    option = Integer.parseInt(input);
                    if (option >= 1 && option <= doctors.size()) break;
                } catch (NumberFormatException ignored) {}
                System.out.println("Invalid option. Try again.");
            }

            Doctor selected = doctors.get(option - 1);
            System.out.println("Doctor selected: Dr. " + selected.getSurname());
            return selected;

        } catch (IOException e) {
            System.out.println("ERROR communicating with server: Could not load doctor list. " + e.getMessage());
            Connection.releaseResources();
            return null;
        }
    }

    private static void sendMessage() {
        if (currentPatient == null) {
            System.out.println("You must log in first.");
            return;
        }

        if (selectedDoctor == null) {
            System.out.println("You must have an assigned doctor.");
            return;
        }

        System.out.println();
        System.out.println("--- Send Message to Dr. " + selectedDoctor.getSurname() + " ---");

        String message;
        do {
            System.out.print("Message: ");
            message = scanner.nextLine().trim();
            if (message.isEmpty()) {
                System.out.println("Please enter a message.");
            }
        } while (message.isEmpty());

        // Llamada al servicio
        try {
            PatientService.sendMessage(selectedDoctor.getId(), message);
            System.out.println("Message sent successfully.");
        } catch (IOException e) {
            System.out.println("ERROR sending message: " + e.getMessage());
            Connection.releaseResources();
        }
    }

    /**
     * Permite al paciente solicitar una cita con su doctor asignado.
     */
    private static void requestAppointment() {
        if (currentPatient == null) {
            System.out.println("You must log in first.");
            return;
        }
        if (selectedDoctor == null) {
            System.out.println("You must have an assigned doctor.");
            return;
        }

        System.out.println();
        System.out.println("--- Request Appointment with Dr. " + selectedDoctor.getSurname() + " ---");

        // Recoger Fecha y Hora
        String date = utilities.Utilities.obtainDate("Appointment Date");
        String time = utilities.Utilities.readString("Appointment Time (HH:mm): ");

        // Formato requerido por el servidor: YYYY-MM-DDTHH:mm:ss
        String datetimeIso = date + "T" + time + ":00";

        String message = utilities.Utilities.readString("Reason for the appointment: ");

        // Llamada al servicio
        try {
            int appointmentId = PatientService.requestAppointment(selectedDoctor.getId(), datetimeIso, message);
            System.out.println("Appointment requested successfully. ID: " + appointmentId);
        } catch (IOException e) {
            System.out.println("ERROR requesting appointment: " + e.getMessage());
            Connection.releaseResources();
        }
    }
}
