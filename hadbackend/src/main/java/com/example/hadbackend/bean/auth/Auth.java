package com.example.hadbackend.bean.auth;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Auth {
    private String purpose;
    private List<String> modes;

    public String getPurpose(){
        return this.purpose;
    }

    public List<String> getModes(){
        return this.modes;
    }
}
