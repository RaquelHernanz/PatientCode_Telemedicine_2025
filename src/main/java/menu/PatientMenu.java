package menu;

import BITalino.BITalino;
import BITalino.Frame;
import BITalino.BITalinoException;
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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import utilities.Utilities;

import javax.bluetooth.RemoteDevice;

public class PatientMenu {

    private static Scanner scanner = new Scanner(System.in);
    private static Patient currentPatient = null; // Quien ha iniciado sesión
    private static Doctor selectedDoctor = null;
    private static Frame[] frame;//Para recoger los datos del Bitalino

    public static void main(String[] args) {
        //Para probar el recordData
        /*Double[][] data= recordBitalinoEDA();
        for(int i=0;i< data[0].length;i++){
            System.out.println("Sample: "+data[0][i]+" Value: "+data[1][i]);
        }*/

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
        String dob = utilities.Utilities.obtainDate("Date of Birth: ");

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
            System.out.println("2. View messages with assigned doctor");//He añadido esto porque faltaba
            System.out.println("3. Send message to assigned doctor");
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
                case "2" -> viewMessagesWithDoctor();
                case "3" -> sendMessage();
                case "4" -> requestAppointment(); // <-- LLAMADA AL NUEVO MÉTODO
                case "5" -> recordECGorEDA();
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

    private static void viewMessagesWithDoctor() {
        try {
            if (currentPatient == null || selectedDoctor == null) {
                System.out.println("You must be logged in and have an assigned doctor.");
                return;
            }

            // Llamamos a PatientService para pedir la conversación
            java.util.List<String> msgs =
                    PatientService.listConversationWithDoctor(selectedDoctor.getId());

            if (msgs.isEmpty()) {
                System.out.println("No messages in this conversation yet.");
                return;
            }

            System.out.println("\n=== Conversation with Dr. " + selectedDoctor.getSurname() + " ===");
            for (String line : msgs) {
                System.out.println(line);
            }
            System.out.println("=== End of conversation ===");

        } catch (IOException e) {
            System.out.println("Error contacting server: " + e.getMessage());
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

    private static void recordECGorEDA() {
        System.out.println("Select 1 for ECG or 2 for EDA");
        //Se le pide al paciente que elija
        String input = scanner.nextLine().trim();
        //Si el input no es 1 o 2 se le vuelve a pedir
        while(input !="1" && input !="2"){
            System.out.println("Invalid option. Select 1 for ECG or 2 for EDA");
            input = scanner.nextLine().trim();
        }
        switch (input) {
            case "1": {
                System.out.println("Recording ECG");
                Double[][] ecgData= recordBitalinoECG();
                //Quedaría enviarlos datos
                break;
            }
            case "2":{
                System.out.println("Recording EDA");
                Double[][] edaData= recordBitalinoEDA();
                //Quedaría enviarlos datos
                break;
            }
            default:{
                System.out.println("Invalid option");
                break;
            }
        }
    }

    //Devuelve un array de Double con los datos recogidos del Bitalino ECG
    private static Double[][] recordBitalinoECG(){

        Double[][] data = new Double[2][10000]; //Array para almacenar los datos recogidos
                                                //La posición [0] es para el número de sample y la [1] para el valor recogido
        BITalino bitalino = null;
        try {
            bitalino = new BITalino();

            // Código para buscar dispositivos Bitalino cercanos
            Vector<RemoteDevice> devices = bitalino.findDevices();
            System.out.println(devices);

            //Mac Address del Bitalino
            String macAddress = "20:17:11:20:52:36";

            int SamplingRate = 1000;
            bitalino.open(macAddress, SamplingRate);

            // Empieza la adquisición en el canal analógico A2
            int[] channelsToAcquire = {1}; //A2 es el canal 1
            bitalino.start(channelsToAcquire);

            //Lee 10000 samples en total
            for (int j = 0; j < 1000; j++) {//Si ponemos el límite a 100, será 100x10=1000 muestras, ya que los bloques
                                            //son de 10 muestras.

                //Cada vez lee un bloque de 10 samples
                int block_size=10;
                frame = bitalino.read(block_size);

                //Imprime los samples
                /*for (int i = 0; i < frame.length; i++) {
                    System.out.println((j * block_size + i) + " seq: " + frame[i].seq + " "
                                    + frame[i].analog[0] + " "
                    );

                }*/
                //Almacena los datos en el array
                for (int i = 0; i < frame.length; i++) {
                    data[0][j * block_size + i] = (double) j * block_size + i;  //Número de sample. Es el número del ciclo multiplicado por el
                                                                                //tamaño del bloque más el índice dentro del bloque
                    data[1][j * block_size + i] = (double) frame[i].analog[0]; //Valor recogido

                }
            }
            //Para de adquirir datos
            bitalino.stop();
        } catch (BITalinoException ex) {
            Logger.getLogger(PatientMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(PatientMenu.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //Cierra la conexión bluetooth
                if (bitalino != null) {
                    bitalino.close();
                }
            } catch (BITalinoException ex) {
                Logger.getLogger(PatientMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return data;
    }

    //Devuelve un array de Double con los datos recogidos del Bitalino EDA
    private static Double[][] recordBitalinoEDA(){

        Double[][] data = new Double[2][10000]; //Array para almacenar los datos recogidos
        //La posición [0] es para el número de sample y la [1] para el valor recogido
        BITalino bitalino = null;
        try {
            bitalino = new BITalino();

            // Código para buscar dispositivos Bitalino cercanos
            Vector<RemoteDevice> devices = bitalino.findDevices();
            System.out.println(devices);

            //Mac Address del Bitalino
            String macAddress = "20:17:11:20:52:36";

            int SamplingRate = 1000;
            bitalino.open(macAddress, SamplingRate);

            // Empieza la adquisición en el canal analógico A3
            int[] channelsToAcquire = {1}; //A3 es el canal 2 de EDA
            bitalino.start(channelsToAcquire);

            //Lee 10000 samples en total
            for (int j = 0; j < 1000; j++) {//Si ponemos el límite a 100, será 100x10=1000 muestras, ya que los bloques
                //son de 10 muestras.

                //Cada vez lee un bloque de 10 samples
                int block_size=10;
                frame = bitalino.read(block_size);

                //Imprime los samples
                /*for (int i = 0; i < frame.length; i++) {
                    System.out.println((j * block_size + i) + " seq: " + frame[i].seq + " "
                            + frame[i].analog[0] + " "
                    );

                }*/
                //Almacena los datos en el array
                for (int i = 0; i < frame.length; i++) {
                    data[0][j * block_size + i] = (double) j * block_size + i;  //Número de sample. Es el número del ciclo multiplicado por el
                    //tamaño del bloque más el índice dentro del bloque
                    data[1][j * block_size + i] = (double) frame[i].analog[0]; //Valor recogido

                }
            }
            //Para de adquirir datos
            bitalino.stop();
        } catch (BITalinoException ex) {
            Logger.getLogger(PatientMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(PatientMenu.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //Cierra la conexión bluetooth
                if (bitalino != null) {
                    bitalino.close();
                }
            } catch (BITalinoException ex) {
                Logger.getLogger(PatientMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return data;
    }
}
