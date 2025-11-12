package pojos;

import java.util.ArrayList;

public class Doctor {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String phonenumber;
    private ArrayList <Patient> patients;
    private ArrayList <Appointment> appointments;

    public Doctor (){}

    public Doctor(String name, String surname, String email, String phonenumber, ArrayList patients, ArrayList appointments){
        this.name = name;
        this.surname = surname;
        this.phonenumber = phonenumber;
        this.email = email;
        this.patients = patients;
        this.appointments = appointments;
    }

    public Doctor(int id, String name, String surname, String phonenumber,String email, ArrayList patients, ArrayList appointments){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phonenumber = phonenumber;
        this.email = email;
        this.patients = patients;
        this.appointments = appointments;
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

    public ArrayList getPatients(){
        return patients;
    }

    public void setPatients(ArrayList patients){
        this.patients = patients;
    }

    public ArrayList getAppointments(){
        return appointments;
    }

    public void setAppointments(ArrayList appointments){
        this.appointments = appointments;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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
                '}';
    }
}
