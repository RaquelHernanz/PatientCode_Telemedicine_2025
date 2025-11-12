package pojos;

import java.util.ArrayList;
import java.util.Objects;

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
    private ArrayList<String> messages;

    public Patient (){}

    public Patient(String name, String surname, String email, String phonenumber,Sex sex,String dob, ArrayList <Appointment> appointments, ArrayList <Measurement> measurements, ArrayList <Symptoms> symptoms, Doctor doctor, ArrayList<String> messages) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dob = dob;
        this.sex = sex;
        this.phonenumber = phonenumber;
        this.appointments = appointments;
        this.measurements = measurements;
        this.symptoms = symptoms;
        this.doctor = doctor;
        this.messages = messages;
    }

    public Patient(int id, String name, String surname, String email, Sex sex, String phonenumber, String dob, ArrayList <Appointment> appointments, ArrayList <Measurement> measurements, ArrayList <Symptoms> symptoms,Doctor doctor, ArrayList <String> messages){
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
        this.messages = messages;
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

    public ArrayList <Appointment> getAppointments(){
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

    public ArrayList<Measurement> getMeasurements() {
        return measurements;
    }
    public ArrayList<Symptoms> getSymptoms() {
        return symptoms;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public void addAppointment(Appointment appointment){
        this.appointments.add(appointment);
    }

    public void addMeasurement(Measurement measurement){
        this.measurements.add(measurement);
    }

    public void addSymptom(Symptoms symptom){
        this.symptoms.add(symptom);
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
                ", messages=" + messages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return id == patient.id && Objects.equals(name, patient.name) && Objects.equals(surname, patient.surname) && Objects.equals(email, patient.email) && Objects.equals(doctor, patient.doctor) && Objects.equals(phonenumber, patient.phonenumber) && Objects.equals(dob, patient.dob) && sex == patient.sex && Objects.equals(appointments, patient.appointments) && Objects.equals(measurements, patient.measurements) && Objects.equals(symptoms, patient.symptoms) && Objects.equals(messages, patient.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, doctor, phonenumber, dob, sex, appointments, measurements, symptoms, messages);
    }
}
