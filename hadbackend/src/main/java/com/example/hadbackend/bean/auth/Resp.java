package com.example.hadbackend.bean.auth;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Setter;

@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Resp {
    private String requestId;

    public String getRequestId(){
        return this.requestId;
    }
}
