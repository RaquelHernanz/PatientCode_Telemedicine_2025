package pojos;

import java.time.LocalDateTime;
import java.util.Objects;

public class Symptoms {
    private int id;
    private String description;
    private LocalDateTime dateTime;
    private Patient patient;

    public Symptoms(String description,LocalDateTime data_hour, Patient patient){
        this.description = description;
        this.dateTime = data_hour;
        this.patient = patient;
    }

    public Symptoms() {}

    public Symptoms(int id, String description, LocalDateTime date_hour, Patient patient){
        this.id = id;
        this.description = description;
        this.dateTime = date_hour;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Symptoms{" +
                "Date/Time=" + dateTime +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", patient=" + patient +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Symptoms symptoms = (Symptoms) o;
        return id == symptoms.id && Objects.equals(description, symptoms.description) && Objects.equals(dateTime, symptoms.dateTime) && Objects.equals(patient, symptoms.patient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, dateTime, patient);
    }
}
