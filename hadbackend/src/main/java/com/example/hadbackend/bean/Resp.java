package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Resp {
    private String requestId;

    public String getRequestId(){
        return this.requestId;
    }
}
