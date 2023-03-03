package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OnConfirmPatient {
    private String id;
    private String name;
    private String gender;
    private int yearOfBirth;
    private int monthOfBirth;
    private int dayOfBirth;
    private PatientAddress address;
    private PatientIdentifiers identifiers;
    private Err error;
    private Resp resp;

    public String getName(){
        return this.name;
    }
    public PatientIdentifiers getIdentifiers(){
        return this.identifiers;
    }
    public PatientAddress getAddress(){
        return this.address;
    }
}
