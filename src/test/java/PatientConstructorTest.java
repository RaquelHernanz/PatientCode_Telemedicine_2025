
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import pojos.Patient;
import pojos.Doctor;
import pojos.Sex;
import pojos.Appointment;
import pojos.Measurement;
import pojos.Symptoms;

import java.util.ArrayList;

public class PatientConstructorTest {

    @Test
    void testPatientConstructorWithoutId_assignsFieldsCorrectly() {
        // Arrange
        String name = "Ana";
        String surname = "García";
        String email = "ana@example.com";
        String dob = "1990-01-01";
        Sex sex = Sex.FEMALE;
        String phone = "600123123";

        ArrayList<Appointment> appointments = new ArrayList<>();
        ArrayList<Measurement> measurements = new ArrayList<>();
        ArrayList<Symptoms> symptoms = new ArrayList<>();
        ArrayList<String> messages = new ArrayList<>();

        Doctor doctor = new Doctor();

        // OJO: usamos el ORDEN REAL del constructor:
        // (name, surname, email, phonenumber, sex, dob, ...)
        Patient patient = new Patient(
                name,
                surname,
                email,
                phone,
                sex,
                dob,
                appointments,
                measurements,
                symptoms,
                doctor,
                messages
        );

        // Assert
        assertEquals(name, patient.getName());
        assertEquals(surname, patient.getSurname());
        assertEquals(email, patient.getEmail());
        assertEquals(dob, patient.getDob());
        assertEquals(sex, patient.getSex());
        assertEquals(phone, patient.getPhonenumber());

        assertSame(doctor, patient.getDoctor());
        assertSame(appointments, patient.getAppointments());
        assertSame(measurements, patient.getMeasurements());
        assertSame(symptoms, patient.getSymptoms());
        assertSame(messages, patient.getMessages());
    }

    @Test
    void testPatientConstructorWithId_assignsFieldsCorrectly() {
        // Arrange
        int id = 42;
        String name = "Luis";
        String surname = "Pérez";
        String email = "luis@example.com";
        String dob = "1985-05-05";
        Sex sex = Sex.MALE;
        String phone = "699999999";

        ArrayList<Appointment> appointments = new ArrayList<>();
        ArrayList<Measurement> measurements = new ArrayList<>();
        ArrayList<Symptoms> symptoms = new ArrayList<>();
        ArrayList<String> messages = new ArrayList<>();

        Doctor doctor = new Doctor();

        // ORDEN REAL del constructor con id:
        // (id, name, surname, email, sex, phonenumber, dob, ...)
        Patient patient = new Patient(
                id,
                name,
                surname,
                email,
                sex,
                phone,
                dob,
                appointments,
                measurements,
                symptoms,
                doctor,
                messages
        );

        // Assert
        assertEquals(id, patient.getId());
        assertEquals(name, patient.getName());
        assertEquals(surname, patient.getSurname());
        assertEquals(email, patient.getEmail());
        assertEquals(dob, patient.getDob());
        assertEquals(sex, patient.getSex());
        assertEquals(phone, patient.getPhonenumber());

        assertSame(doctor, patient.getDoctor());
        assertSame(appointments, patient.getAppointments());
        assertSame(measurements, patient.getMeasurements());
        assertSame(symptoms, patient.getSymptoms());
        assertSame(messages, patient.getMessages());
    }
}
