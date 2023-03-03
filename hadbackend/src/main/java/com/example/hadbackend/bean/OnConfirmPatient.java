package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.ArrayList;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OnConfirmPatient {
    private String id;
    private String name;
    private String gender;
    private int yearOfBirth;
    private int monthOfBirth;
    private int dayOfBirth;
    private PatientAddress address;
    private ArrayList<PatientIdentifiers> identifiers;
//    private PatientIdentifiers identifier;

    public String getName(){
        return this.name;
    }
    public ArrayList<PatientIdentifiers> getIdentifiers(){
        return this.identifiers;
    }
//    public PatientIdentifiers getIdentifier(){
//        return this.identifier;
//    }

    public PatientAddress getAddress(){
        return this.address;
    }
}
