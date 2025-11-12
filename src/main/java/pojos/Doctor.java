package pojos;

import utilities.Utilities;

import java.util.ArrayList;
import java.util.Objects;

public class Doctor {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String phonenumber;
    private ArrayList <Patient> patients;
    private ArrayList <Appointment> appointments;
    private ArrayList <String> messages;

    public Doctor (){}
    public Doctor(String name, String surname, String email, String phonenumber, ArrayList <Patient> patients, ArrayList <Appointment> appointments,  ArrayList <String> messages) {
        this.name = name;
        this.surname = surname;
        this.phonenumber = phonenumber;
        this.email = email;
        this.patients = patients;
        this.appointments = appointments;
        this.messages = messages;
    }

    public Doctor(int id, String name, String surname, String phonenumber,String email, ArrayList <Patient> patients, ArrayList <Appointment> appointments, ArrayList <String> messages) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phonenumber = phonenumber;
        this.email = email;
        this.patients = patients;
        this.appointments = appointments;
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

    public ArrayList <Patient> getPatients(){
        return patients;
    }

    public void setPatients(ArrayList <Patient> patients){
        this.patients = patients;
    }

    public ArrayList <Appointment> getAppointments(){
        return appointments;
    }

    public void setAppointments(ArrayList <Appointment> appointments){
        this.appointments = appointments;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public ArrayList <String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList <String> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "appointments=" + appointments +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", patients=" + patients +
                ", messages=" + messages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return id == doctor.id && Objects.equals(name, doctor.name) && Objects.equals(surname, doctor.surname) && Objects.equals(email, doctor.email) && Objects.equals(phonenumber, doctor.phonenumber) && Objects.equals(patients, doctor.patients) && Objects.equals(appointments, doctor.appointments) && Objects.equals(messages, doctor.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, phonenumber, patients, appointments, messages);
    }

    //Function that returns the names and the ids of the patients associated to the doctor
    public void showPatientsAndIds(){
        for (Object patientObj : patients) {
            Patient patient = (Patient) patientObj;
            System.out.println("ID: " + patient.getId() + ", Name and surname: " + patient.getName() + " " + patient.getSurname());
        }
    }

    //Function that searches for a patient associated to the doctor by its id
    public Patient searchPatientById(){
        this.showPatientsAndIds();
        int id = Utilities.readInt("Enter the ID of the patient: ");

        for (Object patientObj : patients) {
            Patient patient = (Patient) patientObj;
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null; // Return null if no patient with the given ID is found
    }

    public void viewPersonalInformation (){
        System.out.println(this.toString());
    }
}
