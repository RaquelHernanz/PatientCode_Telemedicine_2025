package pojos;

import java.time.LocalDateTime;

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
}
