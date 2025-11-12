package menu;

import pojos.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import utilities.Utilities;

public class PatientMenu {

    private static Scanner scanner = new Scanner(System.in);
    private static String loggedEmail = null; // quién ha iniciado sesión
    private static java.util.Map<String,String> userPwdHash = new java.util.HashMap<>(); // email -> hash(password)
    // Últimos síntomas guardados
    private static String lastSymptomsDescription;
    private static java.time.LocalDateTime lastSymptomsDateTime;
    // Doctor seleccionado por el paciente (por ahora será un String, luego será Doctor)
    private static String selectedDoctor = null;
    // Lista de mensajes enviados por el paciente (en memoria por ahora)
    private static java.util.List<String> sentMessages = new java.util.ArrayList<>();

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

        // 1. pide el email, lo valida y lo guarda
        String email = Utilities.obtainEmail();

        // 2. Comprueba si el email ya existe y si ya existe le dice que haga log in en vez de register
        if (userPwdHash.containsKey(email)) {
            System.out.println("This email is already registered. Please log in.");
            return;
        }

        // 3. Password (de momento guardamos tal cual, sin encriptar)
        System.out.print("Password: ");
        String password = scanner.nextLine().trim(); //quita espacios en blanco al principio y al final

        // 4. Guardar credenciales temporalmente (email -> password)
        userPwdHash.put(email, password);
        // userPwdHash.put("ana@gmail.com", "1234"); --> formato (valor, clave)

        // 5. Nombre
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        // 6. Apellidos
        System.out.print("Surname: ");
        String surname = scanner.nextLine().trim();

        // 7. Fecha de nacimiento
        String dob = Utilities.obtainDate("Date of Birth");
        // Crear objeto Patient con los datos introducidos por el usuario
        /*Patient newPatient = new Patient(name,surname, email);*/


    // if (!connection.Connection.isConnected()) {
    //        connection.Connection.connectToServer();
    //    }


    // Enviar el registro al servidor
    /*if (ConnectionPatient.sendRegisterServer(newPatient, password)) {
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
        patientMenu();

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

    private static void patientMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Patient Menu ===");
            System.out.println("Logged as: " + loggedEmail);
            if (selectedDoctor != null) {
                System.out.println("Selected doctor: " + selectedDoctor);
            }

            System.out.println("0. Log out");
            System.out.println("1. Write symptoms");
            System.out.println("2. Select doctor");
            System.out.println("3. Send a message to selected doctor");
            System.out.println("4. Record ECG/EDA");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "0" -> {
                    System.out.println("Logging out.");
                    loggedEmail = null;
                    return; // volver al menú principal
                }
                case "1" -> writeSymptoms();
                case "2" -> selectDoctor();
                case "3" -> sendMessage();
                case "4" -> System.out.println("FALTA_METODO");// recordECGorEDA_BitalinoJavaSDK();

                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
    // Permite al paciente escribir síntomas en texto libre y los guarda temporalmente
    private static void writeSymptoms() {
        // Comprobamos que haya sesión iniciada
        if (loggedEmail == null) {
            System.out.println("You must log in first.");
            return;
        }

        System.out.println();
        System.out.println("Write symptoms");

        String symptoms;
        // Pedimos texto hasta que el usuario escriba algo (evitar vacío)
        do {
            System.out.print("Symptoms: ");
            symptoms = scanner.nextLine().trim();
            if (symptoms.isEmpty()) {
                System.out.println("Please enter some text.");
            }
        } while (symptoms.isEmpty());

        // Guardamos temporalmente (sin crear el objeto Symptoms aún)
        lastSymptomsDescription = symptoms;
        lastSymptomsDateTime = java.time.LocalDateTime.now();

        System.out.println("Symptoms saved successfully.");

        // FUTURO (cuando se cargue el Patient tras login):
        //
        // Patient p = currentPatient;  // variable que aún no tenemos
        // Symptoms s = new Symptoms(
        //         lastSymptomsDescription,
        //         lastSymptomsDate,
        //         lastSymptomsHour,
        //         p
        // );
        //
        // Después se enviará al servidor o BD según el proyecto.
    }

    // Permite al paciente seleccionar un doctor
    private static void selectDoctor() {
        if (loggedEmail == null) {
            System.out.println("You must log in first.");
            //Se podría hacer también lanzando una excepción
            return;
        }

        System.out.println();
        System.out.println("Select a doctor");

        // LISTA LOCAL TEMPORAL (más adelante se reemplazará por servidor)
        String[] doctors = {
                "Dr. Ana García - Cardiology",
                "Dr. Luis Pérez - Family Medicine",
                "Dr. Marta Ruiz - Cardiology"
        };

        // Mostrar lista numerada
        for (int i = 0; i < doctors.length; i++) {
            System.out.println((i + 1) + ". " + doctors[i]);
        }

        // Elegir opción válida
        int option;
        while (true) {
            System.out.print("Choose a doctor (1-" + doctors.length + "): ");
            String input = scanner.nextLine().trim();
            try {
                option = Integer.parseInt(input);
                if (option >= 1 && option <= doctors.length) break;
            } catch (NumberFormatException ignored) {}
            System.out.println("Invalid option. Try again.");
        }

        // Guardamos el doctor seleccionado
        selectedDoctor = doctors[option - 1];
        System.out.println("Doctor selected: " + selectedDoctor);

        // FUTURO (cuando haya servidor y clase Doctor):
        //
        // ArrayList<Doctor> doctors = ConnectionPatient.getAvailableDoctors();
        // Doctor selected = doctors.get(option - 1);
        // currentPatient.setDoctor(selected);
    }

    // Permite que el paciente envíe un mensaje a su doctor seleccionado
    private static void sendMessage() {
        // Comprobar login
        if (loggedEmail == null) {
            System.out.println("You must log in first.");
            return;
        }

        // Comprobar que haya doctor seleccionado
        if (selectedDoctor == null) {
            System.out.println("You must select a doctor first.");
            return;
        }

        System.out.println();
        System.out.println("Send a message to your doctor");

        String message;
        do {
            System.out.print("Message: ");
            message = scanner.nextLine().trim();
            if (message.isEmpty()) {
                System.out.println("Please enter a message.");
            }
        } while (message.isEmpty());

        // Crear mensaje con datos relevantes
        String fullMessage =
                "To: " + selectedDoctor + "\n" +
                        "Message: " + message + "\n" +
                        "Date: " + java.time.LocalDate.now() + " " + java.time.LocalTime.now() + "\n";

        // Guardar en la lista local
        sentMessages.add(fullMessage);

        System.out.println("Message sent successfully.");

        // FUTURO (cuando haya servidor):
        //
        // Message m = new Message(currentPatient, selectedDoctorObj, message, timestamp);
        // ConnectionPatient.sendMessage(m);

        //SI NO QUEREMOS CREAR LA CLASE MESSAGE:
        // FUTURE (when connecting to the server):
        // Build a string with the message to send:
        // String msgToSend =
        //         "SENDER: " + loggedEmail + "\n" +
        //         "DOCTOR: " + selectedDoctor + "\n" +
        //         "DATE: " + java.time.LocalDate.now() + "\n" +
        //         "TIME: " + java.time.LocalTime.now() + "\n" +
        //         "MESSAGE: " + message;
        // Send this text to the server instead of an object
        // ConnectionPatient.sendMessage(msgToSend);
    }

    private static void patientMenu1(Patient patient) throws Exception{
        System.out.println("\n=== Patient Menu ===");
        System.out.println("1. Write symptoms 2. Select doctor 3. Send message  0. Log out");

        do {
            int option_menu = scanner.nextInt(); //leer opción
            scanner.nextLine();
            /*case "0" -> {
                System.out.println("Logging out.");
                loggedEmail = null;
                return; // volver al menú principal
            }
            case "1" -> writeSymptoms();
            case "2" -> selectDoctor();
            case "3" -> sendMessage();
            case "4" -> System.out.println("FALTA_METODO");// recordECGorEDA_BitalinoJavaSDK();

            default -> System.out.println("Invalid option. Try again.");*/
            switch (option_menu) {
                case 1://Write symptoms
                    writeSymptoms1(patient);
                    break;
                case 2://Select doctor
                    selectDoctor();
                    break;
                case 3://Send message
                    sendMessage();
                    break;

                case 0://Log out
                    //ConnectionDoctor.closeConnection();
                    System.out.println("Logging out...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }while (true);
    }

    private static void writeSymptoms1(Patient patient) throws Exception{ //Se pasa el paciente al method para añadir los síntomas
        // Comprobamos que haya sesión iniciada
        if (loggedEmail == null) {
            throw new Exception("You must log in first.");//Envía una excepción si no está iniciada la sesión para
            //que luego se gestione con try catch
        }

        System.out.println();
        System.out.println("Write symptoms");

        String symptoms;
        // Pedimos texto hasta que el usuario escriba algo (evitar vacío)
        do {
            System.out.print("Symptoms: ");
            symptoms = scanner.nextLine().trim();
            if (symptoms.isEmpty()) {
                System.out.println("Please enter some text.");
            }
        } while (symptoms.isEmpty());

        // Guardamos temporalmente (sin crear el objeto Symptoms aún)
        lastSymptomsDescription = symptoms;
        lastSymptomsDateTime = java.time.LocalDateTime.now();



        Symptoms s = new Symptoms(lastSymptomsDescription,lastSymptomsDateTime,
                 patient
         );
        patient.addSymptom(s); //Añade los síntomas a la lista de síntomas del paciente
        // Después se enviará al servidor o BD según el proyecto.
        System.out.println("Symptoms saved successfully.");

    }

}
