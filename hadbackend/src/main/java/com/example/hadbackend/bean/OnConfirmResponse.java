package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OnConfirmResponse {
    private String requestId;
    private String timestamp;
    private ConfirmAuth auth;
    private OnConfirmPatient patient;

    public String getRequestid(){
        return this.requestId;
    }

    public OnConfirmPatient getPatient(){
        return this.patient;
    }
}
