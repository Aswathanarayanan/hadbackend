package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SessionRequest {
    private String clientId;
    private String clientSecret;

    public void setClientId(String id){
        this.clientId=id;
    }

    public void setClientsecret(String key){
        this.clientSecret=key;
    }
}
