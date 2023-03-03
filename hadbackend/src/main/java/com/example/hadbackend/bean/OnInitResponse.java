package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OnInitResponse {
    private String requstId;
    private String timestamp;
    private InitAuth auth;
    private Err error;
    private Resp resp;

    public void setInitAuth(InitAuth auth){
        this.auth=auth;
    }

    public InitAuth getInitAuth(){
        return this.auth;
    }
}
