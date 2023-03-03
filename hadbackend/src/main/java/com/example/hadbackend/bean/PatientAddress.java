package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PatientAddress {
    private String line;
    private String district;
    private String state;
    private Object pincode;

    public String getState(){
        return this.state;
    }
}
