package pojos;

import java.time.LocalDateTime;
import java.util.List;

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
}
