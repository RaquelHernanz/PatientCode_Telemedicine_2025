package menu;

import pojos.Appointment;
import pojos.Measurement;
import pojos.Patient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import pojos.Symptoms;
import utilities.Utilities;

public class PatientMenu {

    private static Scanner scanner = new Scanner(System.in);
    private static String loggedEmail = null; // quién ha iniciado sesión
    private static java.util.Map<String,String> userPwdHash = new java.util.HashMap<>(); // email -> hash(password)

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

    private static void registerPatient() {
        System.out.println();
        System.out.println("Register");

        // 1. Email (valida y devuelve el email correcto)
        String email = Utilities.obtainEmail();

        // 2. Comprobar si ya existe
        if (userPwdHash.containsKey(email)) {
            System.out.println("This email is already registered. Please log in.");
            return;
        }

        // 3. Password (de momento guardamos tal cual, sin encriptar)
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        // 4. Guardar credenciales temporalmente (email -> password)
        userPwdHash.put(email, password);

        // 5. Nombre
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        // 6. Apellidos
        System.out.print("Surname: ");
        String surname = scanner.nextLine().trim();

        // 7. Fecha de nacimiento (usa tu método que ya funciona)
        String dob = Utilities.obtainDate("Date of Birth");
         /*
    // Crear objeto Patient con los datos introducidos por el usuario
    Patient newPatient = new Patient(name, surname, email, dob,
            new ArrayList<Appointment>(), new ArrayList<Measurement>(), new ArrayList<Symptoms>());

    // Enviar el registro al servidor
    if (ConnectionPatient.sendRegisterServer(newPatient, password)) {
        System.out.println("Usuario registrado correctamente en el servidor.");
    } else {
        System.out.println("Este email ya existe en el servidor. Intenta iniciar sesión.");
    }
    */

        System.out.println("Registration completed successfully.");
    }

    private static void login() {
        System.out.println();
        System.out.println("Log in");

        // 1) Pedir email válido
        String email = Utilities.obtainEmail();

        // 2) Pedir contraseña
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        // 3) Comprobar en el almacenamiento temporal (HashMap)
        String saved = userPwdHash.get(email);
        if (saved == null) {
            System.out.println("No account found with this email. Please register first.");
            return;
        }
        if (!saved.equals(password)) {
            System.out.println("Incorrect password. Try again.");
            return;
        }

        // 4) Marcar sesión iniciada
        loggedEmail = email;
        System.out.println("Login successful.");

        // ESTE CÓDIGO SE UTILIZARÁ MÁS ADELANTE CUANDO SE CONECTE CON EL SERVIDOR
    /*
    try {
        if (ConnectionPatient.validateLogin(email, password)) {
            System.out.println("Login successful (server).");
            // Cuando esté el menú interno del paciente:
            // patientMenu();
        } else {
            System.out.println("Incorrect credentials on the server.");
        }
    } catch (Exception e) {
        System.out.println("ERROR connecting with the server. Please try again later.");
    }
    */
    }


    //Make an appointment
    private static void scheduleAppointment() throws Exception{
        //
        //		//ask for date of appointment
        String dob;
        do {
            System.out.print("Date of Birth (yyyy-MM-dd): ");
            dob = scanner.nextLine().trim();
            if (!Utilities.validateDateOfBirth(dob)) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        } while (!Utilities.validateDateOfBirth(dob));
        //		//description
        //		System.out.println("Description: ");
        //		String description = reader.readLine();
        //		//doctor
        //		doctors = doctormanager.getListOfDoctors();
        //		System.out.println("Doctors available: ");
        //		for(Doctor d : doctors) {
        //			System.out.println(d.toString());
        //		}
        //		System.out.println("Enter doctor's id: ");
        //		Integer d_id = Integer.parseInt(reader.readLine());
        //		d = doctormanager.searchDoctorById(d_id);
        //		//patient
        //		p = patientmanager.getPatientByEmail(email);
        //
        //		Appointment a = new Appointment(date, description, d, p);
        //		amanager.addAppointment(a);
    }
    //

    //Write a set of symptons
    //Record a mesuarement OF EGC/EDA
    //Send a message
    //Select doctor


}
