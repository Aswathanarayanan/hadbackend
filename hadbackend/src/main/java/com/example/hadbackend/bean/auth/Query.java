package com.example.hadbackend.bean.auth;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Query {
    private String id;
    private String purpose;
    private Requester requester;

    public String getId(){
        return this.id;
    }
    public  String getPurpose(){
        return this.purpose;
    }

    public Requester getRequester(){
        return this.requester;
    }

    public void setId(String id){
        this.id=id;
    }
    public void setPurpose(String purpose){
        this.purpose=purpose;
    }
    public void setRequester(Requester requester){
        this.requester=requester;
    }
}
