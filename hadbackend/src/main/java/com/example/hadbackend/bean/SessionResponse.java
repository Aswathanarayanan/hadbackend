package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SessionResponse {
    private String accesstoken;
    private int expiresin;
    private int refreshexpiresIn;
    private String refreshToken;
    private String tokentypt;

    public String getAccesstoken(){
        return this.accesstoken;
    }
}
