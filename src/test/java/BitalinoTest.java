import BITalino.BITalino;
import BITalino.BITalinoException;
import BITalino.Frame;

import connection.Connection;
import connection.PatientService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import pojos.Doctor;
import pojos.Patient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BitalinoTest {
    private static final String PATIENT_EMAIL = "patienttest@gmail.com";
    private static final String PATIENT_PASSWORD = "Patient";

    private static final String DOCTOR_EMAIL = "doctortrial@gmail.com";
    private static int DOCTOR_ID = -1;

    @BeforeAll
    static void setup() throws IOException {

        System.out.println("---- Iniciando test BITalino -> Doctor específico ----");

        // 1) Conexión al servidor
        Connection.connectToServer();
        assertTrue(Connection.isConnected(),
                "No se ha podido conectar al servidor. ¿Está corriendo en localhost:9000?");

        // 2) Login del paciente ya existente
        System.out.println("Haciendo login del paciente: " + PATIENT_EMAIL);

        Patient logged = PatientService.login(PATIENT_EMAIL, PATIENT_PASSWORD);
        assertNotNull(logged,
                "El login del paciente ha fallado: revisa email/contraseña o base de datos.");
        assertTrue(logged.getId() > 0,
                "El paciente debe tener un ID válido.");

        // 3) Obtener al doctor específico de la lista de doctores del servidor
        System.out.println("Buscando doctor: " + DOCTOR_EMAIL);

        List<Doctor> doctors = PatientService.listAllDoctors();
        assertNotNull(doctors, "Error obteniendo lista de doctores.");
        assertFalse(doctors.isEmpty(), "No hay doctores registrados.");

        Doctor target = doctors.stream()
                .filter(d -> DOCTOR_EMAIL.equalsIgnoreCase(d.getEmail()))
                .findFirst()
                .orElse(null);

        assertNotNull(target,
                "No se encontró el doctor con email " + DOCTOR_EMAIL + " en la base de datos.");

        DOCTOR_ID = target.getId();
        System.out.println("Doctor encontrado con ID = " + DOCTOR_ID);
    }

    @Test
    void sendBitalinoMeasurementToSpecificDoctor() {

        assertNotEquals(-1, DOCTOR_ID,
                "El ID del doctor no se pudo cargar en el setup.");
        assertNotNull(PatientService.getCurrentPatient(),
                "El paciente debe estar logueado antes de enviar las mediciones.");

        // ====== CONFIGURACIÓN DEL BITALINO ======
        String macAddress = "20:17:11:20:52:36";  // <-- pon aquí la MAC REAL del BITalino
        int samplingRate = 1000;                 // 10, 100 o 1000 Hz
        int secondsToAcquire = 3;                // duración de prueba
        int blockSize = 10;                      // muestras por bloque

        BITalino device = null;
        List<Integer> values = new ArrayList<>();

        try {
            System.out.println("Abriendo BITalino...");
            device = new BITalino();                 // puede lanzar Throwable
            device.open(macAddress, samplingRate);   // idem

            int[] channels = {1};                    // ECG normalmente en A1
            device.start(channels);                  // idem

            System.out.println("Leyendo datos del BITalino...");
            int totalBlocks = (samplingRate * secondsToAcquire) / blockSize;

            for (int j = 0; j < totalBlocks; j++) {
                Frame[] frames = device.read(blockSize);  // puede lanzar BITalinoException
                for (Frame f : frames) {
                    values.add(f.analog[0]);
                }
            }

            device.stop();                            // puede lanzar BITalinoException

            assertFalse(values.isEmpty(),
                    "No se capturaron valores del BITalino. ¿Está conectado y emparejado?");

            // ======= Envío de mediciones al doctor específico =======
            System.out.println("Enviando mediciones al doctor " + DOCTOR_EMAIL);
            boolean ok = PatientService.sendMeasurements(DOCTOR_ID, values, "ECG");
            assertTrue(ok, "Error enviando las mediciones al doctor.");

        } catch (Throwable t) {
            // Cubre BITalinoException, IOException o cualquier otra checked rara
            fail("Error durante la adquisición/envío BITalino: " + t);
        } finally {
            if (device != null) {
                try {
                    device.close();
                } catch (Throwable ignored) {
                }
            }
        }
    }



}
