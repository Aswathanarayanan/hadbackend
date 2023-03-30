package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OnConfirmResponse {
    private String requestId;
    private String timestamp;
    private ConfirmAuth auth;
    private Err error;
    private Resp resp;

    public String getRequestid(){
        return this.requestId;
    }
    public ConfirmAuth getAuth(){
        return this.auth;
    }

}
