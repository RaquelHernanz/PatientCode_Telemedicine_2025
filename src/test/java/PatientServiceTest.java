import connection.Connection;
import connection.PatientService;
import pojos.Doctor;
import pojos.Patient;
import pojos.Sex;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Pruebas de integración para PatientService.
 * Requiere que el Servidor esté corriendo en localhost:9000.
 * Utiliza un usuario de prueba fijo para simular el flujo completo.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientServiceTest {

    // Datos fijos de prueba (usamos timestamp para garantizar un email único)
    private static final String TEST_EMAIL = "test_paciente_" + System.currentTimeMillis() + "@telemedicina.com";
    private static final String TEST_PASSWORD = "testpassword123";
    private static int TEST_DOCTOR_ID = -1; // ID del primer doctor disponible en el servidor

    // Objeto paciente de prueba
    private static Patient testPatient;


    // --- 0. PREPARACIÓN DE CONEXIÓN ---

    @BeforeAll
    static void setup() {
        System.out.println("--- Starting Patient Service Tests ---");
        try {
            // Verifica que el cliente puede abrir un socket y establecer comunicación.
            Connection.connectToServer();
            assertTrue(Connection.isConnected(), "Debe poder conectar al servidor.");
        } catch (Exception e) {
            fail("ERROR: Servidor no accesible en localhost:9000. Asegúrate de que ServerMain esté corriendo. Excepción: " + e.getMessage());
        }

        // Carga un ID de doctor para las pruebas que lo requieren (citas, mensajes).
        try {
            List<Doctor> doctors = PatientService.listAllDoctors();
            if (!doctors.isEmpty()) {
                TEST_DOCTOR_ID = doctors.get(0).getId();
            } else {
                fail("ERROR: No hay doctores disponibles en la base de datos para realizar pruebas de citas/mensajes. Por favor, registre un doctor en el servidor.");
            }
        } catch (Exception e) {
            fail("ERROR al listar doctores: " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDown() {
        // Asegura que el socket se cierra al finalizar todas las pruebas.
        Connection.releaseResources();
        System.out.println("--- Tests Finished, connection closed ---");
    }

    // --- 1. REGISTRO (Prueba de conexión y protocolo más importante) ---

    @Test
    @Order(1)
    void testRegisterPatient_Success() throws IOException {
        // Comprueba que el cliente puede enviar el REQUEST "REGISTER_PATIENT" 
        // y que el servidor asigna y devuelve correctamente un ID.
        testPatient = new Patient(
                "TestName", "TestSurname", TEST_EMAIL, "+34666000111",
                Sex.FEMALE, "1990-01-01",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new Doctor(), new ArrayList<>()
        );

        System.out.println("\nTesting 1. Registration...");

        Patient registered = PatientService.registerPatient(testPatient, TEST_PASSWORD);

        assertNotNull(registered, "El registro debería devolver el objeto Patient.");
        assertTrue(registered.getId() > 0, "El servidor debe asignar un ID positivo.");

        // Asignamos el ID real para el resto de tests
        testPatient.setId(registered.getId());
    }

    // --- 2. LOGIN (Prueba de autenticación de protocolo) ---

    @Test
    @Order(2)
    void testLogin_Success() throws IOException {
        // Comprueba que el cliente puede enviar el REQUEST "LOGIN" con credenciales válidas 
        // y que el servidor confirma la autenticación y devuelve el ID correcto.
        System.out.println("\nTesting 2. Login...");

        Patient loggedIn = PatientService.login(TEST_EMAIL, TEST_PASSWORD);

        assertNotNull(loggedIn, "El login debería ser exitoso.");
        assertEquals(testPatient.getId(), loggedIn.getId(), "El ID devuelto debe coincidir con el ID registrado.");
    }

    @Test
    @Order(3)
    void testLogin_Failure_IncorrectPassword() {
        // Comprueba que el cliente lanza una IOException cuando el servidor rechaza el LOGIN
        // debido a una contraseña incorrecta (verificación de manejo de errores).
        System.out.println("\nTesting 3. Login Failure...");

        assertThrows(IOException.class, () -> {
            PatientService.login(TEST_EMAIL, "wrong_password");
        }, "El login con credenciales incorrectas debe lanzar IOException.");
    }

    // --- 3. ENVÍO DE DATOS (Verificación del protocolo SEND_*) ---

    @Test
    @Order(4)
    void testSendSymptoms_Success() throws IOException {
        // Comprueba el envío exitoso del REQUEST "SEND_SYMPTOMS" al servidor.
        System.out.println("\nTesting 4. Send Symptoms...");
        PatientService.setCurrentPatient(testPatient); // Asegurar sesión activa

        boolean success = PatientService.sendSymptoms("Testing severe chest pain and dizziness.");

        assertTrue(success, "El envío de síntomas debe ser exitoso.");
    }

    @Test
    @Order(5)
    void testRequestAppointment_Success() throws IOException {
        // Comprueba el envío exitoso del REQUEST "REQUEST_APPOINTMENT"
        // y que el servidor devuelve un ID de cita válido.
        System.out.println("\nTesting 5. Request Appointment...");

        int appointmentId = PatientService.requestAppointment(
                TEST_DOCTOR_ID,
                "2026-01-01T10:00:00",
                "Annual checkup test."
        );

        assertTrue(appointmentId > 0, "Se debe devolver un ID de cita positivo.");
    }

    @Test
    @Order(6)
    void testSendMessage_Success() throws IOException {
        // Comprueba el envío exitoso del REQUEST "SEND_MESSAGE" al doctor asignado.
        System.out.println("\nTesting 6. Send Message...");

        boolean success = PatientService.sendMessage(
                TEST_DOCTOR_ID,
                "Message sent from JUnit test."
        );

        assertTrue(success, "El envío de mensaje debe ser exitoso.");
    }

    // NOTA: El test de BITalino (recordAndSendMeasurement) se haría aquí, pero requiere hardware real.
}