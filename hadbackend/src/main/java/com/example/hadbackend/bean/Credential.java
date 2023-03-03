package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Credential {
    //private Demographic demographic;
    private String authCode;

    public void setAuthCode(String authCode){
        this.authCode=authCode;
    }
}
