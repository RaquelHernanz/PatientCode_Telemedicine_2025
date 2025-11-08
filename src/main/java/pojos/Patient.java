package pojos;

import java.util.ArrayList;

public class Patient {
    private int id;
    private String name;
    private String surname;
    private String email;
    private Doctor doctor;
    private String phonenumber;
    private String dob;
    private Sex sex;
    private ArrayList<Appointment> appointments;
    private ArrayList<Measurement>measurements;
    private ArrayList<Symptoms> symptoms;

    public Patient(String name, String surname, String email, Sex sex, String phonenumber,String dob, ArrayList appointments, ArrayList measurements, ArrayList symptoms){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dob = dob;
        this.sex = sex;
        this.phonenumber = phonenumber;
        this.appointments = appointments;
        this.measurements = measurements;
        this.symptoms = symptoms;
    }

    public Patient(int id, String name, String surname, String email, Sex sex, String phonenumber,Doctor doctor, String dob, ArrayList appointments, ArrayList measurements, ArrayList symptoms){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.doctor = doctor;
        this.dob = dob;
        this.sex = sex;
        this.phonenumber = phonenumber;
        this.appointments = appointments;
        this.measurements = measurements;
        this.symptoms = symptoms;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getSurname(){
        return surname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public Doctor getDoctor(){
        return doctor;
    }

    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
    }

    public String getDob(){return dob;}

    public void setDob(String dob){this.dob = dob;}

    public ArrayList getAppointments(){
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void setMeasurements(ArrayList<Measurement> measurements) {
        this.measurements = measurements;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setSymptoms(ArrayList<Symptoms> symptoms) {
        this.symptoms = symptoms;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "appointments=" + appointments +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", doctor=" + doctor +
                ", phonenumber='" + phonenumber + '\'' +
                ", dob='" + dob + '\'' +
                ", sex=" + sex +
                ", measurements=" + measurements +
                ", symptoms=" + symptoms +
                '}';
    }
}
