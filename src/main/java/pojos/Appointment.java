package pojos;

import java.time.LocalDateTime;
import java.util.Objects;

public class Appointment {
    private int id;
    private LocalDateTime date;
    private String message;
    private Doctor doctor;
    private Patient patient;

    public Appointment(LocalDateTime date, String message, Doctor doctor, Patient patient){
        this.date = date;
        this.message = message;
        this.doctor = doctor;
        this.patient = patient;
    }
    public Appointment(int id, LocalDateTime date, String message, Doctor doctor, Patient patient){
        this.id = id;
        this.date = date;
        this.message = message;
        this.doctor = doctor;
        this.patient = patient;
    }
    public Appointment(){

    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public Doctor getDoctor(){
        return doctor;
    }

    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
    }

    public Patient getPatient(){
        return patient;
    }

    public void setPatient(Patient patient){
        this.patient = patient;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "date=" + date +
                ", id=" + id +
                ", message='" + message + '\'' +
                ", doctor=" + doctor +
                ", patient=" + patient +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return id == that.id && Objects.equals(date, that.date) && Objects.equals(message, that.message) && Objects.equals(doctor, that.doctor) && Objects.equals(patient, that.patient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, message, doctor, patient);
    }
}
