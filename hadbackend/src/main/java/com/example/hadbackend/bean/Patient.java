package com.example.hadbackend.bean;
import jakarta.persistence.*;

@Entity
@Table(name="Patient")
public class Patient {
    @Id
    @Column(name = "patient_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientID;

    @Column(name = "abha_number")
    private String abhaNumber;

    @Column(name = "abha_address")
    private String abhaAddress;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_year")
    private Integer yearOfBirth;

    @Column(name = "birth_month")
    private Integer monthOfBirth;

    @Column(name = "birth_day")
    private Integer dayOfBirth;

    @Column(name = "address")
    private String patientAddr;

    @Column(name = "phone_number")
    private String phoneNumber;

    public Patient(Integer patientID, String abhaNumber, String abhaAddress, String name, String gender, Integer yearOfBirth, Integer monthOfBirth, Integer dayOfBirth, String patientAddr, String phoneNumber) {
        this.patientID = patientID;
        this.abhaNumber = abhaNumber;
        this.abhaAddress = abhaAddress;
        this.name = name;
        this.gender = gender;
        this.yearOfBirth = yearOfBirth;
        this.monthOfBirth = monthOfBirth;
        this.dayOfBirth = dayOfBirth;
        this.patientAddr = patientAddr;
        this.phoneNumber = phoneNumber;
    }

    public Patient() {

    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public String getAbhaNumber() {
        return abhaNumber;
    }

    public void setAbhaNumber(String abhaNumber) {
        this.abhaNumber = abhaNumber;
    }

    public String getAbhaAddress() {
        return abhaAddress;
    }

    public void setAbhaAddress(String abhaAddress) {
        this.abhaAddress = abhaAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public Integer getMonthOfBirth() {
        return monthOfBirth;
    }

    public void setMonthOfBirth(Integer monthOfBirth) {
        this.monthOfBirth = monthOfBirth;
    }

    public Integer getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(Integer dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getPatientAddr() {
        return patientAddr;
    }

    public void setPatientAddr(String patientAddr) {
        this.patientAddr = patientAddr;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientID=" + patientID +
                ", abhaNumber='" + abhaNumber + '\'' +
                ", abhaAddress='" + abhaAddress + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                ", monthOfBirth=" + monthOfBirth +
                ", dayOfBirth=" + dayOfBirth +
                ", patientAddr='" + patientAddr + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}