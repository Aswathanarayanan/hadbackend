package com.example.hadbackend.bean.auth;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Requester {
    private String type;
    private String id;

    public String getId(){
        return this.id;
    }

    public String getType(){
        return this.type;
    }

    public void setId(String id){
        this.id=id;
    }
    public void setType(String type){
        this.type=type;
    }
}
