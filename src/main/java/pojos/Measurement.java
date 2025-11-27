package pojos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Measurement {
    private int id;
    private Type type;
    private List<Integer> values;
    private LocalDateTime date;// No puede ser string, localdate para db
    private Patient patient;

    public Measurement(Type type, List<Integer> values, LocalDateTime date, Patient patient){
        this.type = type;
        this.values = values;
        this.date = date;
        this.patient = patient;
    }

    public Measurement(){}

    public Measurement(int id, Type type, List<Integer> values, LocalDateTime date, Patient patient){
        this.id = id;
        this.type = type;
        this.values = values;
        this.date = date;
        this.patient = patient;
    }
    public enum Type{
        ECG, EDA
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "date=" + date +
                ", id=" + id +
                ", type=" + type +
                ", values=" + values +
                ", patient=" + patient +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return id == that.id && type == that.type && Objects.equals(values, that.values) && Objects.equals(date, that.date) && Objects.equals(patient, that.patient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, values, date, patient);
    }
}
