package menu;

import utilities.Utilities;

import java.util.Scanner;

public class PatientMenu {

    private static Scanner scanner = new Scanner(System.in);
    
    private static void menu() {
        System.out.println("\nPatient App");
        while (true) {

            System.out.println("0. Exit");
            System.out.println("1. Register"); //si no tienes cuenta, te registras
            System.out.println("2. Log in"); //si tienes cuenta, entras con tu cuenta
            System.out.println("\nChoose an option:");

            int option = scanner.nextInt(); //leer opción
            scanner.nextLine();

            switch (option) {
                case 1:
                    registerPatient();
                    break;
                case 2:
                 //   logIn();
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
            System.out.println("Enter your personal details to register. ");

            String email;
            do {
                System.out.println("Email: "); //pide el email
                email = scanner.nextLine();
            } while (!Utilities.readEmail(email)); // si no es correcto, te lo vuelve a pedir

            System.out.println("Password: ");
            String password = scanner.nextLine();

            System.out.println("Name: ");
            String name = scanner.nextLine();

            System.out.println("Surname: ");
            String surname = scanner.nextLine();

            // Solicitar fecha de nacimiento
            String dob;
            do {
                System.out.print("Date of Birth (yyyy-MM-dd): ");
                dob = scanner.nextLine().trim();
                if (!Utilities.validateDateOfBirth(dob)) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                }
            } while (!Utilities.validateDateOfBirth(dob));

            //Patient new_patient = new Patient(name, surname, email, dob, new ArrayList<Appointment>(), new ArrayList<Measurement>(), new ArrayList<Symptoms>());

            // Crear objeto Patient con los nuevos datos
        /*  Patient currentPatient = new Patient(dni, password, name, surname, email, , gender, telephone, dateOfBirth);

            if (ConnectionPatient.sendRegisterServer(currentPatient, password)) {
                System.out.println("User registered with DNI: " + dni);
                mainMenu();
            } else {
                System.out.println("DNI: " + dni + " is already registered. Try to login to access your account.");
                mainMenu();
            }*/

        }

        private static void loginPatient(){

        }

        //Make an appointment
        //Write a set of symptons
        //Record a mesuarement OF EGC/EDA
        //Send a message
        //Select doctor


}
    
    


