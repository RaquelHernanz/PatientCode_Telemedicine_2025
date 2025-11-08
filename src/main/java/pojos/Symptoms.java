package pojos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Symptoms {
    private int id;
    private String description;
    private LocalDate date;
    private LocalDateTime hour;
    private Patient patient;

    public Symptoms(String description, LocalDate date,LocalDateTime hour, Patient patient){
        this.description = description;
        this.date = date;
        this.hour = hour;
        this.patient = patient;
    }

    public Symptoms(int id, String description, LocalDate date,LocalDateTime hour, Patient patient){
        this.id = id;
        this.description = description;
        this.date = date;
        this.hour = hour;
        this.patient = patient;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Patient getPatient(){
        return patient;
    }

    public void setPatient(Patient patient){
        this.patient = patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getHour() {
        return hour;
    }

    public void setHour(LocalDateTime hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "Symptoms{" +
                "date=" + date +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", hour=" + hour +
                ", patient=" + patient +
                '}';
    }
}
