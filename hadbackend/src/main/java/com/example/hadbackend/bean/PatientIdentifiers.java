package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PatientIdentifiers {
    private String type;
    private String value;

    public String getValue(){
        return this.value;
    }
}
