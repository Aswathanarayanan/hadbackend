package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ConfirmAuth {
    private String accessToken;
    private OnConfirmPatient patient;
    private ConfirmValidity validity;
    private String expiry;
    private int limit;

    public OnConfirmPatient getPatient(){
        return this.patient;
    }
}
